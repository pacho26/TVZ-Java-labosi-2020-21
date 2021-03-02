package main.java.hr.java.covidportal.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import main.java.hr.java.covidportal.genericsi.ComboBoxItemWrap;
import main.java.hr.java.covidportal.main.Main;
import main.java.hr.java.covidportal.model.*;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

public class DodavanjeNoveOsobeController implements Initializable {
    private ComboBox<ComboBoxItemWrap<Osoba>> kontaktiraneOsobeComboBox;
    private List<Osoba> listaOsoba;

    @FXML
    private TextField imeTextField;

    @FXML
    private TextField prezimeTextField;

    @FXML
    private TextField starostTextField;

    @FXML
    private ChoiceBox<Zupanija> zupanijaChoiceBox;

    @FXML
    private ChoiceBox<Bolest> bolestChoiceBox;

    @FXML
    private VBox vBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Zupanija> listaZupanija = Main.dohvacanjeZupanija();
        listaZupanija = listaZupanija.stream()
                .sorted(Comparator.comparing(Zupanija::getNaziv))
                .collect(Collectors.toList());
        zupanijaChoiceBox.getItems().addAll(listaZupanija);

        List<Bolest> listaBolesti = Main.dohvacanjeBolesti();
        listaBolesti = listaBolesti.stream()
                .sorted(Comparator.comparing(Bolest::getNaziv))
                .collect(Collectors.toList());
        List<Virus> listaVirusa = Main.dohvacanjeVirusa();
        listaVirusa = listaVirusa.stream()
                .sorted(Comparator.comparing(Virus::getNaziv))
                .collect(Collectors.toList());
        bolestChoiceBox.getItems().addAll(listaBolesti);
        bolestChoiceBox.getItems().addAll(listaVirusa);

        kontaktiraneOsobeComboBox = new ComboBox<>();
        listaOsoba = Main.dohvacanjeOsoba();
        listaOsoba = listaOsoba.stream()
                .sorted(Comparator.comparing(Osoba::getPrezime))
                .collect(Collectors.toList());
        List<ComboBoxItemWrap<Osoba>> mozdaOsobe = new ArrayList<>();
        for (Osoba osoba : listaOsoba){
            mozdaOsobe.add(new ComboBoxItemWrap<>(osoba));
        }

        ObservableList<ComboBoxItemWrap<Osoba>> options = FXCollections.observableArrayList(mozdaOsobe);

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

    public void dodavanjeOsobeUDatoteku(){
        String ime = imeTextField.getText();
        String prezime = prezimeTextField.getText();
        String starost = starostTextField.getText();
        Zupanija zupanija = zupanijaChoiceBox.getValue();
        Bolest bolest = bolestChoiceBox.getValue();
        Long id = (long) listaOsoba.size() + 1;
        List<Osoba> izrabraneOsobe = new ArrayList<>();
        kontaktiraneOsobeComboBox.getItems().stream()
                .filter(ComboBoxItemWrap::getCheck)
                .collect(Collectors.toList())
                .forEach(osoba -> izrabraneOsobe.add(osoba.getItem()));

        boolean isInt = true;
        try {
            Integer starostInt = Integer.parseInt(starost);
        } catch (NumberFormatException ex) {
            isInt = false;
        }

        if (ime.isEmpty() || prezime.isEmpty() || starost.isEmpty() || zupanija == null || bolest == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Dodavanje nove osobe");
            alert.setHeaderText("Greška pri dodavanju osobe!");
            alert.setContentText("Niste unijeli sve tražene podatke potrebne za dodavanje osobe.");
            alert.showAndWait();
        }
        else if (!isInt) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Dodavanje nove osobe");
            alert.setHeaderText("Greška pri dodavanju osobe!");
            alert.setContentText("Polje za unos starosti zahtjeva brojčanu vrijednost.");
            alert.showAndWait();
        }

        else {
            Path osobeDatoteka = Path.of("dat/osobe.txt");
            try {
                Files.writeString(osobeDatoteka, "\n" + id.toString(), StandardOpenOption.APPEND);
                Files.writeString(osobeDatoteka, "\n" + ime, StandardOpenOption.APPEND);
                Files.writeString(osobeDatoteka, "\n" + prezime, StandardOpenOption.APPEND);
                Files.writeString(osobeDatoteka, "\n" + starost, StandardOpenOption.APPEND);
                Files.writeString(osobeDatoteka, "\n" + zupanija.getId().toString(), StandardOpenOption.APPEND);
                Files.writeString(osobeDatoteka, "\n" + bolest.getId().toString(), StandardOpenOption.APPEND);
                Files.writeString(osobeDatoteka, "\n", StandardOpenOption.APPEND);
                for (int i = 0; i < izrabraneOsobe.size(); i++) {
                    Files.writeString(osobeDatoteka, izrabraneOsobe.get(i).getId().toString(),
                            StandardOpenOption.APPEND);
                    if ((i + 1) != izrabraneOsobe.size()) {
                        Files.writeString(osobeDatoteka, ",", StandardOpenOption.APPEND);
                    }
                }
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Dodavanje nove osobe");
            alert.setHeaderText("Uspješno ste dodali novu osobu!");
            alert.setContentText("Novounesenu osobu možete pronaći u tablici osoba.");
            alert.showAndWait();
        }
    }
}