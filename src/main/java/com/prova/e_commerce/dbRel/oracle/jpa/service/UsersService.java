package com.prova.e_commerce.dbRel.oracle.jpa.service;

import com.prova.e_commerce.dbRel.oracle.jpa.entity.Users;
import com.prova.e_commerce.dbRel.oracle.jpa.repository.classi.UsersRepImpJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

    @Autowired
    private UsersRepImpJPA usersRepImpJPA;

    // Seleziona tutti gli utenti
    public List<Users> getAllUsers() {
        return usersRepImpJPA.findAll();
    }

    // Trova un utente per ID
    public Optional<Users> getUserById(String id) {
        return usersRepImpJPA.findById(id);
    }

    // Trova utenti per nome
    public List<Users> getUsersByFirstName(String firstName) {
        return usersRepImpJPA.findByFirstName(firstName);
    }

    // Trova utenti per cognome
    public List<Users> getUsersByLastName(String lastName) {
        return usersRepImpJPA.findByLastName(lastName);
    }

    // Trova un utente per nome utente
    public Optional<Users> getUserByUsername(String username) {
        return usersRepImpJPA.findByUsername(username);
    }

    // Trova un utente per email
    public Optional<Users> getUserByEmail(String email) {
        return usersRepImpJPA.findByEmail(email);
    }

    // Inserisci un nuovo utente
    public void addUser(Users user) {
        usersRepImpJPA.insert(user);
    }

    // Aggiorna un utente esistente
    public void updateUser(Users user) {
        usersRepImpJPA.update(user);
    }

    // Elimina un utente per ID
    public void deleteUserById(String id) {
        usersRepImpJPA.deleteById(id);
    }
}
