package com.prova.e_commerce.dbRel.oracle.jpa.service;

import com.prova.e_commerce.dbRel.oracle.jpa.entity.Users;
import com.prova.e_commerce.dbRel.oracle.jpa.repository.classi.UsersRepImpJPA;
import com.prova.e_commerce.storage.S3Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

    @Autowired
    private UsersRepImpJPA usersRepImpJPA;

    @Autowired
    private S3Service s3Service;

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

    // Carica l'immagine di un utente su S3
    public String uploadUserImage(String userId, MultipartFile file) throws IOException {
        // Verifica se l'utente esiste
        Optional<Users> userOpt = usersRepImpJPA.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        Users user = userOpt.get();

        // Carica il file su S3 e ottieni l'URL
        String imageUrl = s3Service.uploadFile("users/images", file); // 'users/images' è la cartella nel bucket S3

        // Aggiorna l'utente con l'URL dell'immagine
        user.setImmagine(imageUrl.getBytes());
        usersRepImpJPA.update(user);  // Salva l'utente con il nuovo URL dell'immagine

        return imageUrl;  // Restituisce l'URL dell'immagine caricata
    }

    // Scarica l'immagine di un utente da S3
    public InputStream downloadUserImage(String userId) throws IOException {
        // Verifica se l'utente esiste
        Optional<Users> userOpt = usersRepImpJPA.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        Users user = userOpt.get();
        String imageUrl = new String(user.getImmagine());  // Ottiene l'URL dell'immagine

        // Scarica il file da S3 utilizzando l'URL
        return s3Service.downloadFile(imageUrl);
    }

    // Elimina l'immagine di un utente da S3
    public void deleteUserImage(String userId) {
        // Verifica se l'utente esiste
        Optional<Users> userOpt = usersRepImpJPA.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        Users user = userOpt.get();
        String imageUrl = new String(user.getImmagine());  // Ottiene l'URL dell'immagine

        // Elimina il file da S3
        s3Service.deleteFile(imageUrl);

        // Rimuovi l'URL dell'immagine dall'utente
        user.setImmagine(null);
        usersRepImpJPA.update(user);  // Salva l'utente senza l'URL dell'immagine
    }
}
