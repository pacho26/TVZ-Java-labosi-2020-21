package hr.java.covidportal.model;

/**
 * Reprezentira županiju, a nasljeđuje apstraktnu klasu <code>ImenovaniEntitet</code>.
 */
public class Zupanija extends ImenovaniEntitet {
    private Integer brojStanovnika;

    /**
     * Prima naziv županije te njen broj stanovnika koji žive u njoj.
     *
     * @param naziv naziv županije
     * @param brojStanovnika broj stanovnika te županije
     */
    public Zupanija(String naziv, Integer brojStanovnika) {
        super(naziv);
        this.brojStanovnika = brojStanovnika;
    }

    public Integer getBrojStanovnika() {
        return brojStanovnika;
    }

    public void setBrojStanovnika(Integer brojStanovnika) {
        this.brojStanovnika = brojStanovnika;
    }
}
