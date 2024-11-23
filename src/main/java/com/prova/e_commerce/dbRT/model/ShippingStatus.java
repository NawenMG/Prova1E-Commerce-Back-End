package com.prova.e_commerce.dbRT.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ShippingStatus {

    @NotBlank(message = "L'ID è obbligatorio")
    private String id; // ID (Obbligatorio)

    @NotBlank(message = "La data di consegna è obbligatoria")
    private String deliveryDate; // Data di consegna (Obbligatorio)

    @NotBlank(message = "Lo status è obbligatorio")
    private String status; // Status (Obbligatorio)

    @NotNull(message = "La locazione corrente è obbligatoria")
    @Valid
    private CurrentLocation currentLocation; // Locazione corrente (Obbligatorio)

    @Valid
    private List<@NotNull(message = "Gli elementi dello storico delle locazioni non possono essere null") HistoricalLocation> locationHistory; // Storico delle locazioni

    // Costruttore
    public ShippingStatus() {}

    // Getter and Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CurrentLocation getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(CurrentLocation currentLocation) {
        this.currentLocation = currentLocation;
    }

    public List<HistoricalLocation> getLocationHistory() {
        return locationHistory;
    }

    public void setLocationHistory(List<HistoricalLocation> locationHistory) {
        this.locationHistory = locationHistory;
    }

    // Classe interna per CurrentLocation
    public static class CurrentLocation {
        @NotBlank(message = "La posizione corrente è obbligatoria")
        private String position; // Posizione (Obbligatorio)

        @NotBlank(message = "Il timestamp corrente è obbligatorio")
        private String timestamp; // Timestamp (Obbligatorio)

        public CurrentLocation() {}

        // Getter and Setter
        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }

    // Classe interna per HistoricalLocation
    public static class HistoricalLocation {
        @NotBlank(message = "La posizione nello storico non può essere vuota")
        private String position; // Posizione

        @NotBlank(message = "Il timestamp nello storico non può essere vuoto")
        private String timestamp; // Timestamp

        public HistoricalLocation() {}

        // Getter and Setter
        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }
}
