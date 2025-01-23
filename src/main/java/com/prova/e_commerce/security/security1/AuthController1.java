package com.prova.e_commerce.security.security1;

import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@RestController
@RequestMapping("/auth")
public class AuthController1 {

    @Autowired
    private User1Service userService;

    @Autowired
    private JwtUtil1 jwtUtil;

    @Autowired
    private SessionDataService1 sessionManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TwoFactorAuthService twoFactorAuthService; // Servizio per il 2FA

    @Autowired
    private PasswordRecoveryService passwordRecoveryService; // Servizio per il recupero password

    /**
     * Endpoint per registrare un nuovo utente.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        if (userService.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        }

        User1 user = new User1();
        user.setId(request.getUserId());
        user.setNome(request.getNome());
        user.setCognome(request.getCognome());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRoles(request.getRoles()); // Set dei ruoli passati nella richiesta
        user.setImmagine(request.getImmagine());
        user.setCreatedAt(request.getCreatedAt());
        user.setUpdatedAt(request.getUpdatedAt());
        user.setTwoFactorEnabled(request.isTwoFactorEnabled()); // Abilita 2FA se richiesto
        user.setBlocked(request.isBlocked());
        userService.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    /**
     * Endpoint per effettuare il login.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        User1 user = userService.findByUsername(request.getUsername());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        if (user.isTwoFactorEnabled()) {
            // Se 2FA è abilitato, invia un codice 2FA e richiedi verifica
            twoFactorAuthService.send2FACode(user.getUsername());
            return ResponseEntity.ok(new LoginResponse("2FA_CODE_SENT"));
        }

        // Autenticazione normale: genera un token JWT
        String sessionId = java.util.UUID.randomUUID().toString();
        String jwt = jwtUtil.generateToken(sessionId);

        SessionData1 session = new SessionData1(user.getUsername(), user.getRoles());
        sessionManager.storeSession(sessionId, session);

        return ResponseEntity.ok(new LoginResponse(jwt));
    }

    /**
     * Endpoint per verificare il codice 2FA.
     */
    @PostMapping("/verify-2fa")
    public ResponseEntity<LoginResponse> verify2FA(@RequestBody TwoFactorRequest request) {
        boolean isValid = twoFactorAuthService.verify2FACode(request.getUsername(), request.getCode());

        if (!isValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Genera un token JWT e ritorna all'utente
        String sessionId = sessionManager.getSessionByUsername(request.getUsername()).getId();
        String jwt = jwtUtil.generateToken(sessionId);

        return ResponseEntity.ok(new LoginResponse(jwt));
    }

    /**
     * Endpoint per effettuare il logout.
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }

        String jwt = token.substring(7); // Rimuovi "Bearer "
        if (jwtUtil.validateToken(jwt)) {
            String sessionId = jwtUtil.extractSessionId(jwt);
            sessionManager.invalidateSession(sessionId);
            return ResponseEntity.ok("Logged out successfully");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
    }

    /**
     * Endpoint per richiedere il recupero della password.
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        passwordRecoveryService.sendPasswordResetToken(request.getEmail());
        return ResponseEntity.ok("Password reset email sent.");
    }

    /**
     * Endpoint per reimpostare la password.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            passwordRecoveryService.resetPassword(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok("Password reset successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

class TwoFactorRequest {
    private String username;
    private String code;

    // Getter e Setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

class ForgotPasswordRequest {
    private String email;

    // Getter e Setter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

class ResetPasswordRequest {
    private String token;
    private String newPassword;

    // Getter e Setter
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

class RegisterRequest {

    private Long userId;

    @NotBlank(message = "Il nome non può essere vuoto")
    @Size(max = 100, message = "Il nome deve contenere massimo 100 caratteri")
    private String nome;

    @NotBlank(message = "Il cognome non può essere vuoto")
    @Size(max = 100, message = "Il cognome deve contenere massimo 100 caratteri")
    private String cognome;

    @NotBlank(message = "Il nome utente non può essere vuoto")
    @Size(max = 100, message = "Il nome utente deve contenere massimo 100 caratteri")
    private String username;

    @NotBlank(message = "La password non può essere vuota")
    @Size(min = 8, message = "La password deve contenere almeno 8 caratteri")
    private String password;

    @NotBlank(message = "L'email non può essere vuota")
    @Email(message = "L'email deve essere valida")
    @Size(max = 100, message = "L'email deve contenere massimo 100 caratteri")
    private String email;

    private byte[] immagine;

    private Date createdAt;

    private Date updatedAt;

    @NotNull(message = "I ruoli non possono essere nulli")
    private Set<Role1> roles;

    private boolean twoFactorEnabled;

    private boolean blocked;

    // Getters and Setters

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getImmagine() {
        return immagine;
    }

    public void setImmagine(byte[] immagine) {
        this.immagine = immagine;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<Role1> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role1> roles) {
        this.roles = roles;
    }

    public boolean isTwoFactorEnabled() {
        return twoFactorEnabled;
    }

    public void setTwoFactorEnabled(boolean twoFactorEnabled) {
        this.twoFactorEnabled = twoFactorEnabled;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}


class LoginRequest {
    private String username;
    private String password;

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
}

class LoginResponse {
    private String token;

    // Costruttore
    public LoginResponse(String token) {
        this.token = token;
    }

    // Getter e Setter
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
