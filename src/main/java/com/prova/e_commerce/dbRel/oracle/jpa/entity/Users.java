package com.prova.e_commerce.dbRel.oracle.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

/* import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size; */

@Entity
@Table(name = "Users")  // Imposta il nome della tabella nel database
public class Users {

    @Id
    @Column(name = "ID")  // Definisce il nome della colonna nel database
    private String usersID;

   /*  @NotBlank(message = "Il nome è obbligatorio")
    @Size(max = 100, message = "Il nome non può superare i 100 caratteri") */
    @Column(name = "Nome")
    private String nome;  // Nome

    /* @NotBlank(message = "Il cognome è obbligatorio")
    @Size(max = 100, message = "Il cognome non può superare i 100 caratteri") */
    @Column(name = "Cognome")
    private String cognome;  // Cognome

    /* @NotBlank(message = "Il ruolo è obbligatorio")
    @Size(max = 50, message = "Il ruolo non può superare i 50 caratteri") */
    @Column(name = "Ruolo")
    private String ruolo;  // Ruolo

   /*  @NotBlank(message = "Il nome utente è obbligatorio")
    @Size(max = 100, message = "Il nome utente non può superare i 100 caratteri") */
    @Column(name = "Nome_Utente", unique = true)  // Imposta "nome_utente" come unico
    private String nomeUtente;  // Nome utente (unico)

    /* @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "L'email non è valida")
    @Size(max = 100, message = "L'email non può superare i 100 caratteri") */
    @Column(name = "Email", unique = true)  // Imposta "email" come unica
    private String email;  // Email (unica)

   /*  @NotBlank(message = "La password è obbligatoria")
    @Size(min = 10, message = "La password deve essere lunga almeno 10 caratteri") */
    @Column(name = "Password")
    private String password;  // Password (hash)

    @Lob
    @Column(name = "Immagine")  // Usa @Lob per il tipo BLOB
    private byte[] immagine;  // Immagine (BLOB trattato come byte[])

    // Costruttore senza argomenti
    public Users() {
    }

    // Getter e Setter

    public String getUsersID() {
        return usersID;
    }

    public void setUsersID(String usersID) {
        this.usersID = usersID;
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
}
