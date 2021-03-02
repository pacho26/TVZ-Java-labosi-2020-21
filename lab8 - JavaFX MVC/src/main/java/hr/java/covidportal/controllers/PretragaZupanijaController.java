package main.java.hr.java.covidportal.controllers;

import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.java.hr.java.covidportal.main.Main;
import main.java.hr.java.covidportal.model.Zupanija;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.w3c.dom.Text;


import java.net.URL;
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

    @FXML
    private TableColumn<Zupanija, Integer> sifraZupanijeColumn;

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
        sifraZupanijeColumn.setCellValueFactory(new PropertyValueFactory<>("sifra"));

        List<Zupanija> procitaneZupanije = Main.dohvacanjeZupanija();
        procitaneZupanije = procitaneZupanije.stream()
                .sorted(Comparator.comparing(Zupanija::getNaziv)).collect(Collectors.toList());
        listaZupanija = FXCollections.observableArrayList(procitaneZupanije);
        tablicaZupanija.setItems(listaZupanija);

        tablicaZupanija.setRowFactory(tablica -> {
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

    public void dupliKlik(Zupanija zupanija) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getClassLoader()
                    .getResource("podaciPojedineZupanije.fxml"));
            Scene scena = new Scene(loader.load(), 600, 400);
            scena.getStylesheets().add(getClass().getClassLoader()
                    .getResource("css/style.css").toExternalForm());
            Stage stage = Main.getMainStage();
            stage.setScene(scena);
            stage.show();

            PodaciPojedineZupanijeController mojController = loader.getController();
            mojController.prikaziPodatkeZupanije(zupanija);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
