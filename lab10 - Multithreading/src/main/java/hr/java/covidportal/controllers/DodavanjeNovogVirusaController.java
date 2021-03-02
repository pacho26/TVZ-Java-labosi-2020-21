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
import main.java.hr.java.covidportal.model.Bolest;
import main.java.hr.java.covidportal.model.ImenovaniEntitet;
import main.java.hr.java.covidportal.model.Simptom;
import main.java.hr.java.covidportal.model.Virus;
import main.java.hr.java.covidportal.niti.DohvacanjeSvihBolestiNit;
import main.java.hr.java.covidportal.niti.DohvacanjeSvihSimptomaNit;
import main.java.hr.java.covidportal.niti.SpremanjeNovogVirusaNit;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class DodavanjeNovogVirusaController implements Initializable {
    private ComboBox<ComboBoxItemWrap<Simptom>> simptomiComboBox;
    private List<Simptom> listaSimptoma;

    @FXML
    private TextField nazivVirusaTextField;

    @FXML
    private VBox vBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<List<Simptom>> future = executorService.submit(new DohvacanjeSvihSimptomaNit());

        List<Simptom> listaSimptoma = new ArrayList<>();
        try {
            listaSimptoma = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        listaSimptoma = listaSimptoma.stream()
                .sorted(Comparator.comparing(ImenovaniEntitet::getNaziv))
                .collect(Collectors.toList());
        List<ComboBoxItemWrap<Simptom>> odabraniSimptomi = new ArrayList<>();
        for (Simptom simptom : listaSimptoma) {
            odabraniSimptomi.add(new ComboBoxItemWrap<>(simptom));
        }

        simptomiComboBox = new ComboBox<>();

        ObservableList<ComboBoxItemWrap<Simptom>> options = FXCollections.observableArrayList(odabraniSimptomi);

        simptomiComboBox.setCellFactory(s -> {
            ListCell<ComboBoxItemWrap<Simptom>> cell = new ListCell<>() {
                @Override
                protected void updateItem(ComboBoxItemWrap<Simptom> item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        final CheckBox checkBox = new CheckBox(item.toString());
                        checkBox.selectedProperty().bind(item.checkProperty());
                        setGraphic(checkBox);
                    }
                }
            };

            cell.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
                cell.getItem().checkProperty().set(!cell.getItem().checkProperty().get());
                StringBuilder stringBuilder = new StringBuilder();
                simptomiComboBox.getItems().filtered(f -> f != null)
                        .filtered(ComboBoxItemWrap::getCheck)
                        .forEach(simptom -> stringBuilder.append(", " + simptom.getItem()));
                final String string = stringBuilder.toString();
                simptomiComboBox.setPromptText(string.substring(Integer.min(2, string.length())));
            });

            return cell;
        });
        simptomiComboBox.setItems(options);
        vBox.getChildren().addAll(simptomiComboBox);
    }

    public void dodavanjeVirusaUBazu() {
        String nazivVirusa = nazivVirusaTextField.getText();
        List<Simptom> izabraniSimptomi = new ArrayList<>();
        simptomiComboBox.getItems().stream().filter(ComboBoxItemWrap::getCheck).collect(Collectors.toList())
                .forEach(simptom -> izabraniSimptomi.add(simptom.getItem()));

        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<List<Bolest>> future = executorService.submit(new DohvacanjeSvihBolestiNit());

        List<Bolest> listaSvihBolesti = new ArrayList<>();
        try {
            listaSvihBolesti = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        boolean vecUneseno = false;
        for (Bolest bolest : listaSvihBolesti) {
            if (bolest.getNaziv().toLowerCase().equals(nazivVirusa.toLowerCase())) {
                vecUneseno = true;
                break;
            }
        }

        if (izabraniSimptomi.isEmpty() || nazivVirusa.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Dodavanje novog virusa");
            alert.setHeaderText("Greška pri dodavanju virusa!");
            alert.setContentText("Niste unijeli naziv virusa i/ili odabrali simptome.");
            alert.showAndWait();
        }
        else if (vecUneseno) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Dodavanje novog virusa");
            alert.setHeaderText("Greška pri dodavanju virusa!");
            alert.setContentText("Virus koji ste pokušali dodati, već se nalazi u tablici virusa.");
            alert.showAndWait();
        }
        else {
            executorService.execute(new SpremanjeNovogVirusaNit(new Virus(nazivVirusa, izabraniSimptomi), true));
            executorService.shutdown();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Dodavanje novog virusa");
            alert.setHeaderText("Uspješno ste dodali novi virus!");
            alert.setContentText("Novouneseni virus možete pronaći u tablici virusa.");
            alert.showAndWait();
        }
    }
}
