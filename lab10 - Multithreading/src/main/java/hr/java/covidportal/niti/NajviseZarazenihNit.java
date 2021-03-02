package main.java.hr.java.covidportal.niti;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import main.java.hr.java.covidportal.database.BazaPodataka;
import main.java.hr.java.covidportal.main.Main;
import main.java.hr.java.covidportal.model.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NajviseZarazenihNit implements Runnable{

    public static Zupanija dohvatiZupanijuSNajviseZarazenih() {
        List<Zupanija> listaSvihZupanija = new ArrayList<>();
        try {
            listaSvihZupanija = BazaPodataka.dohvacanjeSvihZupanija();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return listaSvihZupanija.stream()
                .max((Zupanija z1, Zupanija z2) -> {
                    if (((double) z1.getBrojZarazenih() / z1.getBrojStanovnika() >
                            (double) z2.getBrojZarazenih() / z2.getBrojStanovnika())) {
                        return 1;
                    }
                    else {
                        return -1;
                    }
                })
                .get();
    }

    @Override
    public synchronized void run() {
        while (true) {
            while (BazaPodataka.aktivnaVezaSBazomPodataka) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Thread.currentThread().setName("Dretva za dohvaćanje županije s najviše zaraženih");

            BazaPodataka.aktivnaVezaSBazomPodataka = true;

            Zupanija zupanijaSNaviseZarazenih = dohvatiZupanijuSNajviseZarazenih();
            double postotakZarazenosti = (double) zupanijaSNaviseZarazenih.getBrojZarazenih() /
                    zupanijaSNaviseZarazenih.getBrojStanovnika() * 100;
            System.out.print("Županija s najviše zaraženih: " + zupanijaSNaviseZarazenih.getNaziv() + " -> ");
            System.out.printf("%.2f%%\n", postotakZarazenosti);

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(10), e -> {
                        Main.getMainStage().setTitle(zupanijaSNaviseZarazenih.getNaziv());
                    })
            );
            timeline.play();

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            BazaPodataka.aktivnaVezaSBazomPodataka = false;
            notifyAll();
        }
    }
}
