package com.prova.e_commerce.security.security1;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class TwoFactorAuthService {

    private final Map<String, String> userToCodeMap = new ConcurrentHashMap<>();

    public void send2FACode(String username) {
        String code = generateRandomCode();
        userToCodeMap.put(username, code);

        // Invio del codice tramite email/SMS
        sendCodeToUser(username, code);
    }

    public boolean verify2FACode(String username, String code) {
        return code.equals(userToCodeMap.get(username));
    }

    private String generateRandomCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    private void sendCodeToUser(String username, String code) {
        // Logica per inviare codice (es. EmailService o SmsService)
    }
}

