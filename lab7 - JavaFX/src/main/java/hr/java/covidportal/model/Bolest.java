package main.java.hr.java.covidportal.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * Reprezentira bolest, a nasljeđuje apstraktnu klasu <code>ImenovaniEntitet</code>.
 */
public class Bolest extends ImenovaniEntitet implements Serializable {

    private Set<Simptom> simptomi;

    /**
     * Prima naziv bolesti te polje simptome te bolesti.
     *
     * @param naziv    naziv bolesti
     * @param simptomi polje simptoma te bolesti
     */
    public Bolest(String naziv, Long id, Set<Simptom> simptomi) {
        super(id, naziv);
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

    public Set<Simptom> getSimptomi() {
        return simptomi;
    }

    public void setSimptomi(Set<Simptom> simptomi) {
        this.simptomi = simptomi;
    }

    @Override
    public String toString() {
        return getNaziv();
    }
}
