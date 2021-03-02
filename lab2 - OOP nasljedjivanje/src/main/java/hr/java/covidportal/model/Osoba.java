package hr.java.covidportal.model;

public class Osoba {
    private String ime;
    private String prezime;
    private Integer starost;
    private Zupanija zupanija;
    private Bolest zarazenBolescu;
    private Osoba[] kontaktiraneOsobe;

    public static class Builder{
        private String ime;
        private String prezime;
        private Integer starost;
        private Zupanija zupanija;
        private Bolest zarazenBolescu;
        private Osoba[] kontaktiraneOsobe;

        public Builder withIme(String ime){
            this.ime = ime;
            return this;
        }

        public Builder withPrezime(String prezime){
            this.prezime = prezime;
            return this;
        }

        public Builder withStarost(Integer Starost){
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

        public Builder withKontaktiraneOsobe(Osoba[] kontaktiraneOsobe){
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
                for (int i = 0; i < kontaktiraneOsobe.length; i++) {
                    virus.prelazakZarazeNaOsobu(kontaktiraneOsobe[i]);
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

    public hr.java.covidportal.model.Osoba[] getKontaktiraneOsobe() {
        return kontaktiraneOsobe;
    }

    public void setKontaktiraneOsobe(hr.java.covidportal.model.Osoba[] kontaktiraneOsobe) {
        this.kontaktiraneOsobe = kontaktiraneOsobe;
    }
}
