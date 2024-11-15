package com.prova.e_commerce.dbTS.controller.graphql;

import com.prova.e_commerce.dbTS.model.SalesMonitoring;
import com.prova.e_commerce.dbTS.service.SalesMonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Component
public class SalesMonitoringResolverGraphql {

    @Autowired
    private SalesMonitoringService salesMonitoringService;

    // Query per ottenere i monitoraggi delle vendite in un intervallo di tempo
    @QueryMapping
    public List<SalesMonitoring> findSalesByTimeRange(@Argument String startTime, @Argument String endTime) {
        Instant start = Instant.parse(startTime);
        Instant end = Instant.parse(endTime);
        return salesMonitoringService.findByTimeRange(start, end);
    }

    // Query per trovare i monitoraggi delle vendite di un prodotto specifico
    @QueryMapping
    public List<SalesMonitoring> findSalesByProduct(@Argument String prodotto) {
        return salesMonitoringService.findByProduct(prodotto);
    }

    // Query per trovare i monitoraggi delle vendite di un venditore specifico
    @QueryMapping
    public List<SalesMonitoring> findSalesByVendor(@Argument String venditore) {
        return salesMonitoringService.findByVendor(venditore);
    }

    // Query per trovare i monitoraggi delle vendite per una categoria di prodotto
    @QueryMapping
    public List<SalesMonitoring> findSalesByCategory(@Argument String categoriaProdotto) {
        return salesMonitoringService.findByCategory(categoriaProdotto);
    }

    // Query per ottenere il ricavo medio per prodotto
    @QueryMapping
    public List<Map.Entry<String, Double>> getAverageRevenueByProduct() {
        return salesMonitoringService.getAverageRevenueByProduct().entrySet().stream().toList();
    }

}
