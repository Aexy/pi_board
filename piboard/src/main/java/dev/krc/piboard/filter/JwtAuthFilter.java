package dev.krc.piboard.filter;

import dev.krc.piboard.util.JWTutil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JWTutil jwtutil;

    public JwtAuthFilter(JWTutil jwtutil) {
        this.jwtutil = jwtutil;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = null;
        String auth = request.getHeader("Authorization");

        if(auth != null && auth.startsWith("Bearer ")) {
            jwt = auth.substring(7);
        } else if (request.getSession().getAttribute("jwt") instanceof String sessionJwt) {
            jwt = sessionJwt;
        }

        if(jwt != null && jwtutil.isValidToken(jwt)) {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(jwtutil.extractEmailFromToken(jwt), null, List.of());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }
}
