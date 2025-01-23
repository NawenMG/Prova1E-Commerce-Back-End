package com.prova.e_commerce.security.security1;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SessionData1 {

    private String id; // ID univoco della sessione
    private String username;
    private Set<Role1> roles;
    private long lastAccessTime; // Nuovo campo per l'ultimo accesso

    public SessionData1(String username, Set<Role1> roles) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.roles = roles;
        this.lastAccessTime = System.currentTimeMillis(); // imposta un valore di default
    }

    // Se vuoi un costruttore di default
    public SessionData1() {
        this.id = UUID.randomUUID().toString();
        this.lastAccessTime = System.currentTimeMillis();
    }

    // Getter/Setter standard
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Role1> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role1> roles) {
        this.roles = roles;
    }

    // Aggiungi un metodo che setta l'ultimo accesso
    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    // Se in AuthController si invoca setRole(String), definisci un metodo dedicato:
    public void setRole(String role) {
        // Se la logica prevede un singolo ruolo, trasformi la stringa in Role1
        // e metti nel set, rimuovendo eventuali ruoli precedenti:
        if (roles == null) {
            roles = new HashSet<>();
        } else {
            roles.clear();
        }
        roles.add(Role1.valueOf(role));
    }

    // (opzionale) Se vuoi recuperare un singolo ruolo
    public String getRole() {
        if (roles == null || roles.isEmpty()) {
            return null;
        }
        // Ritorna il nome della prima costante
        return roles.iterator().next().name();
    }

    // (opzionale) Se preferisci salvare più ruoli, allora i metodi setRole(String)
    // non hanno molto senso, e dovresti usare setRoles(Set<Role1>) come stai già facendo.

    @Override
    public String toString() {
        return "SessionData1{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", roles=" + roles +
                ", lastAccessTime=" + lastAccessTime +
                '}';
    }
}
