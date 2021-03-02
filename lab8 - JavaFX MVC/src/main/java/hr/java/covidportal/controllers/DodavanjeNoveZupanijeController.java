package main.java.hr.java.covidportal.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import main.java.hr.java.covidportal.main.Main;
import main.java.hr.java.covidportal.model.Zupanija;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class DodavanjeNoveZupanijeController implements Initializable {
    @FXML
    private TextField nazivZupanijeTextField;

    @FXML
    private TextField brojStanovnikaTextField;

    @FXML
    private TextField brojZarazenihTextField;

    @FXML
    private TextField sifraTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void dodavanjeZupanijeUDatoteku(){
        String nazivZupanije = nazivZupanijeTextField.getText();
        String brojStanovnika = brojStanovnikaTextField.getText();
        String brojZarazenih = brojZarazenihTextField.getText();
        String sifraZupanije = sifraTextField.getText();
        List<Zupanija> listaZupanija = Main.dohvacanjeZupanija();
        boolean nijeRanijeUnesenNaziv = true;
        boolean nijeRanijeUnesenaSifra = true;
        boolean isIntStanovnici = true;
        boolean isIntZarazeni = true;

        for (Zupanija zupanija : listaZupanija){
            if(zupanija.getNaziv().toLowerCase().equals(nazivZupanije.toLowerCase())){
                nijeRanijeUnesenNaziv = false;
                break;
            }
        }

        for (Zupanija zupanija : listaZupanija) {
            if (zupanija.getSifra().toLowerCase().equals(sifraZupanije.toLowerCase())) {
                nijeRanijeUnesenaSifra = false;
                break;
            }
        }

        try {
            Integer brojStanovnikaInt = Integer.parseInt(brojStanovnika);
        } catch (NumberFormatException ex) {
            isIntStanovnici = false;
        }

        try {
            Integer brojZarazenihInt = Integer.parseInt(brojZarazenih);
        } catch (NumberFormatException ex) {
            isIntZarazeni = false;
        }

        postaviValidaciju(nazivZupanijeTextField, nijeRanijeUnesenNaziv);
        postaviValidaciju(sifraTextField, nijeRanijeUnesenaSifra) ;
        postaviValidaciju(brojStanovnikaTextField, isIntStanovnici);
        postaviValidaciju(brojZarazenihTextField, isIntZarazeni);

        if (nazivZupanije.isEmpty() || brojStanovnika.isEmpty() || brojZarazenih.isEmpty() || sifraZupanije.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Dodavanje nove županije");
            alert.setHeaderText("Greška pri dodavanju županije!");
            alert.setContentText("Niste unijeli sve tražene podatke potrebne za dodavanje županije.");
            alert.showAndWait();
        }

        else if (!isIntZarazeni || !isIntStanovnici){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Dodavanje nove županije");
            alert.setHeaderText("Greška pri dodavanju županije!");
            alert.setContentText("Polja za unos broja stanovnika i broja zaraženih zahtjevaju brojčane vrijenosti.");
            alert.showAndWait();
        }

        else if (!nijeRanijeUnesenNaziv || !nijeRanijeUnesenaSifra){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Dodavanje nove županije");
            alert.setHeaderText("Greška pri dodavanju županije!");
            alert.setContentText("Županija koji ste pokušali dodati, već se nalazi u tablici županija.");
            alert.showAndWait();
        }
        else{
            Long id = (long) listaZupanija.size() + 1;
            Path zupanijeDatoteka = Path.of("dat/zupanije.txt");
            try {
                Files.writeString(zupanijeDatoteka, "\n" + id.toString(), StandardOpenOption.APPEND);
                Files.writeString(zupanijeDatoteka, "\n" + nazivZupanije, StandardOpenOption.APPEND);
                Files.writeString(zupanijeDatoteka, "\n" + sifraZupanije, StandardOpenOption.APPEND);
                Files.writeString(zupanijeDatoteka, "\n" + brojStanovnika, StandardOpenOption.APPEND);
                Files.writeString(zupanijeDatoteka, "\n" + brojZarazenih, StandardOpenOption.APPEND);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Dodavanje nove županije");
                alert.setHeaderText("Uspješno ste dodali novu županiju!");
                alert.setContentText("Novounesenu županiju možete pronaći u tablici županija.");
                alert.showAndWait();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void postaviValidaciju(final TextField textField, boolean provjera) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                validacija(textField, provjera);
            }
        });
        validacija(textField, provjera);
    }


    private void validacija(TextField textField, boolean provjera) {
        ObservableList<String> styleClass = textField.getStyleClass();
        if (textField.getText().trim().length() == 0 || !provjera) {
            if (!styleClass.contains("error")) {
                styleClass.add("error");
            }
        } else {
            styleClass.removeAll(Collections.singleton("error"));
        }
    }
}
