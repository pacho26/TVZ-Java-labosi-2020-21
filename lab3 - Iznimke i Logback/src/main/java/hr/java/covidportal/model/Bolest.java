package hr.java.covidportal.model;

/**
 * Reprezentira bolest, a nasljeđuje apstraktnu klasu <code>ImenovaniEntitet</code>.
 */
public class Bolest extends ImenovaniEntitet{

    private Simptom[] simptomi;

    /**
     * Prima naziv bolesti te polje simptome te bolesti.
     *
     * @param naziv naziv bolesti
     * @param simptomi polje simptoma te bolesti
     */
    public Bolest(String naziv, Simptom[] simptomi) {
        super(naziv);
        this.simptomi = simptomi;
    }

    public Simptom[] getSimptomi() {
        return simptomi;
    }

    public void setSimptomi(Simptom[] simptomi) {
        this.simptomi = simptomi;
    }
}
