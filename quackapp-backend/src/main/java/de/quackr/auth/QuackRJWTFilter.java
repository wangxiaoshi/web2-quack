package de.quackr.auth;

import com.nimbusds.jose.JWSObject;
import com.nimbusds.jwt.JWTClaimsSet;
import net.minidev.json.JSONObject;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import static de.quackr.auth.DefaultRealm.ROLE_ADMIN;
import static de.quackr.auth.DefaultRealm.ROLE_MODERATOR;

public class QuackRJWTFilter extends AuthenticatingFilter {

    private final Logger logger = LoggerFactory.getLogger(QuackRJWTFilter.class);


    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        final HttpServletRequest httpRequest = WebUtils.toHttp(servletRequest);
        final String authorizationHeader = httpRequest.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return new UsernamePasswordToken();
        }

        String token = authorizationHeader.split(" ")[1];

        try {
            final JWSObject jwsObject = JWSObject.parse(token);
            final JSONObject payload = jwsObject.getPayload().toJSONObject();
            final JWTClaimsSet claimsSet  = JWTClaimsSet.parse(payload);

            final Set<String> roles = new HashSet<>();

            if(claimsSet.getBooleanClaim(ROLE_ADMIN) != null && claimsSet.getBooleanClaim(ROLE_ADMIN)) {
                roles.add(ROLE_ADMIN);
            }
            if(claimsSet.getBooleanClaim(ROLE_MODERATOR) != null && claimsSet.getBooleanClaim(ROLE_MODERATOR)) {
                roles.add(ROLE_MODERATOR);
            }

            return new QuackRJWToken(claimsSet.getSubject(), token, roles);

        } catch (ParseException ex) {
            logger.warn("Could not parse JWT.", ex);
            return new UsernamePasswordToken();
        }

    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletResponse httpResponse = WebUtils.toHttp(servletResponse);

        if(!containsAuthorizationHeader(servletRequest)) {
            logger.debug("Received HTTP request without authorization header.");
            return true;
        }

        // Request contains authorization header, but Shiro didn't allow access anyway. Try to log in.
        boolean loggedIn = executeLogin(servletRequest, servletResponse);

        if (!loggedIn) {
            logger.debug("Received HTTP request with authorization header, but login FAILED.");
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        return true;
    }

    private boolean containsAuthorizationHeader(ServletRequest servletRequest) {
        return WebUtils.toHttp(servletRequest).getHeader("Authorization") != null;
    }

}
