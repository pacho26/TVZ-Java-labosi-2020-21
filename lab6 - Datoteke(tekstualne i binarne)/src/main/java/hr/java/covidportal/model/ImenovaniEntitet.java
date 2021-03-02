package hr.java.covidportal.model;

import java.io.Serializable;

/**
 * Ostale klase ju nasljeđuju, jer se pomoću nje dodjeljuje <code>naziv</code>.
 */
public abstract class ImenovaniEntitet implements Serializable {
    private Long id;
    private String naziv;

    /**
     * Prima String.
     *
     * @param naziv String kojim imenujemo <code>naziv</code>
     */
    public ImenovaniEntitet(Long id, String naziv) {
        this.id = id;
        this.naziv = naziv;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
