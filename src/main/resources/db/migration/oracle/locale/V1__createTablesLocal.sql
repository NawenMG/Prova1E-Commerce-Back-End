-- V1__Create_Tables.sql

-- Creazione della tabella Categorie
CREATE TABLE Categorie (
    ID VARCHAR2(50) PRIMARY KEY,
    Nome VARCHAR2(100) UNIQUE
);

-- Creazione della tabella Pagamenti
CREATE TABLE Pagamenti (
    ID VARCHAR2(50) PRIMARY KEY,
    Type VARCHAR2(50),
    Data DATE DEFAULT SYSDATE,
    Status NUMBER(1) DEFAULT 0,  -- 0 per "false" e 1 per "true"
    Totale NUMBER(10, 2),
    CONSTRAINT unq_pagamenti_status UNIQUE (Status)
);

-- Creazione della tabella Prodotti
CREATE TABLE Prodotti (
    ID VARCHAR2(50) PRIMARY KEY,
    Nome VARCHAR2(100) NOT NULL,
    Prezzo NUMBER(10, 2) NOT NULL,
    Descrizione CLOB,
    Immagine BLOB,
    Quantita_Disponibile INT DEFAULT 0,
    Categoria VARCHAR2(50),
    Data_di_inserimento DATE DEFAULT SYSDATE,
    
    CONSTRAINT fk_categoria FOREIGN KEY (Categoria) REFERENCES Categorie(Nome)
);

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

-- Creazione della tabella Ordini
CREATE TABLE Ordini (
    ID VARCHAR2(50) PRIMARY KEY,
    User_id VARCHAR2(50),
    Stato_spedizione TIMESTAMP,
    Data_di_consegna DATE,
    Data_di_richiesta DATE DEFAULT SYSDATE,
    Accettazione_ordine NUMBER(1) DEFAULT 0,
    Status VARCHAR2(50),
    Corriere VARCHAR2(100),
    Posizione VARCHAR2(100),

    CONSTRAINT fk_user_ordini FOREIGN KEY (User_id) REFERENCES Users(ID),
    CONSTRAINT fk_status_pagamenti FOREIGN KEY (Status) REFERENCES Pagamenti(Status)
);

-- Creazione della tabella Resi
CREATE TABLE Resi (
    ID VARCHAR2(50) PRIMARY KEY,
    User_id VARCHAR2(50),
    Status NUMBER(1) DEFAULT 0,
    Accettazione_reso NUMBER(1) DEFAULT 0,
    Data_di_richiesta DATE DEFAULT SYSDATE,

    CONSTRAINT fk_user_resi FOREIGN KEY (User_id) REFERENCES Users(ID)
);

-- Creazione della tabella ponte User_WishList
CREATE TABLE User_WishList (
    User_ID VARCHAR2(50),
    Prodotto_ID VARCHAR2(50),
    CONSTRAINT fk_user_wishlist_user FOREIGN KEY (User_ID) REFERENCES Users(ID),
    CONSTRAINT fk_user_wishlist_prodotto FOREIGN KEY (Prodotto_ID) REFERENCES Prodotti(ID),
    PRIMARY KEY (User_ID, Prodotto_ID)
);

-- Creazione della tabella ponte User_Ordini
CREATE TABLE User_Ordini (
    User_ID VARCHAR2(50),
    Ordine_ID VARCHAR2(50),
    CONSTRAINT fk_user_ordini_user FOREIGN KEY (User_ID) REFERENCES Users(ID),
    CONSTRAINT fk_user_ordini_ordine FOREIGN KEY (Ordine_ID) REFERENCES Ordini(ID),
    PRIMARY KEY (User_ID, Ordine_ID)
);

-- Creazione della tabella ponte User_Resi
CREATE TABLE User_Resi (
    User_ID VARCHAR2(50),
    Reso_ID VARCHAR2(50),
    CONSTRAINT fk_user_resi_user FOREIGN KEY (User_ID) REFERENCES Users(ID),
    CONSTRAINT fk_user_resi_reso FOREIGN KEY (Reso_ID) REFERENCES Resi(ID),
    PRIMARY KEY (User_ID, Reso_ID)
);