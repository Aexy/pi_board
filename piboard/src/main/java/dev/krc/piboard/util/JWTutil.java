package dev.krc.piboard.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JWTutil {

    @Value("${JWT.KEY}")
    private String jwtKey;
    private final long EXPIRATION_TIME = 1000*60*60*24; //24 HOURS

    /**
     * Used to generate a JWT valid for 24 hours
     * @param email
     * @return a JWT valid for 24 hours
     */
    public String generateToken(String email){
        return Jwts.builder()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .claim("email", email)
                .signWith(Keys.hmacShaKeyFor(jwtKey.getBytes()))
                .compact();
    }

    public boolean isValidToken(String token){
        try{
            Jwts.parser().verifyWith(getKey()).build().parse(token);
            return true;
        }catch (ExpiredJwtException | MalformedJwtException | SecurityException | IllegalArgumentException e){ // parse expections
            return false;
        }
    }

    public String extractEmailFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("email", String.class);
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(jwtKey.getBytes());
    }

}
