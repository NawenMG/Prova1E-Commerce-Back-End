-- Creazione della tabella Users
CREATE TABLE Users (
    ID VARCHAR2(50) PRIMARY KEY,
    Nome VARCHAR2(100) NOT NULL,
    Cognome VARCHAR2(100) NOT NULL,
    Ruolo VARCHAR2(50) NOT NULL,
    Nome_Utente VARCHAR2(100) UNIQUE NOT NULL,
    Email VARCHAR2(100) UNIQUE NOT NULL,
    Password VARCHAR2(255) NOT NULL,  -- Aggiungi hash
    Immagine BLOB
);