package com.prova.e_commerce.dbRel.awsRds.jpa.repository.classi;

import com.prova.e_commerce.dbRel.awsRds.jpa.entity.Users;
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
