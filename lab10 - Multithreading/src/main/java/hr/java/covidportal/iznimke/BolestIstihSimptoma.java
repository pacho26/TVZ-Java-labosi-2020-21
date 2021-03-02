package main.java.hr.java.covidportal.iznimke;

import main.java.hr.java.covidportal.model.Bolest;

/**
 * Neoznačena iznimka koja se baca kada se unese bolest sa simptomima koji su već ranije uneseni.
 */
public class BolestIstihSimptoma extends RuntimeException{

    /**
     * Poruka koja se baca kada se dogodi greška.
     * @param poruka poruka koja se baca
     */
    public BolestIstihSimptoma(String poruka){
        super(poruka);
    }

    /**
     * Uzrok koji se baca kada se dogodi greška.
     * @param uzrok uzrok navedene greške
     */
    public BolestIstihSimptoma(Throwable uzrok){
        super(uzrok);
    }

    /**
     * Poruka i uzrok koji se bacaju kada se dogodi greška.
     * @param poruka poruka koja se baca
     * @param uzrok uzrok navedene greške
     */
    public BolestIstihSimptoma(String poruka, Throwable uzrok){
        super(poruka, uzrok);
    }
}
