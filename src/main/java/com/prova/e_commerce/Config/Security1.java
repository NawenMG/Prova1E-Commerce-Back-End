package com.prova.e_commerce.Config;

import com.prova.e_commerce.security.security1.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;



@Configuration
public class Security1 {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public Security1(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Disabilita CSRF poiché utilizziamo JWT
            .authorizeHttpRequests(auth -> auth
                // Accesso pubblico agli endpoint di autenticazione
                .requestMatchers("/auth/**").permitAll()
                
                // Accesso per utenti con ruolo USER a specifiche rotte
                .requestMatchers("/user/**").hasRole("USER")
                
                // Accesso per utenti con ruolo ADMIN a specifiche rotte
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                // Accesso per CONTROLLER a una specifica rotta
                .requestMatchers("/controller/**").hasRole("CONTROLLER")
                
                // Accesso per USERAI e USERMONITORING a una specifica rotta
                .requestMatchers("/ai/**").hasAnyRole("USERAI", "ADMIN")
                .requestMatchers("/monitoring/**").hasRole("USERMONITORING")
                
                // Tutte le altre richieste richiedono autenticazione
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/auth/login")
                .defaultSuccessUrl("/oauth2/success")
                .failureUrl("/oauth2/failure")
                .userInfoEndpoint(userInfo -> userInfo.oidcUserService(oidcUserService()))
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Aggiungi filtro JWT
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authException) -> 
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                .accessDeniedHandler((request, response, accessDeniedException) -> 
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden"))
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Usa BCrypt per la codifica delle password
    }

    @Bean
    public OidcUserService oidcUserService() {
        return new OidcUserService();
    }
}
