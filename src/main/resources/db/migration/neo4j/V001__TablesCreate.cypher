// Creazione dei nodi

// Nodo Utente
CREATE CONSTRAINT IF NOT EXISTS ON (u:Utente) ASSERT u.id IS UNIQUE;

// Nodo Prodotto
CREATE CONSTRAINT IF NOT EXISTS ON (p:Prodotto) ASSERT p.id IS UNIQUE;

// Nodo Categoria Prodotto
CREATE CONSTRAINT IF NOT EXISTS ON (c:CategoriaProdotto) ASSERT c.nome IS UNIQUE;

// Nodo Locazione Utente
CREATE CONSTRAINT IF NOT EXISTS ON (l:LocazioneUtente) ASSERT l.id IS UNIQUE;

// Creazione delle relazioni

// Relazione Visita del prodotto da parte dell'utente
MATCH (u:Utente), (p:Prodotto)
WHERE u.id = {utenteId} AND p.id = {prodottoId}
CREATE (u)-[:VISITA]->(p);

// Relazione Acquisto del prodotto
MATCH (u:Utente), (p:Prodotto)
WHERE u.id = {utenteId} AND p.id = {prodottoId}
CREATE (u)-[:ACQUISTA]->(p);

// Relazione Appartenenza a delle categorie da parte del prodotto
MATCH (p:Prodotto), (c:CategoriaProdotto)
WHERE p.id = {prodottoId} AND c.nome = {categoriaNome}
CREATE (p)-[:APPARTIENE_A]->(c);

// Relazione Provenienza geografica dell'utente che ha acquistato il prodotto
MATCH (u:Utente), (p:Prodotto)
WHERE u.id = {utenteId} AND p.id = {prodottoId}
CREATE (u)-[:PROVIENE_DA]->(p);

// Conferma della creazione delle entità e relazioni
RETURN "Gestione Raccomandazioni Prodotti creata con successo!";
