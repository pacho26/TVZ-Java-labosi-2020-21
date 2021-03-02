package main.java.hr.java.covidportal.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import main.java.hr.java.covidportal.database.BazaPodataka;
import main.java.hr.java.covidportal.genericsi.ComboBoxItemWrap;
import main.java.hr.java.covidportal.main.Main;
import main.java.hr.java.covidportal.model.*;
import main.java.hr.java.covidportal.niti.DohvacanjeSvihBolestiNit;
import main.java.hr.java.covidportal.niti.DohvacanjeSvihOsobaNit;
import main.java.hr.java.covidportal.niti.DohvacanjeSvihZupanijaNit;
import main.java.hr.java.covidportal.niti.SpremanjeNoveOsobeNit;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class DodavanjeNoveOsobeController implements Initializable {
    private ComboBox<ComboBoxItemWrap<Osoba>> kontaktiraneOsobeComboBox;
    private List<Osoba> listaOsoba;

    @FXML
    private TextField imeTextField;

    @FXML
    private TextField prezimeTextField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ChoiceBox<Zupanija> zupanijaChoiceBox;

    @FXML
    private ChoiceBox<Bolest> bolestChoiceBox;

    @FXML
    private VBox vBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<List<Zupanija>> futureZupanije = executorService.submit(new DohvacanjeSvihZupanijaNit());
        List<Zupanija> listaSvihZupanija = new ArrayList<>();
        try {
            listaSvihZupanija = futureZupanije.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        listaSvihZupanija = listaSvihZupanija.stream()
                .sorted(Comparator.comparing(Zupanija::getNaziv))
                .collect(Collectors.toList());
        zupanijaChoiceBox.getItems().addAll(listaSvihZupanija);



        Future<List<Bolest>> futureBolesti = executorService.submit(new DohvacanjeSvihBolestiNit());

        List<Bolest> listaSvihBolesti = new ArrayList<>();
        try {
            listaSvihBolesti = futureBolesti.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        listaSvihBolesti = listaSvihBolesti.stream()
                .sorted(Comparator.comparing(Bolest::getNaziv))
                .collect(Collectors.toList());
        bolestChoiceBox.getItems().addAll(listaSvihBolesti);



        kontaktiraneOsobeComboBox = new ComboBox<>();

        Future<List<Osoba>> futureOsobe = executorService.submit(new DohvacanjeSvihOsobaNit());

        try {
            listaOsoba = futureOsobe.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        executorService.shutdown();

        listaOsoba = listaOsoba.stream()
                .sorted(Comparator.comparing(Osoba::getPrezime))
                .collect(Collectors.toList());
        List<ComboBoxItemWrap<Osoba>> izabraneOsobe = new ArrayList<>();
        for (Osoba osoba : listaOsoba){
            izabraneOsobe.add(new ComboBoxItemWrap<>(osoba));
        }

        ObservableList<ComboBoxItemWrap<Osoba>> options = FXCollections.observableArrayList(izabraneOsobe);


        kontaktiraneOsobeComboBox.setCellFactory(o -> {
            ListCell<ComboBoxItemWrap<Osoba>> cell = new ListCell<>() {
                @Override
                protected void updateItem(ComboBoxItemWrap<Osoba> item, boolean empty){
                    super.updateItem(item, empty);
                    if (!empty){
                        final CheckBox checkBox = new CheckBox(item.toString());
                        checkBox.selectedProperty().bind(item.checkProperty());
                        setGraphic(checkBox);
                    }
                }
            };

            cell.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
                cell.getItem().checkProperty().set(!cell.getItem().checkProperty().get());
                StringBuilder stringBuilder = new StringBuilder();
                kontaktiraneOsobeComboBox.getItems().filtered(f -> f != null)
                        .filtered(ComboBoxItemWrap::getCheck)
                        .forEach(osoba -> stringBuilder.append(", " + osoba.getItem()));
                final String string = stringBuilder.toString();
                kontaktiraneOsobeComboBox.setPromptText(string.substring(Integer.min(2, string.length())));
            });

            return cell;
        });
        kontaktiraneOsobeComboBox.setItems(options);
        vBox.getChildren().addAll(kontaktiraneOsobeComboBox);
    }

    public void dodavanjeOsobeUBazu(){
        String ime = imeTextField.getText();
        String prezime = prezimeTextField.getText();
        LocalDate datumRodjenja = datePicker.getValue();
        Zupanija zupanija = zupanijaChoiceBox.getValue();
        Bolest bolest = bolestChoiceBox.getValue();
        Long id = (long) listaOsoba.size() + 1;
        List<Osoba> izrabraneOsobe = new ArrayList<>();
        kontaktiraneOsobeComboBox.getItems().stream()
                .filter(ComboBoxItemWrap::getCheck)
                .collect(Collectors.toList())
                .forEach(osoba -> izrabraneOsobe.add(osoba.getItem()));

        if (ime.isEmpty() || prezime.isEmpty() || datumRodjenja == null || zupanija == null || bolest == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Dodavanje nove osobe");
            alert.setHeaderText("Greška pri dodavanju osobe!");
            alert.setContentText("Niste unijeli sve tražene podatke potrebne za dodavanje osobe.");
            alert.showAndWait();
        }

        else {
            Osoba osoba = new Osoba.Builder()
                    .withId(id)
                    .withIme(ime)
                    .withPrezime(prezime)
                    .withDatumRodjenja(datumRodjenja)
                    .withZupanija(zupanija)
                    .withZarazenBolescu(bolest)
                    .withKontaktiraneOsobe(izrabraneOsobe)
                    .build();

            ExecutorService executorService = Executors.newCachedThreadPool();
            executorService.execute(new SpremanjeNoveOsobeNit(osoba));

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Dodavanje nove osobe");
            alert.setHeaderText("Uspješno ste dodali novu osobu!");
            alert.setContentText("Novounesenu osobu možete pronaći u tablici osoba.");
            alert.showAndWait();
        }
    }
}