package main.java.hr.java.covidportal.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import main.java.hr.java.covidportal.database.BazaPodataka;
import main.java.hr.java.covidportal.model.Zupanija;
import main.java.hr.java.covidportal.niti.DohvacanjeSvihZupanijaNit;
import main.java.hr.java.covidportal.niti.SpremanjeNoveZupanijeNit;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DodavanjeNoveZupanijeController implements Initializable {
    @FXML
    private TextField nazivZupanijeTextField;

    @FXML
    private TextField brojStanovnikaTextField;

    @FXML
    private TextField brojZarazenihTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void dodavanjeZupanijeUBazu(){
        String nazivZupanije = nazivZupanijeTextField.getText();
        String brojStanovnika = brojStanovnikaTextField.getText();
        String brojZarazenih = brojZarazenihTextField.getText();

        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<List<Zupanija>> future = executorService.submit(new DohvacanjeSvihZupanijaNit());
        List<Zupanija> listaZupanija = new ArrayList<>();
        try {
            listaZupanija = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        boolean nijeRanijeUneseno = true;
        boolean isInt = true;

        for (Zupanija zupanija : listaZupanija){
            if(zupanija.getNaziv().toLowerCase().equals(nazivZupanije.toLowerCase())){
                nijeRanijeUneseno = false;
                break;
            }
        }

        try {
            Integer brojStanovnikaInt = Integer.parseInt(brojStanovnika);
            Integer brojZarazenihInt = Integer.parseInt(brojZarazenih);
        }
        catch (NumberFormatException ex){
            isInt = false;
        }

        if (nazivZupanije.isEmpty() || brojStanovnika.isEmpty() || brojZarazenih.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Dodavanje nove županije");
            alert.setHeaderText("Greška pri dodavanju županije!");
            alert.setContentText("Niste unijeli sve tražene podatke potrebne za dodavanje županije.");
            alert.showAndWait();
        }

        else if (!isInt){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Dodavanje nove županije");
            alert.setHeaderText("Greška pri dodavanju županije!");
            alert.setContentText("Polja za unos broja stanovnika i broja zaraženih zahtjevaju brojčane vrijenosti.");
            alert.showAndWait();
        }

        else if (!nijeRanijeUneseno){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Dodavanje nove županije");
            alert.setHeaderText("Greška pri dodavanju županije!");
            alert.setContentText("Županija koji ste pokušali dodati, već se nalazi u tablici županija.");
            alert.showAndWait();
        }
        else{
            executorService.execute(new SpremanjeNoveZupanijeNit
                    (new Zupanija(nazivZupanije, Integer.parseInt(brojStanovnika), Integer.parseInt(brojZarazenih))));
            executorService.shutdown();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Dodavanje nove županije");
            alert.setHeaderText("Uspješno ste dodali novu županiju!");
            alert.setContentText("Novounesenu županiju možete pronaći u tablici županija.");
            alert.showAndWait();
        }
    }
}
