package hr.java.covidportal.model;

import java.io.Serializable;
import java.util.Set;

/**
 * Reprezentira virus. Nasljeđuje klasu <code>Bolest</code> te implementira sučelje <code>Zarazno</code>.
 */
public class Virus extends Bolest implements Zarazno, Serializable {

    /**
     * Prima naziv bolesti te polje simptome te bolesti koje nasljeđuje iz klase <code>Bolest</code>
     *
     * @param naziv naziv virusa
     * @param simptomi polje simptoma koji idu uz taj virus
     */
    public Virus(Long id, String naziv, Set<Simptom> simptomi) {
        super(naziv, id, simptomi);
    }

    /**
     * Služi da određenu osobu koja je bila u kontaktu s određenom osobom zarazi tim virusom.
     *
     * @param osoba osoba na koju prelazi virus
     */
    @Override
    public void prelazakZarazeNaOsobu(Osoba osoba) {
        osoba.setZarazenBolescu(this);
    }

    @Override
    public String toString() {
        return getNaziv() + " (ID: " + getId() + ")";
    }
}