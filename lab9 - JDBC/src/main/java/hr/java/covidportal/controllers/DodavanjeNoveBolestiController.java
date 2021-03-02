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

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DodavanjeNoveBolestiController implements Initializable {
    private ComboBox<ComboBoxItemWrap<Simptom>> simptomiComboBox;
    private List<Simptom> listaSimptoma;

    @FXML
    private TextField nazivBolestiTextField;

    @FXML
    private VBox vBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        simptomiComboBox = new ComboBox<>();

        try {
            listaSimptoma = BazaPodataka.dohvacanjeSvihSimptoma();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        listaSimptoma = listaSimptoma.stream()
                .sorted(Comparator.comparing(ImenovaniEntitet::getNaziv))
                .collect(Collectors.toList());
        List<ComboBoxItemWrap<Simptom>> odabraniSimptomi = new ArrayList<>();
        for (Simptom simptom : listaSimptoma) {
            odabraniSimptomi.add(new ComboBoxItemWrap<>(simptom));
        }

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

    public void dodavanjeBolestiUBazu(){
        String nazivBolesti = nazivBolestiTextField.getText();
        boolean vecUneseno = false;
        List<Bolest> listaSvihBolesti = new ArrayList<>();
        try {
            listaSvihBolesti = BazaPodataka.dohvacanjeSvihBolesti();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        List<Simptom> izabraniSimptomi = new ArrayList<>();
        simptomiComboBox.getItems().stream()
                .filter(ComboBoxItemWrap::getCheck)
                .collect(Collectors.toList())
                .forEach(simptom -> izabraniSimptomi.add(simptom.getItem()));

        for (Bolest bolest : listaSvihBolesti){
            if(bolest.getNaziv().toLowerCase().equals(nazivBolesti.toLowerCase())){
                vecUneseno = true;
                break;
            }
        }

        if (izabraniSimptomi.isEmpty() || nazivBolesti.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Dodavanje nove bolesti");
            alert.setHeaderText("Greška pri dodavanju bolesti!");
            alert.setContentText("Niste unijeli naziv bolesti i/ili odabrali simptome.");
            alert.showAndWait();
        }

        else if (vecUneseno) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Dodavanje nove bolesti");
            alert.setHeaderText("Greška pri dodavanju bolesti!");
            alert.setContentText("Bolest koju ste pokušali dodati, već se nalazi u tablici bolesti.");
            alert.showAndWait();
        }
        else {
            try {
                BazaPodataka.spremanjeNoveBolesti(new Bolest(nazivBolesti, izabraniSimptomi), false);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Dodavanje nove bolesti");
                alert.setHeaderText("Uspješno ste dodali novu bolest!");
                alert.setContentText("Novounesenu bolest možete pronaći u tablici bolesti.");
                alert.showAndWait();
            }
            catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}