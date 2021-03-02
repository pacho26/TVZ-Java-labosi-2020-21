package main.java.hr.java.covidportal.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.hr.java.covidportal.enumeracije.VrijednostiSimptoma;
import main.java.hr.java.covidportal.main.Main;
import main.java.hr.java.covidportal.model.Simptom;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Služi kao controller klasa za scenu gdje se pretražuju simptomi.
 */
public class PretragaSimptomaController implements Initializable {

    private static ObservableList<Simptom> listaSimptoma;

    @FXML
    private TextField nazivSimptomaTextField;

    @FXML
    private TableView<Simptom> tablicaSimptoma;

    @FXML
    private TableColumn<Simptom, String> nazivSimptomaColumn;

    @FXML
    private TableColumn<Simptom, VrijednostiSimptoma> vrijednostSimptomaColumn;

    /**
     * Inicijalizira stupce kao određene parametre tražene klase.
     * Stvara se nova lista, koja čita podatke iz datoteke pomoću metode koja se nalazi u <code>Main.</code>
     * Ta se lista pretvara u <code>observable</code>, te se na kraju te vrijednosti postavaljaju u tablicu.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nazivSimptomaColumn.setCellValueFactory(new PropertyValueFactory<>("naziv"));
        vrijednostSimptomaColumn.setCellValueFactory(new PropertyValueFactory<>("vrijednost"));

        List<Simptom> procitaniSimptomi = Main.dohvacanjeSimptoma();
        listaSimptoma = FXCollections.observableArrayList(procitaniSimptomi);
        tablicaSimptoma.setItems(listaSimptoma);
    }

    /**
     * Filtrira listu na način da ostavlja samo one elemente koji sadržavaju <code>String</code> koji smo unijeli.
     * <code>ArrayList</code> se na kraju pretvara u novu <code>ObservableList</code>
     * te se njene vrijednosti postavljaju u tablicu.
     * Pritom originalna <code>ObservableList</code> sa svim podacima ostaje spremljena za kasnije nove pretrage.
     */
    public void pretraga() {
        String nazivSimptoma = nazivSimptomaTextField.getText().toLowerCase();

        List<Simptom> filtriraniSimptomi = listaSimptoma.stream()
                .filter(simptom -> simptom.getNaziv().toLowerCase().contains(nazivSimptoma))
                .collect(Collectors.toList());
        ObservableList<Simptom> novaListaSimptoma = FXCollections.observableArrayList(filtriraniSimptomi);
        tablicaSimptoma.setItems(novaListaSimptoma);
    }
}
