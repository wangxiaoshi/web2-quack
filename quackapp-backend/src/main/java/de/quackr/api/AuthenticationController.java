package de.quackr.api;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import de.quackr.auth.QuackRJWToken;
import de.quackr.persistence.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.UUID;

import static de.quackr.auth.DefaultRealm.ROLE_ADMIN;
import static de.quackr.auth.DefaultRealm.ROLE_MODERATOR;

@Path("/auth")
public class AuthenticationController {

    private Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @EJB
    IOController ioController;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticate(LoginData credentials) {
        if (credentials == null || credentials.getUsername() == null || credentials.getPassword() == null) {
            logger.debug("Received request to authenticate, but credentials are missing.");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        User user = ioController.getUser(credentials.getUsername());

        if (user == null || !user.getPasswordHash().equals(credentials.getPassword())) {
            logger.debug("Tried to authenticate, but received invalid credentials.");
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        String token;
        try {
            token = this.createToken(user);
        } catch (JOSEException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        logger.debug("Successfully authenticated \"{}\"", credentials.username);
        return Response.ok(token).build();
    }

    private String createToken(User user) throws JOSEException {
        final JWTClaimsSet.Builder claimsSetBuilder = new JWTClaimsSet.Builder();

        claimsSetBuilder.subject(user.getUsername());
        claimsSetBuilder.issueTime(new Date(System.currentTimeMillis()));
        claimsSetBuilder.jwtID(UUID.randomUUID().toString());

        if (user.isAdmin()) {
            claimsSetBuilder.claim(ROLE_ADMIN, true);
        }
        if (user.isModerator()) {
            claimsSetBuilder.claim(ROLE_MODERATOR, true);
        }

        final JWTClaimsSet claimsSet = claimsSetBuilder.build();

        final JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        final Payload payload = new Payload(claimsSet.toJSONObject());
        final JWSObject jwsObject = new JWSObject(header, payload);

        jwsObject.sign(new MACSigner(QuackRJWToken.SHARED_KEY.getBytes()));

        return jwsObject.serialize();
    }


    public static class LoginData {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
