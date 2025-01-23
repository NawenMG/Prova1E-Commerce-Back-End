package com.prova.e_commerce.security.security1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {

    private final User1Service userService;
    private final JwtUtil1 jwtUtil;
    private final SessionDataService1 sessionManager;

    public OAuth2Controller(User1Service userService, JwtUtil1 jwtUtil, SessionDataService1 sessionManager) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.sessionManager = sessionManager;
    }

    /**
     * Successo del login OAuth2
     */
    @GetMapping("/success")
    public ResponseEntity<String> success(@AuthenticationPrincipal OidcUser oidcUser) {
        String email = oidcUser.getEmail();
        System.out.println("Utente autenticato: " + email);

        // Salva o aggiorna l'utente nel database
        if (!userService.existsByUsername(email)) {
            User1 newUser = new User1();
            newUser.setUsername(email);
            newUser.setPassword("");
            newUser.setRoles(Set.of(Role1.USER)); // Ruolo predefinito
            userService.save(newUser);
        }

        // Genera un token JWT
        String sessionId = java.util.UUID.randomUUID().toString();
        String jwt = jwtUtil.generateToken(sessionId);

        // Memorizza i dettagli della sessione
        SessionData1 session = new SessionData1(email, Set.of(Role1.USER));
        sessionManager.storeSession(sessionId, session);

        return ResponseEntity.ok("Login effettuato con successo. Token JWT: " + jwt);
    }

    /**
     * Fallimento del login OAuth2
     */
    @GetMapping("/failure")
    public ResponseEntity<String> failure() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login fallito.");
    }

    /**
     * Logout
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7); // Rimuovi "Bearer "
            if (jwtUtil.validateToken(jwt)) {
                String sessionId = jwtUtil.extractSessionId(jwt);
                sessionManager.invalidateSession(sessionId);
                return ResponseEntity.ok("Logout effettuato con successo.");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token non valido.");
    }

    /**
     * Registrazione manuale
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestOAuth2 request) {
        if (userService.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'username esiste già.");
        }

        User1 user = new User1();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword()); // La password può essere codificata lato client
        user.setRoles(request.getRoles());
        userService.save(user);

        return ResponseEntity.ok("Registrazione avvenuta con successo.");
    }
}

/**
 * Classe per la richiesta di registrazione
 */
class RegisterRequestOAuth2 {
    private String username;
    private String password;
    private Set<Role1> roles;

    // Getter e Setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role1> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role1> roles) {
        this.roles = roles;
    }
}
