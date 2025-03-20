package io.sanlam.bankaccountwithdrawal.jwt;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey jwtsecretkey;
    private static final long EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000; // 7 days in milliseconds (604800000)

    public JwtUtil() {
        // Load .env file and set system properties
        Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        // Retrieve the JWT secret
        String jwtsecretkey = System.getProperty("JWT_SECRET");
        if (jwtsecretkey == null || jwtsecretkey.isEmpty()) {
            throw new IllegalStateException("JWT_SECRET is not set in the environment!");
        }
        //creating a cryptographic key for signing JWTs using HMAC (Hash-based Message Authentication Code).
        this.jwtsecretkey = Keys.hmacShaKeyFor(jwtsecretkey.getBytes());
    }

    // Generate a JWT token for the given username
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(jwtsecretkey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtsecretkey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token, String username) {
        return username.equals(extractUsername(token)) && !isTokenExpired(token);
    }

    // Check if the token has expired
    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(jwtsecretkey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }

}