package com.prova.e_commerce.dbRel.oracle.jdbc.model;

/* import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size; */

public class Users {

    /* @NotBlank(message = "Obbligatorio") */
    private String usersID;  // ID, cambiato a String per corrispondere a VARCHAR2(50)

    /* @NotBlank(message = "Obbligatorio")
    @Size(max = 100, message = "Il nome non può superare i 100 caratteri") */
    private String nome;          // Nome

    /* @NotBlank(message = "Obbligatorio")
    @Size(max = 100, message = "Il cognome non può superare i 100 caratteri") */
    private String cognome;       // Cognome

    /* @NotBlank(message = "Obbligatorio")
    @Size(max = 50, message = "Il ruolo non può superare i 50 caratteri") */
    private String ruolo;         // Ruolo

    /* @NotBlank(message = "Obbligatorio")
    @Size(max = 100, message = "Il nome utente non può superare i 100 caratteri") */
    private String nomeUtente;    // Nome utente (unico)

    /* @NotBlank(message = "Obbligatorio")
    @Email(message = "L'email non è valida")
    @Size(max = 100, message = "L'email non può superare i 100 caratteri") */
    private String email;         // Email (unica)

    /* @NotBlank(message = "Obbligatorio")
    @Size(min = 10, message = "La password deve essere lunga almeno 10 caratteri") */
    private String password;      // Password (hash, rimuovere la validazione di lunghezza stretta)

    private byte[] immagine;      // Immagine (BLOB trattato come byte[])

    // Costruttore
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
