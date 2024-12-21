package com.prova.e_commerce.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class SessionAuthenticationToken extends AbstractAuthenticationToken {

    private final SessionData sessionData;

    // Costruttore con solo SessionData
    public SessionAuthenticationToken(SessionData sessionData) {
        super(null); // Nessuna autorità assegnata inizialmente
        this.sessionData = sessionData;
        setAuthenticated(true); // Marca il token come autenticato
    }

    // Costruttore completo con authorities (se richiesto in futuro)
    public SessionAuthenticationToken(SessionData sessionData, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.sessionData = sessionData;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null; // Nessuna credenziale da restituire
    }

    @Override
    public Object getPrincipal() {
        return sessionData.getUsername(); // Restituisce l'utente come principale
    }

    public SessionData getSessionData() {
        return sessionData;
    }
}
