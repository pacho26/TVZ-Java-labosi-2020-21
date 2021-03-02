package main.java.hr.java.covidportal.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Reprezentira bolest, a nasljeđuje apstraktnu klasu <code>ImenovaniEntitet</code>. Implementira sučelje
 * <code>Serializable</code> ako želimo serijalizirati objekte klase te ih zapisati u datoteku u binarnom obliku.
 */
public class Bolest extends ImenovaniEntitet implements Serializable {

    private List<Simptom> simptomi;

    /**
     * Prima naziv bolesti te polje simptome te bolesti.
     *
     * @param id identifikacijski broj bolesti
     * @param naziv naziv bolesti
     * @param simptomi polje simptoma te bolesti
     */
    public Bolest(Long id, String naziv, List<Simptom> simptomi) {
        super(id, naziv);
        this.simptomi = simptomi;
    }

    public Bolest(String naziv, List<Simptom> simptomi) {
        super(naziv);
        this.simptomi = simptomi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bolest bolest = (Bolest) o;
        return Objects.equals(simptomi, bolest.simptomi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(simptomi);
    }

    public List<Simptom> getSimptomi() {
        return simptomi;
    }

    public void setSimptomi(List<Simptom> simptomi) {
        this.simptomi = simptomi;
    }

    @Override
    public String toString() {
        return getNaziv();
    }
}
