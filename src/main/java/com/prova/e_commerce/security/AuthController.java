package com.prova.e_commerce.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private SessionService sessionService;

    // Mock database
    private final ConcurrentHashMap<String, String> userStore = new ConcurrentHashMap<>();

    /**
     * Registrazione di un nuovo utente
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam @NotBlank String username, @RequestParam @NotBlank String password) {
        logger.info("Registering new user: {}", username);

        if (userStore.containsKey(username)) {
            logger.warn("Registration failed: User {} already exists", username);
            return ResponseEntity.badRequest().body("User already exists!");
        }

        userStore.put(username, password); // Salva utente nel mock DB
        logger.info("User {} registered successfully", username);
        return ResponseEntity.ok("User registered successfully!");
    }

    /**
     * Login dell'utente
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam @NotBlank String username, @RequestParam @NotBlank String password) {
        logger.info("Login attempt for user: {}", username);

        if (userStore.containsKey(username) && userStore.get(username).equals(password)) {
            logger.info("User {} authenticated successfully", username);

            String sessionId = UUID.randomUUID().toString();

            // Salva i dati della sessione
            SessionData data = new SessionData();
            data.setUsername(username);
            data.setRole("USER");
            data.setLastAccessTime(System.currentTimeMillis());
            sessionService.saveSession(sessionId, data);

            String jwt = jwtUtil.generateToken(sessionId, 86400000); // Token valido per 1 giorno
            return ResponseEntity.ok().body(new LoginResponse(jwt, "Login successful!"));
        }

        logger.warn("Invalid credentials for user: {}", username);
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    /**
     * Logout dell'utente
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            if (jwtUtil.validateToken(token)) {
                String sessionId = jwtUtil.extractSessionId(token);
                sessionService.invalidateSession(sessionId);
                logger.info("User session invalidated for session ID: {}", sessionId);
                return ResponseEntity.ok("User logged out successfully!");
            }
        }

        logger.warn("Logout failed: Invalid or missing JWT token");
        return ResponseEntity.badRequest().body("Invalid or missing token");
    }

    /**
     * Endpoint per ottenere i dettagli dell'utente OAuth2
     */
    @GetMapping("/oauth2/userinfo")
    public ResponseEntity<?> getOAuth2UserInfo(@AuthenticationPrincipal OAuth2User oauth2User) {
        if (oauth2User == null) {
            return ResponseEntity.status(401).body("User not authenticated via OAuth2");
        }

        // Recupera informazioni dell'utente OAuth2
        logger.info("OAuth2 User: {}", oauth2User.getAttributes());

        return ResponseEntity.ok(oauth2User.getAttributes());
    }

    /**
     * Classe di supporto per la risposta del login
     */
    public static class LoginResponse {
        private String token;
        private String message;

        public LoginResponse(String token, String message) {
            this.token = token;
            this.message = message;
        }

        public String getToken() {
            return token;
        }

        public String getMessage() {
            return message;
        }
    }
}
