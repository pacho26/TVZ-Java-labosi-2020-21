package main.java.hr.java.covidportal.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.hr.java.covidportal.main.Main;
import main.java.hr.java.covidportal.model.Bolest;
import main.java.hr.java.covidportal.model.Simptom;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Služi kao controller klasa za scenu gdje se pretražuju bolesti.
 */
public class PretragaBolestiController implements Initializable {

    private static ObservableList<Bolest> listaBolesti;

    @FXML
    private TextField nazivBolestiTextField;

    @FXML
    private TableView<Bolest> tablicaBolesti;

    @FXML
    private TableColumn<Bolest, String> nazivBolestiColumn;

    @FXML
    private TableColumn<Bolest, List<Simptom>> simptomiBolestiColumn;

    /**
     * Inicijalizira stupce kao određene parametre tražene klase.
     * Stvara se nova lista, koja čita podatke iz datoteke pomoću metode koja se nalazi u <code>Main.</code>
     * Ta se lista pretvara u <code>observable</code>, te se na kraju te vrijednosti postavaljaju u tablicu.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nazivBolestiColumn.setCellValueFactory(new PropertyValueFactory<>("naziv"));
        simptomiBolestiColumn.setCellValueFactory(new PropertyValueFactory<>("simptomi"));

        List<Bolest> procitaneBolesti = Main.dohvacanjeBolesti();
        listaBolesti = FXCollections.observableArrayList(procitaneBolesti);
        tablicaBolesti.setItems(listaBolesti);
    }

    /**
     * Filtrira listu na način da ostavlja samo one elemente koji sadržavaju <code>String</code> koji smo unijeli.
     * <code>ArrayList</code> se na kraju pretvara u novu <code>ObservableList</code>
     * te se njene vrijednosti postavljaju u tablicu.
     * Pritom originalna <code>ObservableList</code> sa svim podacima ostaje spremljena za kasnije nove pretrage.
     */
    public void pretraga() {
        String nazivBolesti = nazivBolestiTextField.getText().toLowerCase();

        List<Bolest> filtriraneZupanije = listaBolesti.stream()
                .filter(bolest -> bolest.getNaziv().toLowerCase().contains(nazivBolesti))
                .collect(Collectors.toList());
        ObservableList<Bolest> novaListaBolesti = FXCollections.observableArrayList(filtriraneZupanije);
        tablicaBolesti.setItems(novaListaBolesti);
    }
}
