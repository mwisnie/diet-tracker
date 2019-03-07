package mw.project.diettracker.security;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSecurityConstants {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_PREFIX = "Bearer ";
    public static final long TOKEN_EXPIRATION_TIME = 118000000L; // 5 hours
    public static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
    public static String JWT_SECRET;

    @Value("${authentication.jwt-secret}")
    public void setJwtSecret(String secret) {
        JWT_SECRET = secret;
    }

}
