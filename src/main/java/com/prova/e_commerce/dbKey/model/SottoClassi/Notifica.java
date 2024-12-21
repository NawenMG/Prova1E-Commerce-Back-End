package com.prova.e_commerce.dbKey.model.SottoClassi;

import javax.validation.constraints.NotNull;

public class Notifica {

    @NotNull(message = "La notifica via email è obbligatoria")
    private Boolean email;

    @NotNull(message = "La notifica via sms è obbligatoria")
    private Boolean sms;

    @NotNull(message = "Le notifiche sono obbligatorie")
    private Boolean notifiche;

    @NotNull(message = "Le notifiche locali sono obbligatorie")
    private Boolean locali;

    // Getter e setter per gli attributi
    public Boolean getEmail() {
        return email;
    }

    public void setEmail(Boolean email) {
        this.email = email;
    }

    public Boolean getSms() {
        return sms;
    }

    public void setSms(Boolean sms) {
        this.sms = sms;
    }

    public Boolean getNotifiche() {
        return notifiche;
    }

    public void setNotifiche(Boolean notifiche) {
        this.notifiche = notifiche;
    }

    public Boolean getLocali() {
        return locali;
    }

    public void setLocali(Boolean locali) {
        this.locali = locali;
    }
}
