package main.java.hr.java.covidportal.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import main.java.hr.java.covidportal.main.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Služi kao početna scena s glavnim izbornikom koji nas vodi u sve ostale scene.
 */
public class PocetniEkranController implements Initializable {
    /**
     * Koristi se za prebacivanje na scenu gdje se pretražuju županije.
     * @throws IOException baca iznimku u slučaju kada je krivo uneseno ime fxml-datoteke
     */
    @FXML
    public void prikaziEkranZaPretraguZupanija() throws IOException {
        Parent pretragaZupanijaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaZupanija.fxml"));
        Scene pretragaZupanijaScene = new Scene(pretragaZupanijaFrame, 600, 400);
        Main.getMainStage().setScene(pretragaZupanijaScene);
    }

    /**
     * Koristi se za prebacivanje na scenu gdje se pretražuju simptomi.
     *
     * @throws IOException baca iznimku u slučaju kada je krivo uneseno ime fxml-datoteke
     */
    @FXML
    public void prikaziEkranZaPretraguSimptoma() throws IOException {
        Parent pretragaSimptomaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaSimptoma.fxml"));
        Scene pretragaSimptomaScene = new Scene(pretragaSimptomaFrame, 600, 400);
        Main.getMainStage().setScene(pretragaSimptomaScene);
    }

    /**
     * Koristi se za prebacivanje na scenu gdje se pretražuju bolesti.
     *
     * @throws IOException baca iznimku u slučaju kada je krivo uneseno ime fxml-datoteke
     */
    @FXML
    public void prikaziEkranZaPretraguBolesti() throws IOException {
        Parent pretragaBolestiFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaBolesti.fxml"));
        Scene pretragaBolestiScene = new Scene(pretragaBolestiFrame, 600, 400);
        Main.getMainStage().setScene(pretragaBolestiScene);
    }

    /**
     * Koristi se za prebacivanje na scenu gdje se pretražuju virusi.
     *
     * @throws IOException baca iznimku u slučaju kada je krivo uneseno ime fxml-datoteke
     */
    @FXML
    public void prikaziEkranZaPretraguVirusa() throws IOException {
        Parent pretragaVirusaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaVirusa.fxml"));
        Scene pretragaVirusaScene = new Scene(pretragaVirusaFrame, 600, 400);
        Main.getMainStage().setScene(pretragaVirusaScene);
    }

    /**
     * Koristi se za prebacivanje na scenu gdje se pretražuju osobe.
     *
     * @throws IOException baca iznimku u slučaju kada je krivo uneseno ime fxml-datoteke
     */
    @FXML
    public void prikaziEkranZaPretraguOsoba() throws IOException {
        Parent pretragaOsobaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("pretragaOsoba.fxml"));
        Scene pretragaOsobaScene = new Scene(pretragaOsobaFrame, 600, 400);
        Main.getMainStage().setScene(pretragaOsobaScene);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}