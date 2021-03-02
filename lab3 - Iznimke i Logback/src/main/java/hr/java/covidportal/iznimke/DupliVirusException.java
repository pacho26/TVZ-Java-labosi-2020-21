package hr.java.covidportal.iznimke;
// 2.zadatak
/**
 * Neoznačena iznimka koja se baca u slučaju kada je po drugi puta unesen neki naziv virusa.
 */
public class DupliVirusException extends RuntimeException{
    /**
     * Poruka koja se baca kada se dogodi greška.
     * @param poruka poruka koja se baca
     */
    public DupliVirusException(String poruka){
        super(poruka);
    }

    /**
     * Uzrok koji se baca kada se dogodi greška.
     * @param uzrok uzrok navedene greške
     */
    public DupliVirusException(Throwable uzrok){
        super(uzrok);
    }

    /**
     * Poruka i uzrok koji se bacaju kada se dogodi greška.
     * @param poruka poruka koja se baca
     * @param uzrok uzrok navedene greške
     */
    public DupliVirusException(String poruka, Throwable uzrok){
        super(poruka, uzrok);
    }
}
