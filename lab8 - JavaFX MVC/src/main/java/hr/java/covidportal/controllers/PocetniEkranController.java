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
        pretragaZupanijaScene.getStylesheets().add(getClass().getClassLoader()
                .getResource("css/style.css").toExternalForm());
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
        pretragaSimptomaScene.getStylesheets().add(getClass().getClassLoader()
                .getResource("css/style.css").toExternalForm());
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
        pretragaBolestiScene.getStylesheets().add(getClass().getClassLoader()
                .getResource("css/style.css").toExternalForm());
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
        pretragaVirusaScene.getStylesheets().add(getClass().getClassLoader()
                .getResource("css/style.css").toExternalForm());
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
        pretragaOsobaScene.getStylesheets().add(getClass().getClassLoader()
                .getResource("css/style.css").toExternalForm());
        Main.getMainStage().setScene(pretragaOsobaScene);
    }

    @FXML
    public void prikaziEkranZaDodavanjeZupanije() throws IOException{
        Parent dodavanjeZupanijeFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeNoveZupanije.fxml"));
        Scene dodavanjeZupanijeScene = new Scene(dodavanjeZupanijeFrame, 600, 400);
        dodavanjeZupanijeScene.getStylesheets().add(getClass().getClassLoader()
                .getResource("css/style.css").toExternalForm());
        Main.getMainStage().setScene(dodavanjeZupanijeScene);
    }

    @FXML
    public void prikaziEkranZaDodavanjeSimptoma() throws IOException {
        Parent dodavanjeSimptomaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeNovogSimptoma.fxml"));
        Scene dodavanjeSimptomaScene = new Scene(dodavanjeSimptomaFrame, 600, 400);
        dodavanjeSimptomaScene.getStylesheets().add(getClass().getClassLoader()
                .getResource("css/style.css").toExternalForm());
        Main.getMainStage().setScene(dodavanjeSimptomaScene);
    }

    @FXML
    public void prikaziEkranZaDodavanjeBolesti() throws IOException {
        Parent dodavanjeBolestiFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeNoveBolesti.fxml"));
        Scene dodavanjeBolestiScene = new Scene(dodavanjeBolestiFrame, 600, 400);
        dodavanjeBolestiScene.getStylesheets().add(getClass().getClassLoader()
                .getResource("css/style.css").toExternalForm());
        Main.getMainStage().setScene(dodavanjeBolestiScene);
    }

    @FXML
    public void prikaziEkranZaDodavanjeVirusa() throws IOException {
        Parent dodavanjeVirusaFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeNovogVirusa.fxml"));
        Scene dodavanjeVirusaScene = new Scene(dodavanjeVirusaFrame, 600, 400);
        dodavanjeVirusaScene.getStylesheets().add(getClass().getClassLoader()
                .getResource("css/style.css").toExternalForm());
        Main.getMainStage().setScene(dodavanjeVirusaScene);
    }

    @FXML
    public void prikaziEkranZaDodavanjeOsoba() throws IOException {
        Parent dodavanjeOsobeFrame =
                FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeNoveOsobe.fxml"));
        Scene dodavanjeOsobeScene = new Scene(dodavanjeOsobeFrame, 600, 400);
        dodavanjeOsobeScene.getStylesheets().add(getClass().getClassLoader()
                .getResource("css/style.css").toExternalForm());
        Main.getMainStage().setScene(dodavanjeOsobeScene);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}