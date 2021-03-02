package main.java.hr.java.covidportal.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;

/**
 * Reprezentira osobu sa njenim parametrima(ime, prezime, datum rođenja, županija, bolest kojom je zaražena te polje
 * kontaktiranih s kojima je bila u kontaktu). Implementira sučelje <code>Serializable</code> ako želimo serijalizirati
 * objekte klase te ih zapisati u datoteku u binarnom obliku.
 */
public class Osoba implements Serializable {
    private Long id;
    private String ime;
    private String prezime;
    private Zupanija zupanija;
    private Bolest zarazenBolescu;
    private List<Osoba> kontaktiraneOsobe;
    private LocalDate datumRodjenja;
    private Integer starost;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Osoba osoba = (Osoba) o;
        return Objects.equals(id, osoba.id) &&
                Objects.equals(ime, osoba.ime) &&
                Objects.equals(prezime, osoba.prezime) &&
                Objects.equals(datumRodjenja, osoba.datumRodjenja) &&
                Objects.equals(zupanija, osoba.zupanija) &&
                Objects.equals(zarazenBolescu, osoba.zarazenBolescu) &&
                Objects.equals(kontaktiraneOsobe, osoba.kontaktiraneOsobe) &&
                Objects.equals(starost, osoba.starost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ime, prezime, zupanija, zarazenBolescu, kontaktiraneOsobe, datumRodjenja, starost);
    }


    /**
     * Služi za dodjeljivanje parametara određenoj osobi.
     */
    public static class Builder{
        private Long id;
        private String ime;
        private String prezime;
        private Zupanija zupanija;
        private Bolest zarazenBolescu;
        private List<Osoba> kontaktiraneOsobe;
        private LocalDate datumRodjenja;
        private Integer starost;


        public Builder withId(Long id){
            this.id = id;
            return this;
        }

        public Builder withIme(String ime){
            this.ime = ime;
            return this;
        }

        public Builder withPrezime(String prezime){
            this.prezime = prezime;
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

        public Builder withDatumRodjenja(LocalDate datumRodjenja) {
            this.datumRodjenja = datumRodjenja;
            return this;
        }

        public Builder withStarost(LocalDate datumRodjenja){
            LocalDate sada = LocalDate.now();
            Period period = Period.between(datumRodjenja, sada);

            this.starost = period.getYears();
            return this;
        }


        public Osoba build(){
            Osoba osoba = new Osoba();
            osoba.id = this.id;
            osoba.ime = this.ime;
            osoba.prezime = this.prezime;
            osoba.zupanija = this.zupanija;
            osoba.zarazenBolescu = this.zarazenBolescu;
            osoba.kontaktiraneOsobe = this.kontaktiraneOsobe;
            osoba.datumRodjenja = this.datumRodjenja;
            osoba.starost = this.starost;
            if(zarazenBolescu instanceof Virus virus) {
                for (Osoba kontakt : kontaktiraneOsobe) {
                    virus.prelazakZarazeNaOsobu(kontakt);
                }
            }
            return osoba;
        }
    }

    private Osoba(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDate getDatumRodjenja() {
        return datumRodjenja;
    }

    public void setDatumRodjenja(LocalDate datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
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

    public Integer getStarost() {
        return starost;
    }

    public void setStarost(Integer starost) {
        this.starost = starost;
    }

    @Override
    public String toString() {
        return getIme() + " " + getPrezime();
    }
}
