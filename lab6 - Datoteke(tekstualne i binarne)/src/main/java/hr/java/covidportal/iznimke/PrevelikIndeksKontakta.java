package hr.java.covidportal.iznimke;

/**
 * Označena iznimka koja se baca kada je indeks odabranog kontakta veći od najvećeg indeksa.
 */
public class PrevelikIndeksKontakta extends Exception{
    /**
     * Poruka koja se baca kada se dogodi greška.
     * @param poruka poruka koja se baca
     */
    public PrevelikIndeksKontakta(String poruka){
        super(poruka);
    }

    /**
     * Uzrok koji se baca kada se dogodi greška.
     * @param uzrok uzrok navedene greške
     */
    public PrevelikIndeksKontakta(Throwable uzrok){
        super(uzrok);
    }

    /**
     * Poruka i uzrok koji se bacaju kada se dogodi greška.
     * @param poruka poruka koja se baca
     * @param uzrok uzrok navedene greške
     */
    public PrevelikIndeksKontakta(String poruka, Throwable uzrok){
        super(poruka, uzrok);
    }
}
