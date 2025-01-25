/* package com.prova.e_commerce.Config;

import com.prova.e_commerce.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Disabilita CSRF per API stateless
                .csrf(csrf -> csrf.disable())

                // Autorizzazione delle richieste
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/oauth2/**", "/login/**").permitAll() // Percorsi pubblici
                        .anyRequest().authenticated() // Protegge tutti gli altri percorsi
                )

                // Configurazione per OAuth2
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization/google") // Pagina di login per Google OAuth2
                        .defaultSuccessUrl("/home", true) // Pagina di reindirizzamento dopo login
                )

                // Aggiunge il filtro JWT prima del filtro di autenticazione di default
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // Configurazione logout
                .logout(logout -> logout
                        .logoutSuccessUrl("/auth/logout-success") // Reindirizza alla pagina dopo il logout
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .build();
    }
}
 */