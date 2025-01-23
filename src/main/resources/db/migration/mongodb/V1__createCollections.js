// v1__createTables.js

// Connessione al database MongoDB
db = db.getSiblingDB("mongodb"); // Sostituisci con il nome del tuo database

// Creazione della collezione 'schedeProdotti'
db.createCollection("schedeProdotti");

// Creazione della collezione 'Recensioni'
db.createCollection("Recensioni");

// (Opzionale) Creazione degli indici per migliorare le performance delle query

// Indice univoco per 'ID' nella collezione 'schedeProdotti'
db.schedeProdotti.createIndex({ "ID": 1 }, { unique: true });
db.schedeProdotti.createIndex({ "User_id": 1 }, { unique: true });


// Indice per 'Product_id' e 'User_id' nella collezione 'Recensioni'
db.Recensioni.createIndex({ "Product_id": 1 }, { unique: true });
db.Recensioni.createIndex({ "User_id": 1 }, { unique: true });

// Log di conferma
print("Le collezioni 'schedeProdotti' e 'Recensioni' sono state create.");
