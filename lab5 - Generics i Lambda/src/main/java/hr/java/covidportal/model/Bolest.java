package hr.java.covidportal.model;

import java.util.Objects;
import java.util.Set;

/**
 * Reprezentira bolest, a nasljeđuje apstraktnu klasu <code>ImenovaniEntitet</code>.
 */
public class Bolest extends ImenovaniEntitet{

    private Set<Simptom> simptomi;

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

    /**
     * Prima naziv bolesti te polje simptome te bolesti.
     *
     * @param naziv naziv bolesti
     * @param simptomi polje simptoma te bolesti
     */
    public Bolest(String naziv, Set<Simptom> simptomi) {
        super(naziv);
        this.simptomi = simptomi;
    }

    public Set<Simptom> getSimptomi() {
        return simptomi;
    }

    public void setSimptomi(Set<Simptom> simptomi) {
        this.simptomi = simptomi;
    }
}
