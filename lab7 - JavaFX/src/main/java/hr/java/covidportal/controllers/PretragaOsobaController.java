package main.java.hr.java.covidportal.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.java.hr.java.covidportal.main.Main;
import main.java.hr.java.covidportal.model.Bolest;
import main.java.hr.java.covidportal.model.Osoba;
import main.java.hr.java.covidportal.model.Zupanija;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Služi kao controller klasa za scenu gdje se pretražuju osobe.
 */
public class PretragaOsobaController implements Initializable {
    private static ObservableList<Osoba> listaOsoba;

    @FXML
    private TextField imeOsobeTextField;

    @FXML
    private TextField prezimeOsobeTextField;

    @FXML
    private TextField korisnickoImeTextField;

    @FXML
    private TableView<Osoba> tablicaOsoba;

    @FXML
    private TableColumn<Osoba, String> imeOsobeColumn;

    @FXML
    private TableColumn<Osoba, String> prezimeOsobeColumn;

    @FXML
    private TableColumn<Osoba, Integer> starostOsobeColumn;

    @FXML
    private TableColumn<Osoba, Zupanija> zupanijaOsobeColumn;

    @FXML
    private TableColumn<Osoba, Bolest> bolestOsobeColumn;

    @FXML
    private TableColumn<Osoba, List<Osoba>> kontaktiOsobeColumn;

    @FXML
    private TableColumn<Osoba, String> korisnickoImeColumn;

    /**
     * Inicijalizira stupce kao određene parametre tražene klase.
     * Stvara se nova lista, koja čita podatke iz datoteke pomoću metode koja se nalazi u <code>Main.</code>
     * Ta se lista pretvara u <code>observable</code>, te se na kraju te vrijednosti postavaljaju u tablicu.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imeOsobeColumn.setCellValueFactory(new PropertyValueFactory<>("ime"));
        prezimeOsobeColumn.setCellValueFactory(new PropertyValueFactory<>("prezime"));
        starostOsobeColumn.setCellValueFactory(new PropertyValueFactory<>("starost"));
        zupanijaOsobeColumn.setCellValueFactory(new PropertyValueFactory<>("zupanija"));
        bolestOsobeColumn.setCellValueFactory(new PropertyValueFactory<>("zarazenBolescu"));
        kontaktiOsobeColumn.setCellValueFactory(new PropertyValueFactory<>("kontaktiraneOsobe"));
        korisnickoImeColumn.setCellValueFactory(new PropertyValueFactory<>("korisnickoIme"));


        List<Osoba> procitaneOsobe = Main.dohvacanjeOsoba();
        listaOsoba = FXCollections.observableArrayList(procitaneOsobe);
        tablicaOsoba.setItems(listaOsoba);

        tablicaOsoba.setRowFactory(tablica -> { //------------------------------2. ZADATAK------------------------------
            TableRow<Osoba> red = new TableRow<>();
            red.setOnMouseClicked(mis -> {
                if(mis.getClickCount() == 2 && (!red.isEmpty())){
                    Osoba osoba = red.getItem();
                    dupliKlik(osoba);
                }
            });
            return red;
        });
    }

    /**
     * Filtrira listu na način da ostavlja samo one elemente koji sadržavaju <code>String</code> koji smo unijeli.
     * <code>ArrayList</code> se na kraju pretvara u novu <code>ObservableList</code>
     * te se njene vrijednosti postavljaju u tablicu.
     * Pritom originalna <code>ObservableList</code> sa svim podacima ostaje spremljena za kasnije nove pretrage.
     */
    public void pretraga() {
        String imeOsobe = imeOsobeTextField.getText().toLowerCase();
        String prezimeOsobe = prezimeOsobeTextField.getText().toLowerCase();
        String korisnickoIme = korisnickoImeTextField.getText().toLowerCase();

        List<Osoba> filtriraneZupanije = listaOsoba.stream()
                .filter(osoba -> osoba.getIme().toLowerCase().contains(imeOsobe) &&
                        osoba.getPrezime().toLowerCase().contains(prezimeOsobe) &&
                        osoba.getKorisnickoIme().toLowerCase().contains(korisnickoIme)) //---------1. ZADATAK-----------
                .collect(Collectors.toList());
        ObservableList<Osoba> novaListaOsoba = FXCollections.observableArrayList(filtriraneZupanije);
        tablicaOsoba.setItems(novaListaOsoba);
    }

    public void dupliKlik(Osoba osoba){     //--------------------------------2. ZADATAK--------------------------------
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getClassLoader()
                    .getResource("podaciPojedineOsobe.fxml"));
            Scene scena = new Scene(loader.load(), 600, 400);
            Stage stage = new Stage();
            stage.setScene(scena);
            stage.show();

            PodaciPojedineOsobeController mojController = loader.getController();
            mojController.prikaziPodatkeOsobe(osoba);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}