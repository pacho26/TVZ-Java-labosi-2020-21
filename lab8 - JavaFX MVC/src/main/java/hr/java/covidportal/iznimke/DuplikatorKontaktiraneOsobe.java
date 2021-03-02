package main.java.hr.java.covidportal.iznimke;

/**
 * Označena iznimka koja se izbacuje kada se kao kontakt unese osoba koja je već ranije dodana u kontaktirane osobe.
 */
public class DuplikatorKontaktiraneOsobe extends Exception{

    /**
     * Poruka koja se baca kada se dogodi greška.
     * @param poruka poruka koja se baca
     */
    public DuplikatorKontaktiraneOsobe(String poruka){
        super(poruka);
    }

    /**
     * Uzrok koji se baca kada se dogodi greška.
     * @param uzrok uzrok navedene greške
     */
    public DuplikatorKontaktiraneOsobe(Throwable uzrok){
        super(uzrok);
    }

    /**
     * Poruka i uzrok koji se bacaju kada se dogodi greška.
     * @param poruka poruka koja se baca
     * @param uzrok uzrok navedene greške
     */
    public DuplikatorKontaktiraneOsobe(String poruka, Throwable uzrok){
        super(poruka, uzrok);
    }
}