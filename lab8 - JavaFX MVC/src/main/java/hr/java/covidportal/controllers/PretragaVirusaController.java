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
import main.java.hr.java.covidportal.model.Simptom;
import main.java.hr.java.covidportal.model.Virus;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Služi kao controller klasa za scenu gdje se pretražuju virusi.
 */
public class PretragaVirusaController implements Initializable {

    private static ObservableList<Virus> listaVirusa;

    @FXML
    private TextField nazivVirusaTextField;

    @FXML
    private TableView<Virus> tablicaVirusa;

    @FXML
    private TableColumn<Virus, String> nazivVirusaColumn;

    @FXML
    private TableColumn<Virus, List<Simptom>> simptomiVirusaColumn;

    /**
     * Inicijalizira stupce kao određene parametre tražene klase.
     * Stvara se nova lista, koja čita podatke iz datoteke pomoću metode koja se nalazi u <code>Main.</code>
     * Ta se lista pretvara u <code>observable</code>, te se na kraju te vrijednosti postavaljaju u tablicu.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nazivVirusaColumn.setCellValueFactory(new PropertyValueFactory<>("naziv"));
        simptomiVirusaColumn.setCellValueFactory(new PropertyValueFactory<>("simptomi"));

        List<Virus> procitaniVirusi = Main.dohvacanjeVirusa();
        procitaniVirusi = procitaniVirusi.stream()
                .sorted(Comparator.comparing(Virus::getNaziv))
                .collect(Collectors.toList());
        listaVirusa = FXCollections.observableArrayList(procitaniVirusi);
        tablicaVirusa.setItems(listaVirusa);
    }

    /**
     * Filtrira listu na način da ostavlja samo one elemente koji sadržavaju <code>String</code> koji smo unijeli.
     * <code>ArrayList</code> se na kraju pretvara u novu <code>ObservableList</code>
     * te se njene vrijednosti postavljaju u tablicu.
     * Pritom originalna <code>ObservableList</code> sa svim podacima ostaje spremljena za kasnije nove pretrage.
     */
    public void pretraga() {
        String nazivVirusa = nazivVirusaTextField.getText().toLowerCase();

        List<Virus> filtriraniVirusi = listaVirusa.stream()
                .filter(virus -> virus.getNaziv().toLowerCase().contains(nazivVirusa))
                .collect(Collectors.toList());
        ObservableList<Virus> novaListaVirusa = FXCollections.observableArrayList(filtriraniVirusi);
        tablicaVirusa.setItems(novaListaVirusa);
    }
}