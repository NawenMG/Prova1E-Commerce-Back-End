package com.prova.e_commerce.security.security1;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class PasswordRecoveryService {

    private final Map<String, String> tokenToEmailMap = new ConcurrentHashMap<>();

    public void sendPasswordResetToken(String email) {
        String token = UUID.randomUUID().toString();
        tokenToEmailMap.put(token, email);

        // Invia email con il link di reset
        sendResetEmail(email, token);
    }

    public void resetPassword(String token, String newPassword) {
        String email = tokenToEmailMap.get(token);

        if (email != null) {
            // Reimposta la password (criptata)
            updateUserPassword(email, newPassword);
            tokenToEmailMap.remove(token);
        } else {
            throw new IllegalArgumentException("Invalid or expired token");
        }
    }

    private void sendResetEmail(String email, String token) {
        // Invia un'email con il link di reset
        String resetLink = "https://your-app.com/reset-password?token=" + token;
        // Usa un EmailService per inviare l'email
    }

    private void updateUserPassword(String email, String newPassword) {
        // Logica per aggiornare la password nel database
        // Esempio: userRepository.updatePassword(email, bcryptEncoder.encode(newPassword));
    }
}

