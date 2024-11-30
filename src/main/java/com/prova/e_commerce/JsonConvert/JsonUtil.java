package com.prova.e_commerce.JsonConvert;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    // Creiamo un'istanza dell'ObjectMapper
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Metodo per convertire un oggetto in formato JSON (come stringa).
     *
     * @param obj l'oggetto da serializzare
     * @return la rappresentazione JSON come stringa
     */
    public static String convertToJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);  // Serializza l'oggetto in formato JSON
        } catch (Exception e) {
            throw new RuntimeException("Errore nella serializzazione dell'oggetto in JSON", e);
        }
    }
}
