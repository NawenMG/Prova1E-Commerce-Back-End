package com.prova.e_commerce.dbRel.oracle.jdbc.model;

/* import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size; */

public class Categorie {

    /* @NotNull(message = "Obbligatorio")
    @Size(max = 50, message = "L'ID della categoria non può superare i 50 caratteri") */
    private String categoryID;  // Chiave primaria (modificato in String, corrispondente a VARCHAR2(50))

    /* @NotBlank(message = "Obbligatorio")
    @Size(max = 100, message = "Il nome della categoria non può superare i 100 caratteri") */
    private String name;       // Nome della categoria

    public Categorie() {
        
    }

    // Getter e Setter
    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
