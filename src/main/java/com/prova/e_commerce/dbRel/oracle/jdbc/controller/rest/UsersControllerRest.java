package com.prova.e_commerce.dbRel.oracle.jdbc.controller.rest;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Users;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/rest")
public class UsersControllerRest {
    @Autowired
    private UsersService usersService;

    /**
     * Endpoint per eseguire una query avanzata sugli utenti in base a parametri dinamici.
     */
    @GetMapping("/query")
    public ResponseEntity<List<Users>> queryUsers(@RequestBody ParamQuery paramQuery, @RequestBody Users users) {
        List<Users> usersList = usersService.queryUsers(paramQuery, users);
        return new ResponseEntity<>(usersList, HttpStatus.OK);
    }

    /**
     * Endpoint per inserire un nuovo utente nel database.
     */
    @PostMapping("/inserisci")
    public ResponseEntity<String> inserisciUtente(@RequestBody Users users) {
        String response = usersService.inserisciUtente(users);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Endpoint per aggiornare un utente esistente.
     */
    @PutMapping("/aggiorna/{userID}")
    public ResponseEntity<String> aggiornaUtente(@PathVariable int userID, @RequestBody Users users) {
        String response = usersService.aggiornaUtente(userID, users);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Endpoint per eliminare un utente in base all'ID.
     */
    @DeleteMapping("/elimina/{userID}")
    public ResponseEntity<String> eliminaUtente(@PathVariable int userID) {
        String response = usersService.eliminaUtente(userID);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    /**
     * Endpoint per generare un numero specificato di utenti casuali e salvarli nel database.
     */
    @PostMapping("/salva-casuali/{numero}")
    public ResponseEntity<String> salvaUtentiCasuali(@PathVariable int numero) {
        String response = usersService.salvaUtentiCasuali(numero);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
