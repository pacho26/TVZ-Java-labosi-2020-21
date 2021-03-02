package hr.java.covidportal.model;

/**
 * Reprezentira simptom, a nasljeđuje apstraktnu klasu <code>ImenovaniEntitet</code>.
 */
public class Simptom extends ImenovaniEntitet{
    private String vrijednost;

    /**
     * Prima <code>naziv</code> simptoma te <code>vrijednost</code> tog simptoma.
     *
     * @param naziv naziv simptoma
     * @param vrijednost vrijednost simptoma
     */
    public Simptom(String naziv, String vrijednost) {
        super(naziv);
        this.vrijednost = vrijednost;
    }

    public String getVrijednost() {
        return vrijednost;
    }

    public void setVrijednost(String vrijednost) {
        this.vrijednost = vrijednost;
    }
}