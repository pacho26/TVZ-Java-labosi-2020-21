package main.java.hr.java.covidportal.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.java.hr.java.covidportal.database.BazaPodataka;
import main.java.hr.java.covidportal.main.Main;
import main.java.hr.java.covidportal.model.Osoba;
import main.java.hr.java.covidportal.model.Zupanija;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
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

        List<Zupanija> procitaneZupanije = new ArrayList<>();
        try {
            procitaneZupanije = BazaPodataka.dohvacanjeSvihZupanija();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        procitaneZupanije = procitaneZupanije.stream()
                .sorted(Comparator.comparing(Zupanija::getNaziv)).collect(Collectors.toList());
        listaZupanija = FXCollections.observableArrayList(procitaneZupanije);
        tablicaZupanija.setItems(listaZupanija);


        //----------------------------------1. ZADATAK----------------------------------
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem1 = new MenuItem("Ukloni");
        contextMenu.getItems().add(menuItem1);

        menuItem1.setOnAction((ActionEvent event) -> {
            try {
                Zupanija odabranaZupanija = BazaPodataka.dohvacanjeZupanijePremaId
                        (tablicaZupanija.getSelectionModel().getSelectedItem().getId());
                BazaPodataka.uklanjanjeZupanije(odabranaZupanija.getId());
            }
            catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        tablicaZupanija.setContextMenu(contextMenu);

        //----------------------------------2. ZADATAK----------------------------------
        tablicaZupanija.setRowFactory(tv -> {
            TableRow<Zupanija> red = new TableRow<>();
            red.setOnMouseClicked(mis -> {
                if (mis.getClickCount() == 2 && (!red.isEmpty())) {
                    Zupanija zupanija = red.getItem();
                    dupliKlik(zupanija);
                }
            });
            return red;
        });
    }

    public void dupliKlik(Zupanija zupanija) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Popis osoba određene županije");
        alert.setHeaderText(zupanija.getNaziv());

        try {
            List<Osoba> listaOsoba = BazaPodataka.popisOsobaOdredjeneZupanije(zupanija.getId());
            if (listaOsoba.size() > 0){
                String stringOsoba = "";
                for (Osoba osoba : listaOsoba) {
                    stringOsoba += osoba.getIme() + " " + osoba.getPrezime() + "\n";
                }
                alert.setContentText(stringOsoba);
            }
            else {
                alert.setContentText("U izabranoj županiji ne živi ni jedna osoba.");
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        alert.showAndWait();
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
