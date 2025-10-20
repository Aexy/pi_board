package dev.krc.piboard.filter;

import dev.krc.piboard.util.JWTutil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JWTutil jwtutil;

    public JwtAuthFilter(JWTutil jwtutil) {
        this.jwtutil = jwtutil;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(request.getSession().getAttribute("jwt") instanceof String jwt &&  jwtutil.isValidToken(jwt)){
            UsernamePasswordAuthenticationToken tokenAuth = new UsernamePasswordAuthenticationToken(jwtutil.extractEmailFromToken(jwt), null, List.of());
            SecurityContextHolder.getContext().setAuthentication(tokenAuth);
        }
        filterChain.doFilter(request, response);
    }
}
