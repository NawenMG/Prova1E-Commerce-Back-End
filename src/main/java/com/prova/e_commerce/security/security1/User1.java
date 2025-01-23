package com.prova.e_commerce.security.security1;

import jakarta.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "Users")
public class User1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", length = 50, nullable = false)
    private Long userId;

    @Column(name = "Nome", length = 100, nullable = false)
    private String nome;

    @Column(name = "Cognome", length = 100, nullable = false)
    private String cognome;

    @Column(name = "Nome_Utente", length = 100, nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "Email", length = 100, nullable = false, unique = true)
    private String email;

    @Lob
    @Column(name = "Immagine")
    private byte[] immagine;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Column(name = "Ruolo", length = 50, nullable = false)
    private Set<Role1> roles = new HashSet<>();

    @Column(name = "TwoFA", nullable = false)
    private boolean twoFactorEnabled;

    @Column(name = "Blocked", nullable = false)
    private boolean blocked;

    // Getters and Setters

    public Long getId() {
        return userId;
    }

    public void setId(Long id) {
        this.userId = id;
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
