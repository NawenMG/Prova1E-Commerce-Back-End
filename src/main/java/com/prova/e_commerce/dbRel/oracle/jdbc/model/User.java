package com.prova.e_commerce.dbRel.oracle.jdbc.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Users")
public class User implements UserDetails {

    @Id
    @Column(name = "ID", length = 50, nullable = false)
    private String id;

    @Column(name = "Nome", length = 100, nullable = false)
    private String nome;

    @Column(name = "Cognome", length = 100, nullable = false)
    private String cognome;

    @Column(name = "Ruolo", length = 50, nullable = false)
    private String ruolo;

    @Column(name = "Nome_Utente", length = 100, nullable = false, unique = true)
    private String nomeUtente;

    @Column(name = "Email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "Password", length = 255, nullable = false)
    private String password;

    @Lob
    @Column(name = "Immagine")
    private byte[] immagine;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    // Costruttore di default
    public User() {
        this.id = UUID.randomUUID().toString(); // Generazione automatica dell'ID
    }

    // Metodi di UserDetails
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Puoi implementare i ruoli qui se necessario
        return List.of(); // Restituisce una lista vuota se non ci sono ruoli associati
    }

    public String getUsername() {
        return nomeUtente; // Usa `nomeUtente` come username
    }

    public boolean isAccountNonExpired() {
        return true; // Puoi aggiungere logica personalizzata per il controllo
    }

    public boolean isAccountNonLocked() {
        return true; // Puoi aggiungere logica personalizzata per il controllo
    }

    public boolean isCredentialsNonExpired() {
        return true; // Puoi aggiungere logica personalizzata per il controllo
    }

    public boolean isEnabled() {
        return true; // Puoi aggiungere logica personalizzata per il controllo
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getRuolo() {
        return ruolo;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

    public String getNomeUtente() {
        return nomeUtente;
    }

    public void setNomeUtente(String nomeUtente) {
        this.nomeUtente = nomeUtente;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
