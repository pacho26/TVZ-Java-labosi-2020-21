package hr.java.covidportal.model;

/**
 * Ostale klase ju nasljeđuju, jer se pomoću nje dodjeljuje <code>naziv</code>.
 */
public abstract class ImenovaniEntitet {
    private String naziv;

    /**
     * Prima String.
     *
     * @param naziv String kojim imenujemo <code>naziv</code>
     */
    public ImenovaniEntitet(String naziv) {
        this.naziv = naziv;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
}
