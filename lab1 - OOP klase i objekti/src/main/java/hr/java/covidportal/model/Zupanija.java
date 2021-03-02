package hr.java.covidportal.model;

public class Zupanija {
    private String naziv;
    private Integer brojStanovnika;
    private String skraceni_naziv;

    public Zupanija(String naziv, Integer brojStanovnika, String skraceni_naziv) {
        this.naziv = naziv;
        this.brojStanovnika = brojStanovnika;
        this.skraceni_naziv = skraceni_naziv;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Integer getBrojStanovnika() {
        return brojStanovnika;
    }

    public void setBrojStanovnika(Integer brojStanovnika) {
        this.brojStanovnika = brojStanovnika;
    }

    public String getSkraceni_naziv() {
        return skraceni_naziv;
    }

    public void setSkraceni_naziv(String skraceni_naziv) {
        this.skraceni_naziv = skraceni_naziv;
    }
}
