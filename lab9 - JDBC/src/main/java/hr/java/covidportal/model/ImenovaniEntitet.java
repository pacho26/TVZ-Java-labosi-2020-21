package main.java.hr.java.covidportal.model;

import java.io.Serializable;

/**
 * Ostale klase ju nasljeđuju, jer se pomoću nje dodjeljuje <code>naziv</code> i <code>id</code>.
 */

public abstract class ImenovaniEntitet implements Serializable {
    private Long id;
    private String naziv;

    /**
     *
     * @param id    identifikacijski broj objekta
     * @param naziv String kojim imenujemo objekt
     */
    public ImenovaniEntitet(Long id, String naziv) {
        this.id = id;
        this.naziv = naziv;
    }

    public ImenovaniEntitet(String naziv) {
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
}
