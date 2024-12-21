package com.prova.e_commerce.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private SessionService sessionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");
        String token = null;

        // Controlla se l'header Authorization è presente e valido
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }

        if (token != null && jwtUtil.validateToken(token)) {
            String sessionId = jwtUtil.extractSessionId(token);
            SessionData sessionData = sessionService.getSession(sessionId);

            if (sessionData != null) {
                // Imposta il contesto di sicurezza con i dati della sessione
                SecurityContextHolder.getContext().setAuthentication(new SessionAuthenticationToken(sessionData));
            } else {
                logger.warn("Session data not found for session ID: " + sessionId);
            }
        } else {
            logger.warn("Invalid or missing JWT token");
        }

        // Passa la richiesta al prossimo filtro
        filterChain.doFilter(request, response);
    }
}
