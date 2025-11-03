package dev.krc.piboard.annotation;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.krc.piboard.util.JWTutil;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

@Aspect
@Component
public class LocalJwtPresentAspect {

    private final JWTutil jwtutil;

    private static final Path JWT_PATH = Path.of(System.getenv("LOCALAPPDATA"), ".piboard","token.json");

    public LocalJwtPresentAspect(){
        jwtutil = new JWTutil();
    }

    @Before("@annotation(LocalJwtPresent)")
    public void before() {
        if(jwtutil.getLocalJWT().isEmpty()){
            getFromFile();
        }
    }

    public void getFromFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,String> map = null;
        try {
            map = objectMapper.readerFor(Map.class).readValue(JWT_PATH.toFile());
        } catch (IOException e) {
            jwtutil.setLocalJWT("");
        }
        if(map != null && map.containsKey("jwt")){
            jwtutil.setLocalJWT(map.get("jwt"));
        }
    }
}
