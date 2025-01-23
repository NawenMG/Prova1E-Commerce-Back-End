// Creazione dei nodi

// Nodo Utente
CREATE CONSTRAINT IF NOT EXISTS ON (u:Utente) ASSERT u.id IS UNIQUE;

// Nodo Prodotto
CREATE CONSTRAINT IF NOT EXISTS ON (p:Prodotto) ASSERT p.id IS UNIQUE;

// Nodo Categoria Prodotto
CREATE CONSTRAINT IF NOT EXISTS ON (c:CategoriaProdotto) ASSERT c.nome IS UNIQUE;

// Nodo Locazione
CREATE CONSTRAINT IF NOT EXISTS ON (l:Locazione) ASSERT l.nome IS UNIQUE;

// Creazione delle relazioni (esempi generici)

// Relazione Visita del prodotto da parte dell'utente
MATCH (u:Utente {id: 1}), (p:Prodotto {id: 100})
CREATE (u)-[:VISITA]->(p);

// Relazione Acquisto del prodotto da parte dell'utente
MATCH (u:Utente {id: 1}), (p:Prodotto {id: 100})
CREATE (u)-[:ACQUISTA]->(p);

// Relazione Appartenenza a delle categorie da parte del prodotto
MATCH (p:Prodotto {id: 100}), (c:CategoriaProdotto {nome: 'Elettronica'})
CREATE (p)-[:APPARTIENE_A]->(c);

// Relazione Provenienza geografica dell'utente
MATCH (u:Utente {id: 1}), (l:Locazione {nome: 'Roma'})
CREATE (u)-[:PROVIENE_DA]->(l);

// Conferma della creazione delle entità e relazioni
RETURN "Migrazione completata con successo!";
