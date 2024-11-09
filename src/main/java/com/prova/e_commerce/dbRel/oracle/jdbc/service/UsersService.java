package com.prova.e_commerce.dbRel.oracle.jdbc.service;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Users;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce.UsersRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {
    @Autowired
    private UsersRep usersRep;

    /**
     * Esegue una query personalizzata sui dati degli utenti in base ai parametri specificati.
     */
    public List<Users> queryUsers(ParamQuery paramQuery, Users users) {
        return usersRep.query(paramQuery, users);
    }

    /**
     * Inserisce un nuovo utente nel database.
     */
    public String inserisciUtente(Users users) {
        return usersRep.insertUser(users);
    }

    /**
     * Aggiorna i dati di un utente specifico identificato dall'ID.
     */
    public String aggiornaUtente(int userID, Users users) {
        return usersRep.updateUser(userID, users);
    }

    /**
     * Elimina un utente specifico dal database in base all'ID.
     */
    public String eliminaUtente(int userID) {
        return usersRep.deleteUser(userID);
    }

    /**
     * Genera un numero specificato di utenti con dati casuali e li salva nel database.
     */
    public String salvaUtentiCasuali(int numero) {
        return usersRep.saveAll(numero);
    }
}
