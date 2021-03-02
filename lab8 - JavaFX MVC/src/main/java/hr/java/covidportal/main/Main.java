package main.java.hr.java.covidportal.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.java.hr.java.covidportal.enumeracije.VrijednostiSimptoma;
import main.java.hr.java.covidportal.model.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private static Stage mainStage;

    /**
     * Služi za startanje početne scene aplikacije
     *
     * @param primaryStage početna scena iz koje krećemo koristiti aplikaciju.
     * @throws Exception baca se u slučaju ako smo krivo napisali naziv fxml-datoteke ili ako ona ne postoji.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root =
                FXMLLoader.load(getClass().getClassLoader().getResource("pocetniEkran.fxml"));
        primaryStage.setTitle("Covid portal");
        primaryStage.setScene(new Scene(root, 600, 400));
        root.getStylesheets().add(getClass().getClassLoader().getResource("css/pocetniEkran.css").toExternalForm());
        Image windowIcon = new Image("/icons/ps.jpg");
        primaryStage.getIcons().add(windowIcon);
        primaryStage.show();
        mainStage = primaryStage;
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    /**
     * Služi za čitanje datoteke i pohranjivanje pročitanih podataka u novu listu tipa <code>Zupanija</code>
     * @return vraća listu pročitanih podataka
     */
    public static List<Zupanija> dohvacanjeZupanija(){
        List<Zupanija> novaLista = new ArrayList<>();
        File zupanijeFile = new File("dat/zupanije.txt");
        try (BufferedReader in = new BufferedReader(new FileReader(zupanijeFile))) {
            String procitanaLinija;
            while ((procitanaLinija = in.readLine()) != null) {
                Long idZupanije = Long.parseLong(procitanaLinija);
                String nazivZupanije = in.readLine();
                String sifraZupanije = in.readLine();
                Integer brojStanovnika = Integer.parseInt(in.readLine());
                Integer brojZarazenih = Integer.parseInt(in.readLine());
                novaLista.add(new Zupanija(nazivZupanije, idZupanije, brojStanovnika, brojZarazenih, sifraZupanije));
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
        return novaLista;
    }

    /**
     * Služi za čitanje datoteke i pohranjivanje pročitanih podataka u novu listu tipa <code>Simptom</code>
     *
     * @return vraća listu pročitanih podataka
     */
    public static List<Simptom> dohvacanjeSimptoma() {
        List<Simptom> novaLista = new ArrayList<>();
        File simptomiFile = new File("dat/simptomi.txt");
        try (BufferedReader in = new BufferedReader(new FileReader(simptomiFile))) {
            String procitanaLinija;
            while ((procitanaLinija = in.readLine()) != null) {
                Long idSimptoma = Long.parseLong(procitanaLinija);
                String nazivSimptoma = in.readLine();
                String procitanaVrijednost = in.readLine();
                VrijednostiSimptoma vrijednostSimptoma;
                if(procitanaVrijednost.equals(VrijednostiSimptoma.CESTO.getVrijednost())){
                    vrijednostSimptoma = VrijednostiSimptoma.CESTO;
                }
                else {
                    vrijednostSimptoma = VrijednostiSimptoma.valueOf(procitanaVrijednost);
                }
                novaLista.add(new Simptom(idSimptoma, nazivSimptoma, vrijednostSimptoma));
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
        return novaLista;
    }

    /**
     * Služi za čitanje datoteke i pohranjivanje pročitanih podataka u novu listu tipa <code>Bolest</code>
     *
     * @return vraća listu pročitanih podataka
     */
    public static List<Bolest> dohvacanjeBolesti() {
        List<Bolest> novaLista = new ArrayList<>();
        File bolestiFile = new File("dat/bolesti.txt");
        try (BufferedReader in = new BufferedReader(new FileReader(bolestiFile))) {
            String procitanaLinija;
            while ((procitanaLinija = in.readLine()) != null) {
                Long idBolesti = Long.parseLong(procitanaLinija);
                String nazivBolesti = in.readLine();
                String stringSimptomaBolesti = in.readLine();
                String[] simptomiBolesti = stringSimptomaBolesti.split(",");
                List<Simptom> setSimptomaBolesti = new ArrayList<>();
                List<Simptom> simptomi = dohvacanjeSimptoma();
                for (String s : simptomiBolesti) {
                    for (Simptom simptom : simptomi) {
                        if (simptom.getId().toString().equals(s)) {
                            setSimptomaBolesti.add(simptom);
                        }
                    }
                }
                novaLista.add(new Bolest(nazivBolesti, idBolesti, setSimptomaBolesti));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return novaLista;
    }

    /**
     * Služi za čitanje datoteke i pohranjivanje pročitanih podataka u novu listu tipa <code>Virus</code>
     *
     * @return vraća listu pročitanih podataka
     */
    public static List<Virus> dohvacanjeVirusa() {
        List<Virus> novaLista = new ArrayList<>();
        File bolestiFile = new File("dat/virusi.txt");
        try (BufferedReader in = new BufferedReader(new FileReader(bolestiFile))) {
            String procitanaLinija;
            while ((procitanaLinija = in.readLine()) != null) {
                Long idVirusa = Long.parseLong(procitanaLinija);
                String nazivVirusa = in.readLine();
                String stringSimptomaVirusa = in.readLine();
                String[] simptomiVirusaStringovi = stringSimptomaVirusa.split(",");
                List<Simptom> listaSimptomaVirusa = new ArrayList<>();
                List<Simptom> simptomi = dohvacanjeSimptoma();
                for (String s : simptomiVirusaStringovi) {
                    for (Simptom simptom : simptomi) {
                        if (Long.parseLong(s) == simptom.getId()) {
                            listaSimptomaVirusa.add(simptom);
                        }
                    }
                }
                novaLista.add(new Virus(idVirusa, nazivVirusa, listaSimptomaVirusa));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return novaLista;
    }

    /**
     * Služi za čitanje datoteke i pohranjivanje pročitanih podataka u novu listu tipa <code>Osoba</code>
     *
     * @return vraća listu pročitanih podataka
     */
    public static List<Osoba> dohvacanjeOsoba() {
        List<Osoba> novaLista = new ArrayList<>();
        List<Zupanija> zupanije = dohvacanjeZupanija();
        List<Bolest> bolesti = dohvacanjeBolesti();
        List<Virus> virusi = dohvacanjeVirusa();
        File osobeFile = new File("dat/osobe.txt");
        try (BufferedReader in = new BufferedReader(new FileReader(osobeFile))) {
            String procitanaLinija;
            while ((procitanaLinija = in.readLine()) != null) {
                Long idOsobe = Long.parseLong(procitanaLinija);
                String imeOsobe = in.readLine();
                String prezimeOsobe = in.readLine();
                Integer starostOsobe = Integer.parseInt(in.readLine());
                Long idZupanije = Long.parseLong(in.readLine());
                Zupanija tempZupanija = null;
                for (Zupanija zupanija : zupanije) {
                    if (zupanija.getId().equals(idZupanije)) {
                        tempZupanija = zupanija;
                    }
                }
                Long idBolestiIliVirusa = Long.parseLong(in.readLine());
                Bolest tempBolest = null;
                for (Bolest bolest : bolesti) {
                    if (bolest.getId().equals(idBolestiIliVirusa)) {
                        tempBolest = bolest;
                    }
                }
                for (Virus virus : virusi) {
                    if (virus.getId().equals(idBolestiIliVirusa)) {
                        tempBolest = virus;
                    }
                }
                String stringKontakata = "" + in.readLine();
                List<Osoba> tempKontaktiranihOsoba = new ArrayList<>();
                if (!stringKontakata.equals("")){
                    String[] kontaktiOsobe = stringKontakata.split(",");
                    for (String s : kontaktiOsobe) {
                        for (Osoba osoba : novaLista) {
                            if (osoba.getId().toString().equals(s)) {
                                tempKontaktiranihOsoba.add(osoba);
                            }
                        }
                    }
                }
                novaLista.add(new Osoba.Builder()
                        .withId(idOsobe)
                        .withIme(imeOsobe)
                        .withPrezime(prezimeOsobe)
                        .withStarost(starostOsobe)
                        .withZupanija(tempZupanija)
                        .withZarazenBolescu(tempBolest)
                        .withKontaktiraneOsobe(tempKontaktiranihOsoba)
                        .build());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return novaLista;
    }

    public static void main(String[] args) {
        launch(args);
    }
}