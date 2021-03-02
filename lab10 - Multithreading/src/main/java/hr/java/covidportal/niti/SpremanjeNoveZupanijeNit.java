package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.database.BazaPodataka;
import main.java.hr.java.covidportal.model.Zupanija;

import java.sql.SQLException;

public class SpremanjeNoveZupanijeNit implements Runnable{
    private Zupanija zupanija;

    public SpremanjeNoveZupanijeNit(Zupanija zupanija) {
        this.zupanija = zupanija;
    }

    @Override
    public synchronized void run() {
        while (BazaPodataka.aktivnaVezaSBazomPodataka) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Thread.currentThread().setName("Dretva za dodavanje nove županije");

            BazaPodataka.aktivnaVezaSBazomPodataka = true;

            try {
                BazaPodataka.spremanjeNoveZupanije(zupanija);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        BazaPodataka.aktivnaVezaSBazomPodataka = false;
        notifyAll();
    }
}
