package com.prova.e_commerce.dbTS.controller.rest;

import com.prova.e_commerce.dbTS.model.SalesMonitoring;
import com.prova.e_commerce.dbTS.service.SalesMonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // Endpoint per ottenere una lista di SalesMonitoring generati casualmente
    @GetMapping("/generateSalesMonitoring")
    public List<SalesMonitoring> generateRandomSalesMonitoring(
            @RequestParam(value = "count", defaultValue = "5") int count) {
        return salesMonitoringService.generateRandomSalesMonitoringList(count);
    }
}
