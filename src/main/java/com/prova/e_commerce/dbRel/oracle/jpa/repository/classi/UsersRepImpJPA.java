package com.prova.e_commerce.dbRel.oracle.jpa.repository.classi;

/*import com.prova.e_commerce.dbRel.oracle.jpa.entity.Users;
import com.prova.e_commerce.dbRel.oracle.jpa.repository.interfacce.UsersRepJPA;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Predicate;


//import com.prova.e_commerce.dbRel.oracle.jpa.randomData.UsersFakerJPA;
import com.prova.e_commerce.dbRel.oracle.jpa.parametri.ParamQueryJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

 @Repository
public class UsersRepImpJPA {

    @Autowired
    private UsersRepJPA usersRepository;

     // Per implementare il faker
    public String saveAll(int number) {
        UsersFakerJPA usersFaker = new UsersFakerJPA();
        
        for (int i = 0; i < number; i++) {
            // Genera un utente fittizio
            Users user = usersFaker.generateFakeUser(number);
            
            // Salva l'utente nel database
            usersRepository.save(user);
        }
        
        return "Dati generati con successo";
    } 

    // Query - Recupero degli utenti con parametri dinamici
    @PersistenceContext
    private EntityManager em;

    public List<Users> query(ParamQueryJPA paramQuery, Users users) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);
        Root<Users> root = cq.from(Users.class);

        List<Predicate> predicates = new ArrayList<>();

        // 1. Condizione WHERE principale
        paramQuery.getCondizioneWhere().ifPresent(cond -> {
            predicates.add(cb.equal(root.get("column"), cond));  // Esempio di filtro per una colonna (sostituire "column" con il campo corretto)
        });

        // 2. Condizione LIKE, se presente
        paramQuery.getLike().ifPresent(likeCondition -> {
            predicates.add(cb.like(root.get("column"), "%" + likeCondition + "%"));  // Filtro LIKE (sostituire "column" con il campo corretto)
        });

        // 3. Condizione IN, se presente
        paramQuery.getIn().ifPresent(inValues -> {
            String[] valuesArray = inValues.split(",");
            predicates.add(root.get("column").in((Object[]) valuesArray));  // Filtro IN (sostituire "column" con il campo corretto)
        });

        // 4. Condizione BETWEEN, se presente
        paramQuery.getBetween().ifPresent(betweenValues -> {
            predicates.add(cb.between(root.get("column"), betweenValues[0], betweenValues[1]));  // Filtro BETWEEN (sostituire "column" con il campo corretto)
        });

        // 5. Condizione HAVING (se presente) - opzionale, solo per aggregazioni
        paramQuery.getHaving().ifPresent(havingCondition -> {
            // Logica per la condizione HAVING
        });

        // 6. Applicare tutte le condizioni (predicati)
        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        // 7. Gestire il tipo di aggregazione (es. COUNT, SUM, AVG, ecc.)
        if (paramQuery.getAggregationType() != ParamQueryJPA.AggregationType.NONE) {
            @SuppressWarnings("unchecked")
            Expression<Double> aggregationExpr = (Expression<Double>) getAggregationExpression(cb, root, paramQuery.getAggregationType());
            cq.multiselect(aggregationExpr);
        } else {
            cq.select(root);  // Se non c'è aggregazione, seleziona tutti i campi dell'entità
        }

        // 8. Ordinamento (se specificato)
        if (paramQuery.isOrderBy()) {
            // Ordina i risultati (puoi aggiungere ordini dinamici basati su parametri)
            cq.orderBy(cb.asc(root.get("column")));  // Modifica "column" con il campo desiderato
        }

        // 9. Gestire TOP (limite dei risultati)
        if (paramQuery.getTop() != null) {
            TypedQuery<Users> query = em.createQuery(cq);
            query.setMaxResults(paramQuery.getTop());
            return query.getResultList();
        }

        // 10. Esegui la query
        TypedQuery<Users> query = em.createQuery(cq);
        return query.getResultList();
    }

    private Expression<?> getAggregationExpression(CriteriaBuilder cb, Root<Users> root, ParamQueryJPA.AggregationType aggregationType) {
        switch (aggregationType) {
            case COUNT:
                return cb.count(root);  // COUNT restituisce un Expression<Long>
            case SUM:
                // Assumiamo che "column" sia un campo di tipo Double
                return cb.sum(root.get("column"));  // Restituisce Expression<Double> se il campo è Double
            case AVG:
                // Assumiamo che "column" sia un campo di tipo Double
                return cb.avg(root.get("column"));  // Restituisce Expression<Double> se il campo è Double
            case MIN:
                // Assumiamo che "column" sia un campo di tipo Double
                return cb.min(root.get("column"));  // Restituisce Expression<Double> se il campo è Double
            case MAX:
                // Assumiamo che "column" sia un campo di tipo Double
                return cb.max(root.get("column"));  // Restituisce Expression<Double> se il campo è Double
            default:
                throw new UnsupportedOperationException("Unsupported aggregation type");
        }
    }


    // Insert - Aggiungi un nuovo utente
    public String insertUser(Users users) {
        // Salva l'utente nel database tramite JPA
        usersRepository.save(users);
        return "Dati inseriti con successo";
    }

    // Update - Modifica i dati di un utente esistente
    public String updateUser(String userID, Users users) {
        Optional<Users> existingUser = usersRepository.findById(userID);

        if (existingUser.isPresent()) {
            Users updatedUser = existingUser.get();
            updatedUser.setNome(users.getNome());
            updatedUser.setCognome(users.getCognome());
            updatedUser.setRuolo(users.getRuolo());
            updatedUser.setNomeUtente(users.getNomeUtente());
            updatedUser.setEmail(users.getEmail());
            updatedUser.setPassword(users.getPassword());
            updatedUser.setImmagine(users.getImmagine());

            usersRepository.save(updatedUser);
            return "Dati aggiornati con successo";
        } else {
            return "Utente non trovato";
        }
    }

    // Delete - Elimina un utente
    public String deleteUser(String userID) {
        Optional<Users> user = usersRepository.findById(userID);

        if (user.isPresent()) {
            usersRepository.delete(user.get());
            return "Dati eliminati con successo";
        } else {
            return "Utente non trovato";
        }
    }
} */


import com.prova.e_commerce.dbRel.oracle.jpa.entity.Users;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UsersRepImpJPA {

    @Autowired
    private SessionFactory sessionFactory;

    // Seleziona tutto
    public List<Users> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Users", Users.class).list();
        }
    }

    // Scegli un utente in base all'ID
    public Optional<Users> findById(String id) {
        try (Session session = sessionFactory.openSession()) {
            Users user = session.get(Users.class, id);
            return Optional.ofNullable(user);
        }
    }

    // Scegli un utente in base al nome
    public List<Users> findByFirstName(String firstName) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Users WHERE firstName = :firstName", Users.class)
                    .setParameter("firstName", firstName)
                    .list();
        }
    }

    // Scegli un utente in base al cognome
    public List<Users> findByLastName(String lastName) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Users WHERE lastName = :lastName", Users.class)
                    .setParameter("lastName", lastName)
                    .list();
        }
    }

    // Scegli un utente in base al nome utente
    public Optional<Users> findByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Users WHERE username = :username", Users.class)
                    .setParameter("username", username)
                    .uniqueResultOptional();
        }
    }

    // Scegli un utente in base all'email
    public Optional<Users> findByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Users WHERE email = :email", Users.class)
                    .setParameter("email", email)
                    .uniqueResultOptional();
        }
    }

    // Metodo per inserire un utente
    public void insert(Users user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Metodo per aggiornare un utente
    public void update(Users user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Metodo per eliminare un utente per ID
    public void deleteById(String id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Users user = session.get(Users.class, id);
            if (user != null) {
                session.remove(user);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}

