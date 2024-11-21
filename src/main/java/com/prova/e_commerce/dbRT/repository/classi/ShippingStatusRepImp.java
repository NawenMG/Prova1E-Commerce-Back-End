package com.prova.e_commerce.dbRT.repository.classi;

import com.prova.e_commerce.dbRT.model.ShippingStatus;
import com.prova.e_commerce.dbRT.repository.interfacce.ShippingStatusRep;
import com.google.api.core.ApiFuture;
import com.google.firebase.database.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public class ShippingStatusRepImp implements ShippingStatusRep {

    @Autowired
    private DatabaseReference databaseReference;

    // ==============================
    // Operazioni sullo Stato della Spedizione
    // ==============================

    // Crea una nuova spedizione
    public ApiFuture<Void> createShippingStatus(ShippingStatus shippingStatus) {
        return databaseReference.child(shippingStatus.getId()).setValueAsync(shippingStatus);
    }

    // Modifica una spedizione esistente
    public ApiFuture<Void> updateShippingStatus(ShippingStatus shippingStatus) {
        return databaseReference.child(shippingStatus.getId()).setValueAsync(shippingStatus);
    }

    // Seleziona lo stato di spedizione per ID
    public CompletableFuture<ShippingStatus> selectShippingStatusById(String shippingStatusId) {
        CompletableFuture<ShippingStatus> future = new CompletableFuture<>();
        databaseReference.child(shippingStatusId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ShippingStatus shippingStatus = snapshot.getValue(ShippingStatus.class);
                future.complete(shippingStatus);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(new RuntimeException(error.getMessage()));
            }
        });
        return future;
    }

    // Elimina lo stato di spedizione
    public ApiFuture<Void> deleteShippingStatus(String shippingStatusId) {
        return databaseReference.child(shippingStatusId).removeValueAsync();
    }

    // ==============================
    // Operazioni sulle Locazioni
    // ==============================

    // Crea una nuova locazione per una spedizione
    public ApiFuture<Void> createLocationForShipping(String shippingStatusId, ShippingStatus.CurrentLocation location) {
        DatabaseReference locationsRef = databaseReference.child(shippingStatusId).child("locationHistory");
        String locationId = locationsRef.push().getKey(); // Genera ID univoco per la locazione
        return locationsRef.child(locationId).setValueAsync(location);
    }

    // Seleziona tutte le locazioni storiche di una spedizione
    public CompletableFuture<List<ShippingStatus.HistoricalLocation>> selectLocationsForShipping(String shippingStatusId) {
        CompletableFuture<List<ShippingStatus.HistoricalLocation>> future = new CompletableFuture<>();
        databaseReference.child(shippingStatusId).child("locationHistory").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<ShippingStatus.HistoricalLocation> locations = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    ShippingStatus.HistoricalLocation location = child.getValue(ShippingStatus.HistoricalLocation.class);
                    if (location != null) {
                        locations.add(location);
                    }
                }
                future.complete(locations);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(new RuntimeException(error.getMessage()));
            }
        });
        return future;
    }

    // Elimina una locazione storica da una spedizione
    public ApiFuture<Void> deleteLocationFromShipping(String shippingStatusId, String locationId) {
        DatabaseReference locationRef = databaseReference.child(shippingStatusId).child("locationHistory").child(locationId);
        return locationRef.removeValueAsync();
    }
}
