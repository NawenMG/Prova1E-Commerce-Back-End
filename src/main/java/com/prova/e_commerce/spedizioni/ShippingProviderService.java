package com.prova.e_commerce.spedizioni;

import com.prova.e_commerce.dbRT.model.ShippingStatus;
import java.util.concurrent.CompletableFuture;

public interface ShippingProviderService {
    CompletableFuture<String> createShippingLabel(ShippingStatus shippingStatus);
    CompletableFuture<String> trackShipment(String trackingNumber);
}
