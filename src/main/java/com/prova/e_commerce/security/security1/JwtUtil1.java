package com.prova.e_commerce.security.security1;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil1 {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    private final long expiration = 86400000; // 1 giorno in millisecondi

    public JwtUtil1() throws Exception {
        // Carica la chiave privata
        String privateKeyContent = new String(Files.readAllBytes(Paths.get("private_key.pem")))
                .replaceAll("\\n", "")
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "");
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyContent);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        this.privateKey = keyFactory.generatePrivate(keySpec);

        // Carica la chiave pubblica
        String publicKeyContent = new String(Files.readAllBytes(Paths.get("public_key.pem")))
                .replaceAll("\\n", "")
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "");
        byte[] pubKeyBytes = Base64.getDecoder().decode(publicKeyContent);
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubKeyBytes);
        this.publicKey = keyFactory.generatePublic(pubKeySpec);
    }

    public String generateToken(String sessionId) {
        return Jwts.builder()
                .setSubject(sessionId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(privateKey, SignatureAlgorithm.ES512)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String extractSessionId(String token) {
        return getClaims(token).getSubject();
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
