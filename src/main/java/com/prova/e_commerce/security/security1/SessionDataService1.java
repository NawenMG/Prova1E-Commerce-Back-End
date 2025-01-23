package com.prova.e_commerce.security.security1;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionDataService1 {

    private final Map<String, SessionData1> sessionStore = new ConcurrentHashMap<>();

    /**
     * Memorizza una sessione utente.
     * @param sessionId L'ID della sessione.
     * @param session La sessione utente da memorizzare.
     */
    public void storeSession(String sessionId, SessionData1 session) {
        sessionStore.put(sessionId, session);
    }

    /**
     * Recupera una sessione utente.
     * @param sessionId L'ID della sessione.
     * @return La sessione utente corrispondente o null se non esiste.
     */
    public SessionData1 getSession(String sessionId) {
        return sessionStore.get(sessionId);
    }

    /**
     * Recupera una sessione utente tramite il suo ID univoco.
     * @param sessionDataId L'ID univoco della sessione.
     * @return La sessione utente corrispondente o null se non esiste.
     */
    public SessionData1 getSessionById(String sessionDataId) {
        return sessionStore.values().stream()
                .filter(session -> session.getId().equals(sessionDataId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Recupera una sessione utente per username.
     * @param username Il nome utente associato alla sessione.
     * @return La sessione utente corrispondente o null se non esiste.
     */
    public SessionData1 getSessionByUsername(String username) {
        return sessionStore.values().stream()
                .filter(session -> session.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    /**
     * Invalida una sessione rimuovendola dall'archivio.
     * @param sessionId L'ID della sessione da invalidare.
     */
    public void invalidateSession(String sessionId) {
        sessionStore.remove(sessionId);
    }

    /**
     * Controlla se una sessione è valida.
     * @param sessionId L'ID della sessione.
     * @return True se la sessione esiste, false altrimenti.
     */
    public boolean isSessionValid(String sessionId) {
        return sessionStore.containsKey(sessionId);
    }
}
