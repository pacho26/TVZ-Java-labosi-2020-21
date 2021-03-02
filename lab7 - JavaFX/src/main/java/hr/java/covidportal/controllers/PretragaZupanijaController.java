package main.java.hr.java.covidportal.controllers;

import javafx.scene.control.cell.PropertyValueFactory;
import main.java.hr.java.covidportal.main.Main;
import main.java.hr.java.covidportal.model.Zupanija;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;


import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Služi kao controller klasa za scenu gdje se pretražuju županije.
 */
public class PretragaZupanijaController implements Initializable {

    private static ObservableList<Zupanija> listaZupanija;

    @FXML
    private TextField nazivZupanijeTextField;

    @FXML
    private TableView<Zupanija> tablicaZupanija;

    @FXML
    private TableColumn<Zupanija, String> nazivZupanijeColumn;

    @FXML
    private TableColumn<Zupanija, Integer> brojStanovnikaZupanijeColumn;

    @FXML
    private TableColumn<Zupanija, Integer> brojZarazenihZupanijeColumn;

    /**
     * Inicijalizira stupce kao određene parametre tražene klase.
     * Stvara se nova lista, koja čita podatke iz datoteke pomoću metode koja se nalazi u <code>Main.</code>
     * Ta se lista pretvara u <code>observable</code>, te se na kraju te vrijednosti postavaljaju u tablicu.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nazivZupanijeColumn.setCellValueFactory(new PropertyValueFactory<>("naziv"));
        brojStanovnikaZupanijeColumn.setCellValueFactory(new PropertyValueFactory<>("brojStanovnika"));
        brojZarazenihZupanijeColumn.setCellValueFactory(new PropertyValueFactory<>("brojZarazenih"));

        List<Zupanija> procitaneZupanije = Main.dohvacanjeZupanija();
        listaZupanija = FXCollections.observableArrayList(procitaneZupanije);
        tablicaZupanija.setItems(listaZupanija);
    }

    /**
     * Filtrira listu na način da ostavlja samo one elemente koji sadržavaju <code>String</code> koji smo unijeli.
     * <code>ArrayList</code> se na kraju pretvara u novu <code>ObservableList</code>
     * te se njene vrijednosti postavljaju u tablicu.
     * Pritom originalna <code>ObservableList</code> sa svim podacima ostaje spremljena za kasnije nove pretrage.
     */
    public void pretraga() {
        String nazivZupanije = nazivZupanijeTextField.getText().toLowerCase();

        List<Zupanija> filtriraneZupanije = listaZupanija.stream()
                .filter(zupanija -> zupanija.getNaziv().toLowerCase().contains(nazivZupanije))
                .collect(Collectors.toList());
        ObservableList<Zupanija> novaListaZupanija = FXCollections.observableArrayList(filtriraneZupanije);
        tablicaZupanija.setItems(novaListaZupanija);
    }
}
