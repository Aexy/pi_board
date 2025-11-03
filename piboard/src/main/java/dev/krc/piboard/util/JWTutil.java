package dev.krc.piboard.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTutil {

    @Value("${JWT.KEY}")
    private String jwtKey;
    private final long EXPIRATION_TIME = 1000*60*60*24L; //24 HOURS

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

    //**************MCP Related Functions**************
    private final long MCP_EXPIRATION_TIME = 1000*60*60*24*365L; //1 YEAR
    @Getter
    @Setter
    private String localJWT = "";
    private static final String STORAGE_DIR = System.getenv("LOCALAPPDATA");
    private static final String PI_FOLDER = ".piboard";
    private static final String TOKEN_FILE = "token.json";

    public String generateMcpToken(String email){
        return Jwts.builder()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + MCP_EXPIRATION_TIME))
                .claim("email", email)
                .signWith(Keys.hmacShaKeyFor(jwtKey.getBytes()))
                .compact();
    }

    private void saveToFile() throws IOException {
        Path pipath = Paths.get(STORAGE_DIR, PI_FOLDER);
        if(!Files.exists(pipath)){
            Files.createDirectories(pipath);
        }
        Path tokenpath = pipath.resolve(TOKEN_FILE);
        Map<String,String> tokenMap = new HashMap<>();
        tokenMap.put("jwt",localJWT);
        new ObjectMapper().writeValue(tokenpath.toFile(), tokenMap);
    }

    public boolean saveLocalJwt(String localJWT) {
        this.localJWT = localJWT;
        try{
            saveToFile();
        }catch (IOException e){
            return false;
        }
        return true;
    }

}
