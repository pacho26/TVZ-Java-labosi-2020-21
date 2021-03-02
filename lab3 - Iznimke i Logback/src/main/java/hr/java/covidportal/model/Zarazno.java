package hr.java.covidportal.model;

/**
 * Sadrži metodu <code>prelazakZarazeNaOsobu</code> preko koje osoba s određenim virusom zarazi sve osobe s kojima je
 * bila u kontaktu.
 */
public interface Zarazno {
    void prelazakZarazeNaOsobu(Osoba osoba);
}
