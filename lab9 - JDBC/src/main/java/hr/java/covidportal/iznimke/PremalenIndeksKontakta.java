package main.java.hr.java.covidportal.iznimke;

/**
 * Označena iznimka koja se baca kada je indeks odabranog kontakta manji od najmanjeg indeksa.
 */
public class PremalenIndeksKontakta extends Exception{

    /**
     * Poruka koja se baca kada se dogodi greška.
     * @param poruka poruka koja se baca
     */
    public PremalenIndeksKontakta(String poruka){
        super(poruka);
    }

    /**
     * Uzrok koji se baca kada se dogodi greška.
     * @param uzrok uzrok navedene greške
     */
    public PremalenIndeksKontakta(Throwable uzrok){
        super(uzrok);
    }

    /**
     * Poruka i uzrok koji se bacaju kada se dogodi greška.
     * @param poruka poruka koja se baca
     * @param uzrok uzrok navedene greške
     */
    public PremalenIndeksKontakta(String poruka, Throwable uzrok){
        super(poruka, uzrok);
    }
}
