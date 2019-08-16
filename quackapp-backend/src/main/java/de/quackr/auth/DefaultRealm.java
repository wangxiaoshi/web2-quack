package de.quackr.auth;


import de.quackr.api.IOController;
import de.quackr.persistence.entities.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class DefaultRealm extends AuthorizingRealm {

    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_MODERATOR = "moderator";

    private final Logger logger = LoggerFactory.getLogger(DefaultRealm.class);

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof QuackRJWToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        final QuackRJWToken jwt = (QuackRJWToken) token;

        if (!jwt.verify()) {
            throw new AuthenticationException();
        }

        logger.debug("JWT verified for user \"{}\".", jwt.getUsername());

        this.updateUserLastActiveTimestamp(jwt.getUsername());

        return new SimpleAccount(jwt.getPrincipal(), jwt.getCredentials(), this.getName(), jwt.getRoles(), Collections.emptySet());
    }


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // get ioController
        final Instance<IOController> iocInstance = CDI.current().select(IOController.class);
        if (!iocInstance.isResolvable()) {
            throw new AuthorizationException();
        }

        // get user
        User user = iocInstance.get().getUser((String) principalCollection.getPrimaryPrincipal());
        if(user == null) {
            throw new AuthorizationException();
        }

        // check roles
        Set<String> roles = new HashSet<>();

        if(user.isAdmin()) {
            roles.add(ROLE_ADMIN);
        }
        if(user.isModerator()) {
            roles.add(ROLE_MODERATOR);
        }

        // return SimpleAuthorizationInfo containing the user's roles
        logger.debug("Returning authorization info for user \"{}\" (#{})", user.getUsername(), user.getId());
        return new SimpleAuthorizationInfo(roles);
    }

    private void updateUserLastActiveTimestamp(String username) {
        // get ioController
        final Instance<IOController> iocInstance = CDI.current().select(IOController.class);
        if (!iocInstance.isResolvable()) {
            return;
        }
        IOController ioController = iocInstance.get();

        // get user
        User user = ioController.getUser(username);
        User updatedUser = new User();

        updatedUser.setId(user.getId());
        updatedUser.setLastActiveTimestamp(new Date(System.currentTimeMillis()));

        ioController.updateUser(user.getId(), updatedUser);
    }
}
