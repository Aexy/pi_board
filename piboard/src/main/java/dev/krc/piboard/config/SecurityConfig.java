package dev.krc.piboard.config;

import dev.krc.piboard.filter.JwtAuthFilter;
import dev.krc.piboard.security.FallBackAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity()
public class SecurityConfig {
    public static final int BCRYPT_PASSWORD_STRENGTH = 14;
    private final JwtAuthFilter jwtAuthFilter;
    private final FallBackAuthenticationEntryPoint fallback;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, FallBackAuthenticationEntryPoint fallback) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.fallback = fallback;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //jwt managed session
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->auth
                        .requestMatchers("/login","/register", "/css/**", "/js/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex ->ex.authenticationEntryPoint(fallback))
                ;
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(BCRYPT_PASSWORD_STRENGTH);
    }
}
