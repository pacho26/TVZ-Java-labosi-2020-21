package main.java.hr.java.covidportal.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import main.java.hr.java.covidportal.main.Main;
import main.java.hr.java.covidportal.model.Zupanija;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class PodaciPojedineZupanijeController {
    public String naziv0;
    public String sifra0;
    public Long id0;
    public Integer brojStanovnika0;
    public Integer brojZarazenih0;
    @FXML
    private TextField nazivZupanije;

    @FXML
    private TextField sifraZupanije;

    @FXML
    private TextField brojStanovnika;

    @FXML
    private TextField brojZarazenih;

    public void prikaziPodatkeZupanije(Zupanija zupanija){
        naziv0 = zupanija.getNaziv();
        sifra0 = zupanija.getSifra();
        id0 = zupanija.getId();
        brojStanovnika0 = zupanija.getBrojStanovnika();
        brojZarazenih0 = zupanija.getBrojZarazenih();
    }

    public void spremiPromjene(){

        Zupanija zupanija0 = new Zupanija(naziv0, id0, brojStanovnika0, brojZarazenih0, sifra0);
        List<Zupanija> listaZupanija = Main.dohvacanjeZupanija();
        for (Zupanija zupanija : listaZupanija) {
            if (zupanija0.getId().equals(zupanija.getId())) {
                zupanija.setNaziv(nazivZupanije.getText());
                zupanija.setSifra(sifraZupanije.getText());
                zupanija.setBrojStanovnika(Integer.parseInt(brojStanovnika.getText()));
                zupanija.setBrojZarazenih(Integer.parseInt(brojZarazenih.getText()));
            }
        }

        Path zupanijeDatoteka = Path.of("dat/zupanije.txt");
        try{
            Files.writeString(zupanijeDatoteka, "");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Zupanija zupanija : listaZupanija){
            try {
                Files.writeString(zupanijeDatoteka, "\n" + zupanija.getId().toString(), StandardOpenOption.APPEND);
                Files.writeString(zupanijeDatoteka, "\n" + zupanija.getNaziv(), StandardOpenOption.APPEND);
                Files.writeString(zupanijeDatoteka, "\n" + zupanija.getSifra(), StandardOpenOption.APPEND);
                Files.writeString(zupanijeDatoteka, "\n" + zupanija.getBrojStanovnika().toString(), StandardOpenOption.APPEND);
                Files.writeString(zupanijeDatoteka, "\n" + zupanija.getBrojZarazenih().toString(), StandardOpenOption.APPEND);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }
}
