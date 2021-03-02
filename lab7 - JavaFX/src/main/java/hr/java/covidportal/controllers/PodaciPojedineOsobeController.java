package main.java.hr.java.covidportal.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.java.hr.java.covidportal.model.Osoba;

public class PodaciPojedineOsobeController {    //------------------------------2. ZADATAK------------------------------
    @FXML
    private Label imePrezimeLabel;

    @FXML
    private Label korisnickoImeLabel;

    @FXML
    private Label idLabel;

    @FXML
    private Label zupanijaLabel;

    @FXML
    private Label zarazenBolescuLabel;

    @FXML
    private Label kontaktiLabel;

    public void prikaziPodatkeOsobe(Osoba osoba){
        this.imePrezimeLabel.setText(osoba.getIme() + " " + osoba.getPrezime());
        this.korisnickoImeLabel.setText(osoba.getKorisnickoIme());
        this.idLabel.setText(osoba.getId().toString());
        this.zupanijaLabel.setText(osoba.getZupanija().getNaziv());
        this.zarazenBolescuLabel.setText(osoba.getZarazenBolescu().getNaziv());
        this.kontaktiLabel.setText(osoba.getKontaktiraneOsobe().toString());
    }
}
