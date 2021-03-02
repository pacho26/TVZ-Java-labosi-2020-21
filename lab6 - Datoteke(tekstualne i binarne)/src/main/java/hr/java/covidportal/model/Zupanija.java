package hr.java.covidportal.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Reprezentira županiju, a nasljeđuje apstraktnu klasu <code>ImenovaniEntitet</code>.
 */
public class Zupanija extends ImenovaniEntitet implements Serializable {
    private Integer brojStanovnika;
    private Integer brojZarazenih;

    /**
     * Prima naziv županije te njen broj stanovnika koji žive u njoj.
     *
     * @param naziv naziv županije
     * @param id ID županije
     * @param brojStanovnika broj stanovnika te županije
     * @param brojZarazenih broj zaraženih u određenoj županiji
     */
    public Zupanija(String naziv, Long id, Integer brojStanovnika, Integer brojZarazenih) {
        super(id, naziv);
        this.brojStanovnika = brojStanovnika;
        this.brojZarazenih = brojZarazenih;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zupanija zupanija = (Zupanija) o;
        return Objects.equals(brojStanovnika, zupanija.brojStanovnika);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brojStanovnika);
    }

    public Integer getBrojStanovnika() {
        return brojStanovnika;
    }

    public void setBrojStanovnika(Integer brojStanovnika) {
        this.brojStanovnika = brojStanovnika;
    }

    public Integer getBrojZarazenih() {
        return brojZarazenih;
    }

    public void setBrojZarazenih(Integer brojZarazenih) {
        this.brojZarazenih = brojZarazenih;
    }

    @Override
    public String toString() {
        return "Zupanija{" +
                "brojStanovnika=" + brojStanovnika +
                ", brojZarazenih=" + brojZarazenih +
                '}';
    }
}
