package main.java.hr.java.covidportal.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import main.java.hr.java.covidportal.database.BazaPodataka;
import main.java.hr.java.covidportal.enumeracije.VrijednostiSimptoma;
import main.java.hr.java.covidportal.model.Simptom;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DodavanjeNovogSimptomaController implements Initializable {
    @FXML
    private TextField nazivSimptomaTextField;

    @FXML
    private RadioButton produktivniRadioButton;

    @FXML
    private RadioButton intenzivnoRadioButton;

    @FXML
    private RadioButton visokaRadioButton;

    @FXML
    private RadioButton jakaRadioButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void dodavanjeSimptomaUBazu(){
        String nazivSimptoma = nazivSimptomaTextField.getText();
        VrijednostiSimptoma vrijednostSimptoma = null;
        boolean nijeIzabranaVrijednost = false;
        if (produktivniRadioButton.isSelected()){
            vrijednostSimptoma = VrijednostiSimptoma.Produktivni;
        }
        else if(intenzivnoRadioButton.isSelected()){
            vrijednostSimptoma = VrijednostiSimptoma.Intenzivno;
        }
        else if(visokaRadioButton.isSelected()){
            vrijednostSimptoma = VrijednostiSimptoma.Visoka;
        }
        else if (jakaRadioButton.isSelected()) {
            vrijednostSimptoma = VrijednostiSimptoma.Jaka;
        }
        else{
            nijeIzabranaVrijednost = true;
        }
        List<Simptom> listaSvihSimptoma = new ArrayList<>();
        try {
            listaSvihSimptoma = BazaPodataka.dohvacanjeSvihSimptoma();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        boolean vecUneseno = false;
        for (Simptom simptom : listaSvihSimptoma) {
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
            try {
                BazaPodataka.spremanjeNovogSimptoma(new Simptom(nazivSimptoma, vrijednostSimptoma));

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Dodavanje novog simptoma");
                alert.setHeaderText("Uspješno ste dodali novi simptom!");
                alert.setContentText("Novouneseni simptom možete pronaći u tablici simptoma.");
                alert.showAndWait();
            }
            catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
