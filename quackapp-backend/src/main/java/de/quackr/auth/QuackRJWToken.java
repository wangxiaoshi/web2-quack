package de.quackr.auth;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import org.apache.shiro.authc.AuthenticationToken;

import java.text.ParseException;
import java.util.Set;

public class QuackRJWToken implements AuthenticationToken {

    public static final String SHARED_KEY = "z$C&F)H@McQfTjWnZr4u7x!A%D*G-KaN";

    private final String username;
    private final String token;

    private final Set<String> roles;

    public QuackRJWToken(String username, String token, Set<String> roles) {
        this.username = username;
        this.token = token;
        this.roles = roles;
    }

    public boolean verify() {
        try {
            final SignedJWT signed = SignedJWT.parse(token);
            final JWSVerifier verifier = new MACVerifier(SHARED_KEY.getBytes());

            return signed.verify(verifier);
        } catch (ParseException | JOSEException ex) {
            return false;
        }
    }

    @Override
    public Object getPrincipal() {
        return getUsername();
    }

    @Override
    public Object getCredentials() {
        return getToken();
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public Set<String> getRoles() {
        return roles;
    }
}
