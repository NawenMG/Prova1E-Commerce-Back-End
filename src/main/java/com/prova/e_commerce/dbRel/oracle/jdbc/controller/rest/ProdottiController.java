package com.prova.e_commerce.dbRel.oracle.jdbc.controller.rest;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Prodotti;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.service.ProdottiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/prodotti")
public class ProdottiController {

    @Autowired
    private ProdottiService prodottiService;

    /**
     * Endpoint per eseguire una query avanzata sui prodotti in base a parametri dinamici.
     */
    @PostMapping("/query")
    public ResponseEntity<List<Prodotti>> queryProdotti(@RequestBody ParamQuery paramQuery, @RequestBody Prodotti prodotti) {
        List<Prodotti> prodottiList = prodottiService.queryProdotti(paramQuery, prodotti);
        return new ResponseEntity<>(prodottiList, HttpStatus.OK);
    }

    /**
     * Endpoint per inserire un nuovo prodotto.
     */
    @PostMapping
    public ResponseEntity<String> inserisciProdotto(@RequestBody Prodotti prodotto) {
        String response = prodottiService.inserisciProdotto(prodotto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Endpoint per aggiornare un prodotto esistente in base all'ID.
     */
    @PutMapping("/{productId}")
    public ResponseEntity<String> aggiornaProdotto(@PathVariable String productId, @RequestBody Prodotti prodotto) {
        String response = prodottiService.aggiornaProdotto(productId, prodotto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Endpoint per eliminare un prodotto in base all'ID.
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> eliminaProdotto(@PathVariable String productId) {
        String response = prodottiService.eliminaProdotto(productId);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    /**
     * Endpoint per generare un numero specificato di prodotti con dati casuali.
     */
    @PostMapping("/random/{numero}")
    public ResponseEntity<String> salvaProdottiCasuali(@PathVariable int numero) {
        String response = prodottiService.salvaProdottiCasuali(numero);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Endpoint per caricare un'immagine su S3 e associarla a un prodotto.
     */
    @PostMapping("/{productId}/immagine")
    public ResponseEntity<String> caricaImmagineProdotto(@PathVariable String productId, @RequestParam("file") MultipartFile file) {
        try {
            String response = prodottiService.caricaImmagineProdotto(productId, file);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Errore nel caricare l'immagine", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint per scaricare l'immagine di un prodotto da S3.
     */
    @GetMapping("/{productId}/immagine")
    public ResponseEntity<byte[]> scaricaImmagineProdotto(@PathVariable String productId) {
        try {
            InputStream immagine = prodottiService.scaricaImmagineProdotto(productId);
            byte[] imageBytes = immagine.readAllBytes();
            return ResponseEntity.ok().body(imageBytes);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint per eliminare l'immagine di un prodotto su S3.
     */
    @DeleteMapping("/{productId}/immagine")
    public ResponseEntity<String> eliminaImmagineProdotto(@PathVariable String productId) {
        try {
            prodottiService.eliminaImmagineProdotto(productId);
            return new ResponseEntity<>("Immagine eliminata con successo", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore nell'eliminazione dell'immagine", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
