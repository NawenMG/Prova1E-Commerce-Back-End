package com.prova.e_commerce.dbRT.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.List;

public class ShippingStatus {

    @NotBlank(message = "L'ID è obbligatorio")
    private String id; // ID (Obbligatorio)

    @NotNull(message = "La data di consegna è obbligatoria")
    private LocalDate deliveryDate; // Data di consegna (Obbligatorio) - Usato LocalDate

    private String deliveryDateStr; // Aggiunto per la conversione da LocalDate a String (per Firebase)

    @NotBlank(message = "Lo status è obbligatorio")
    private String status; // Status (Obbligatorio)

    @NotNull(message = "La locazione corrente è obbligatoria")
    @Valid
    private CurrentLocation currentLocation; // Locazione corrente (Obbligatorio)

    @Valid
    private List<HistoricalLocation> locationHistory; // Storico delle locazioni - Non obbligatorio

    // Costruttore
    public ShippingStatus() {}

    // Getter and Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    // Aggiunto getter e setter per il campo deliveryDateStr
    public String getDeliveryDateStr() {
        return deliveryDateStr;
    }

    public void setDeliveryDateStr(String deliveryDateStr) {
        this.deliveryDateStr = deliveryDateStr;
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
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}", message = "Il timestamp deve essere nel formato YYYY-MM-DDTHH:MM:SS")
        private String timestamp; // Timestamp (Obbligatorio) - Validazione con formato di data e ora

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
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}", message = "Il timestamp deve essere nel formato YYYY-MM-DDTHH:MM:SS")
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
