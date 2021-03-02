package main.java.hr.java.covidportal.model;

import main.java.hr.java.covidportal.enumeracije.VrijednostiSimptoma;

import java.io.Serializable;
import java.util.Objects;

/**
 * Reprezentira simptom, a nasljeđuje apstraktnu klasu <code>ImenovaniEntitet</code>.
 */
public class Simptom extends ImenovaniEntitet implements Serializable {
    private VrijednostiSimptoma vrijednost;

    /**
     * Prima <code>naziv</code> simptoma te <code>vrijednost</code> tog simptoma.
     *
     * @param naziv naziv simptoma
     * @param vrijednost vrijednost simptoma
     */
    public Simptom(Long id, String naziv, VrijednostiSimptoma vrijednost) {
        super(id, naziv);
        this.vrijednost = vrijednost;
    }

    public VrijednostiSimptoma getVrijednost() {
        return vrijednost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Simptom simptom = (Simptom) o;
        return Objects.equals(vrijednost, simptom.vrijednost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vrijednost);
    }

    public void setVrijednost(VrijednostiSimptoma vrijednost) {
        this.vrijednost = vrijednost;
    }

    @Override
    public String toString() {
        return getNaziv();
    }
}