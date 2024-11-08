-- Creazione della tabella 'ArchiviazioneOrdini'
CREATE TABLE IF NOT EXISTS ArchiviazioneOrdini (
    ID VARCHAR PRIMARY KEY,               
    User_ID VARCHAR,                      
    Order_date TIMESTAMP,                
    Status VARCHAR,                       
    Importo_totale DECIMAL,              
    Indirizzo_della_spedizione VARCHAR,   
    Lista_articoli_dell_ordine SET<VARCHAR>, 
    Consegna TIMESTAMP,                   
    Corriere VARCHAR                      
) WITH comment = 'Tabella per l\'archiviazione degli ordini';

-- Creazione degli indici (opzionali) per ottimizzare le ricerche
-- Indice per User_ID (per recuperare gli ordini di un utente specifico)
CREATE INDEX IF NOT EXISTS idx_user_id ON ArchiviazioneOrdini(User_ID);

-- Indice per lo Stato dell'ordine (per filtrare gli ordini per stato)
CREATE INDEX IF NOT EXISTS idx_status ON ArchiviazioneOrdini(Status);

-- Indice per la data dell'ordine (per recuperare gli ordini in base alla data)
CREATE INDEX IF NOT EXISTS idx_order_date ON ArchiviazioneOrdini(Order_date);

-- Creazione della tabella 'ArchiviazioneTransizioni'
CREATE TABLE IF NOT EXISTS ArchiviazioneTransizioni (
    ID VARCHAR PRIMARY KEY,               
    Order_ID VARCHAR,                      
    Transizione_date TIMESTAMP,            
    Importo_totale DECIMAL,                
    Metodo_di_pagamento VARCHAR,          
    Status VARCHAR                         
) WITH comment = 'Tabella per l\'archiviazione delle transizioni degli ordini';

-- Creazione degli indici per ottimizzare le ricerche
-- Indice per Order_ID (per recuperare tutte le transizioni di un ordine specifico)
CREATE INDEX IF NOT EXISTS idx_order_id ON ArchiviazioneTransizioni(Order_ID);

-- Indice per la data della transizione (per recuperare le transizioni in base alla data)
CREATE INDEX IF NOT EXISTS idx_transizione_date ON ArchiviazioneTransizioni(Transizione_date);

-- Indice per lo stato della transizione (per recuperare tutte le transizioni in un determinato stato)
CREATE INDEX IF NOT EXISTS idx_status ON ArchiviazioneTransizioni(Status);

-- Creazione della tabella 'ArchiviazioneSegnalazioni'
CREATE TABLE IF NOT EXISTS ArchiviazioneSegnalazioni (
    ID VARCHAR PRIMARY KEY,               
    Utente VARCHAR,                       
    Riferimento VARCHAR,                 
    Data TIMESTAMP,                       
    Titolo VARCHAR,                       
    Descrizione TEXT,                     
    File_multimediali VARCHAR             
) WITH comment = 'Tabella per l\'archiviazione delle segnalazioni';

-- Creazione degli indici per ottimizzare le ricerche
-- Indice per Utente (per recuperare tutte le segnalazioni fatte da un utente specifico)
CREATE INDEX IF NOT EXISTS idx_utente ON ArchiviazioneSegnalazioni(Utente);

-- Indice per Data (per recuperare le segnalazioni in base alla data)
CREATE INDEX IF NOT EXISTS idx_data ON ArchiviazioneSegnalazioni(Data);

-- Indice per Titolo (per recuperare le segnalazioni in base al titolo)
CREATE INDEX IF NOT EXISTS idx_titolo ON ArchiviazioneSegnalazioni(Titolo);

-- Log per confermare la creazione
PRINT 'Le tabelle ArchiviazioneOrdini, ArchiviazioneTransizioni e ArchiviazioneSegnalazioni sono state create con successo.';
