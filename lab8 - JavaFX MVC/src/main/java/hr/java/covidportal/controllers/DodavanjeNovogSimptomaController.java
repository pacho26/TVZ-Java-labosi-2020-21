package main.java.hr.java.covidportal.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import main.java.hr.java.covidportal.enumeracije.VrijednostiSimptoma;
import main.java.hr.java.covidportal.main.Main;
import main.java.hr.java.covidportal.model.Simptom;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.ResourceBundle;

public class DodavanjeNovogSimptomaController implements Initializable {
    @FXML
    private TextField nazivSimptomaTextField;

    @FXML
    private RadioButton rijetkoRadioButton;

    @FXML
    private RadioButton srednjeRadioButton;

    @FXML
    private RadioButton cestoRadioButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void dodavanjeSimptomaUDatoteku(){
        String nazivSimptoma = nazivSimptomaTextField.getText();
        String vrijednostSimptoma = null;
        boolean nijeIzabranaVrijednost = false;
        if (rijetkoRadioButton.isSelected()){
            vrijednostSimptoma = VrijednostiSimptoma.RIJETKO.getVrijednost();
        }
        else if(srednjeRadioButton.isSelected()){
            vrijednostSimptoma = VrijednostiSimptoma.SREDNJE.getVrijednost();
        }
        else if(cestoRadioButton.isSelected()){
            vrijednostSimptoma = VrijednostiSimptoma.CESTO.getVrijednost();
        }
        else{
            nijeIzabranaVrijednost = true;
        }
        List<Simptom> listaSimptoma = Main.dohvacanjeSimptoma();

        boolean vecUneseno = false;
        for (Simptom simptom : listaSimptoma) {
            if (simptom.getNaziv().toLowerCase().equals(nazivSimptoma.toLowerCase())) {
                vecUneseno = true;
                break;
            }
        }

        if (nijeIzabranaVrijednost || nazivSimptoma.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Dodavanje novog simptoma");
            alert.setHeaderText("Greška pri dodavanju simptoma!");
            alert.setContentText("Niste unijeli naziv i/ili odabrali vrijednost simptoma.");
            alert.showAndWait();
        }
        else if (vecUneseno){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Dodavanje novog simptoma");
            alert.setHeaderText("Greška pri dodavanju simptoma!");
            alert.setContentText("Simptom koji ste pokušali dodati, već se nalazi u tablici simptoma.");
            alert.showAndWait();
        }
        else{
            Long id = (long) listaSimptoma.size() + 1;
            Path simptomiDatoteka = Path.of("dat/simptomi.txt");
            try {
                Files.writeString(simptomiDatoteka, "\n" + id.toString(),
                        StandardOpenOption.APPEND);
                Files.writeString(simptomiDatoteka, "\n" + nazivSimptoma,
                        StandardOpenOption.APPEND);
                Files.writeString(simptomiDatoteka, "\n" + vrijednostSimptoma,
                        StandardOpenOption.APPEND);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Dodavanje novog simptoma");
                alert.setHeaderText("Uspješno ste dodali novi simptom!");
                alert.setContentText("Novouneseni simptom možete pronaći u tablici simptoma.");
                alert.showAndWait();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
