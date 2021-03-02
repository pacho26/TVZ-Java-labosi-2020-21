package hr.java.covidportal.iznimke;
// 1.zadatak
/**
 * Označena iznimka koja se baca u slučaju kada <code>naziv</code> virusa sadrži više od 15 znakova.
 */
public class NeispravanNazivException extends Exception{
    /**
     * Poruka koja se baca kada se dogodi greška.
     * @param poruka poruka koja se baca
     */
    public NeispravanNazivException(String poruka){
        super(poruka);
    }

    /**
     * Uzrok koji se baca kada se dogodi greška.
     * @param uzrok uzrok navedene greške
     */
    public NeispravanNazivException(Throwable uzrok){
        super(uzrok);
    }

    /**
     * Poruka i uzrok koji se bacaju kada se dogodi greška.
     * @param poruka poruka koja se baca
     * @param uzrok uzrok navedene greške
     */
    public NeispravanNazivException(String poruka, Throwable uzrok){
        super(poruka, uzrok);
    }
}
