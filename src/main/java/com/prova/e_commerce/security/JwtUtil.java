/* package com.prova.e_commerce.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;

    // Carica la chiave segreta dal file di configurazione
    public JwtUtil(@Value("${jwt.secret}") String jwtSecret) {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    } */

    /**
     * Genera un JWT con un determinato sessionId e durata.
     */
    /* public String generateToken(String sessionId, long expiration) {
        return Jwts.builder()
                .setSubject(sessionId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey, SignatureAlgorithm.HS256) // Firma sicura con chiave segreta
                .compact();
    } */

    /**
     * Estrae il sessionId dal token JWT.
     */
    /* public String extractSessionId(String token) {
        return getClaims(token).getSubject();
    }
 */
    /**
     * Valida il token JWT.
     */
   /*  public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            System.out.println("Invalid JWT Token: " + e.getMessage());
            return false;
        }
    } */

    /**
     * Estrae i Claims dal token.
     */
    /* private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
 */