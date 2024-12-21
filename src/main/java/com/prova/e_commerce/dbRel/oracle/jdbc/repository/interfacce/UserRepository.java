package com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    // Trova un utente per nome utente
    Optional<User> findByNomeUtente(String nomeUtente);
    
    // Trova un utente per email
    Optional<User> findByEmail(String email);
    
    // Controlla se un nome utente esiste
    boolean existsByNomeUtente(String nomeUtente);
    
    // Controlla se un'email esiste
    boolean existsByEmail(String email);
}
