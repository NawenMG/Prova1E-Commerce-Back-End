package com.prova.e_commerce.dbRel.oracle.jdbc.model;

import java.math.BigDecimal;
import java.time.LocalDate;
/* import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size; */

public class Pagamenti {

   /*  @NotBlank(message = "Obbligatorio")
    @Size(max = 50, message = "L'ID del pagamento non può superare i 50 caratteri") */
    private String paymentsID;  // chiave primaria (modificato in String per corrispondere a VARCHAR2(50))

    /* @NotBlank(message = "Obbligatorio") */
    private String type;     // tipo di pagamento

    /* @NotNull(message = "Obbligatorio") */
    private LocalDate data;  // data del pagamento

    private Boolean status;   // stato del pagamento (Boolean per permettere null)

    /* @NotNull(message = "Obbligatorio") */
    private BigDecimal total;     // totale del pagamento (BigDecimal per precisione decimale)

    // Costruttore
    public Pagamenti() {
    }

    // Getters e Setters
    public String getPaymentsID() {
        return paymentsID;
    }

    public void setPaymentsID(String paymentsID) {
        this.paymentsID = paymentsID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
