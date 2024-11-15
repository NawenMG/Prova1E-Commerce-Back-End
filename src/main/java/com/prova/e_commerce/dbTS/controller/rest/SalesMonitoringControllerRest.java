package com.prova.e_commerce.dbTS.controller.rest;

import com.prova.e_commerce.dbTS.model.SalesMonitoring;
import com.prova.e_commerce.dbTS.service.SalesMonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sales-monitoring")
public class SalesMonitoringControllerRest {

    @Autowired
    private SalesMonitoringService salesMonitoringService;

    // Endpoint per inserire un singolo monitoraggio delle vendite
    @PostMapping("/add")
    public void addSalesMonitoring(@RequestBody SalesMonitoring salesMonitoring) {
        salesMonitoringService.insert(salesMonitoring);
    }

    // Endpoint per inserire un batch di monitoraggi delle vendite
    @PostMapping("/addBatch")
    public void addSalesMonitoringBatch(@RequestBody List<SalesMonitoring> salesMonitoringList) {
        salesMonitoringService.insertBatch(salesMonitoringList);
    }

    // Endpoint per aggiornare un monitoraggio delle vendite
    @PutMapping("/update")
    public void updateSalesMonitoring(@RequestBody SalesMonitoring oldSalesMonitoring, @RequestBody SalesMonitoring newSalesMonitoring) {
        salesMonitoringService.update(oldSalesMonitoring, newSalesMonitoring);
    }

    // Endpoint per eliminare i monitoraggi delle vendite di un venditore specifico
    @DeleteMapping("/delete/{venditore}")
    public void deleteByVendor(@PathVariable String venditore) {
        salesMonitoringService.deleteByTag(venditore);
    }

    // Endpoint per recuperare i monitoraggi delle vendite in un intervallo temporale
    @GetMapping("/byTimeRange")
    public List<SalesMonitoring> getByTimeRange(@RequestParam("start") Instant startTime, 
                                                @RequestParam("end") Instant endTime) {
        return salesMonitoringService.findByTimeRange(startTime, endTime);
    }

    // Endpoint per recuperare i monitoraggi delle vendite per un prodotto specifico
    @GetMapping("/byProduct/{prodotto}")
    public List<SalesMonitoring> getByProduct(@PathVariable String prodotto) {
        return salesMonitoringService.findByProduct(prodotto);
    }

    // Endpoint per recuperare i monitoraggi delle vendite di un venditore specifico
    @GetMapping("/byVendor/{venditore}")
    public List<SalesMonitoring> getByVendor(@PathVariable String venditore) {
        return salesMonitoringService.findByVendor(venditore);
    }

    // Endpoint per recuperare i monitoraggi delle vendite per una categoria di prodotto
    @GetMapping("/byCategory/{categoriaProdotto}")
    public List<SalesMonitoring> getByCategory(@PathVariable String categoriaProdotto) {
        return salesMonitoringService.findByCategory(categoriaProdotto);
    }

    // Endpoint per ottenere il ricavo medio per prodotto
    @GetMapping("/averageRevenueByProduct")
    public Map<String, Double> getAverageRevenueByProduct() {
        return salesMonitoringService.getAverageRevenueByProduct();
    }

    // Endpoint per ottenere una lista di SalesMonitoring generati casualmente
    @GetMapping("/generateSalesMonitoring")
    public List<SalesMonitoring> generateRandomSalesMonitoring(
            @RequestParam(value = "count", defaultValue = "5") int count) {
        return salesMonitoringService.generateRandomSalesMonitoringList(count);
    }
}
