package hr.java.covidportal.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Reprezentira osobu sa njenim parametrima(ime, prezime, starost, županija, bolest kojom je zaražena te polje
 * kontaktiranih s kojima je bila u kontaktu).
 */
public class Osoba {
    private String ime;
    private String prezime;
    private Integer starost;
    private Zupanija zupanija;
    private Bolest zarazenBolescu;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Osoba osoba = (Osoba) o;
        return Objects.equals(ime, osoba.ime) &&
                Objects.equals(prezime, osoba.prezime) &&
                Objects.equals(starost, osoba.starost) &&
                Objects.equals(zupanija, osoba.zupanija) &&
                Objects.equals(zarazenBolescu, osoba.zarazenBolescu) &&
                Objects.equals(kontaktiraneOsobe, osoba.kontaktiraneOsobe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ime, prezime, starost, zupanija, zarazenBolescu, kontaktiraneOsobe);
    }

    private List<Osoba> kontaktiraneOsobe;

    /**
     * Služi za dodjeljivanje parametara određenoj osobi.
     */
    public static class Builder{
        private String ime;
        private String prezime;
        private Integer starost;
        private Zupanija zupanija;
        private Bolest zarazenBolescu;
        private List<Osoba> kontaktiraneOsobe;

        public Builder withIme(String ime){
            this.ime = ime;
            return this;
        }

        public Builder withPrezime(String prezime){
            this.prezime = prezime;
            return this;
        }

        public Builder withStarost(Integer starost){
            this.starost = starost;
            return this;
        }

        public Builder withZupanija(Zupanija zupanija){
            this.zupanija = zupanija;
            return this;
        }

        public Builder withZarazenBolescu(Bolest zarazenBolescu){
            this.zarazenBolescu = zarazenBolescu;
            return this;
        }

        public Builder withKontaktiraneOsobe(List<Osoba> kontaktiraneOsobe){
            this.kontaktiraneOsobe = kontaktiraneOsobe;
            return this;
        }

        public Osoba build(){
            Osoba osoba = new Osoba();
            osoba.ime = this.ime;
            osoba.prezime = this.prezime;
            osoba.starost = this.starost;
            osoba.zupanija = this.zupanija;
            osoba.zarazenBolescu = this.zarazenBolescu;
            osoba.kontaktiraneOsobe = this.kontaktiraneOsobe;
            if(zarazenBolescu instanceof Virus virus) {
                for (int i = 0; i < kontaktiraneOsobe.size(); i++) {
                    virus.prelazakZarazeNaOsobu(kontaktiraneOsobe.get(i));
                }
            }
            return osoba;
        }
    }

    private Osoba(){

    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public Integer getStarost() {
        return starost;
    }

    public void setStarost(Integer starost) {
        this.starost = starost;
    }

    public Zupanija getZupanija() {
        return zupanija;
    }

    public void setZupanija(Zupanija zupanija) {
        this.zupanija = zupanija;
    }

    public Bolest getZarazenBolescu() {
        return zarazenBolescu;
    }

    public void setZarazenBolescu(Bolest zarazenBolescu) {
        this.zarazenBolescu = zarazenBolescu;
    }

    public List<Osoba> getKontaktiraneOsobe() {
        return kontaktiraneOsobe;
    }

    public void setKontaktiraneOsobe(List<Osoba> kontaktiraneOsobe) {
        this.kontaktiraneOsobe = kontaktiraneOsobe;
    }

    @Override
    public String toString() {
        return getIme() + " " + getPrezime();
    }
}
