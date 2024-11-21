package com.prova.e_commerce.dbRT.model;

import java.util.List;

public class ShippingStatus {

    private String id; // ID (Obbligatorio)
    private String deliveryDate; // Data di consegna (Obbligatorio)
    private String status; // Status (Obbligatorio)
    private CurrentLocation currentLocation; // Locazione corrente (Obbligatorio)
    private List<HistoricalLocation> locationHistory; // Storico delle locazioni

    public ShippingStatus() {
    }

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

    // Inner classes for current and historical locations
    public static class CurrentLocation {
        private String position; // Posizione (Obbligatorio)
        private String timestamp; // Timestamp (Obbligatorio)

        public CurrentLocation() {
        }

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

    public static class HistoricalLocation {
        private String position; // Posizione
        private String timestamp; // Timestamp

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
