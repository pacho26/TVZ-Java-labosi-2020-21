package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.database.BazaPodataka;
import main.java.hr.java.covidportal.model.Osoba;

import java.sql.SQLException;

public class SpremanjeNoveOsobeNit implements Runnable{

    private Osoba osoba;

    public SpremanjeNoveOsobeNit(Osoba osoba) {
        this.osoba = osoba;
    }

    @Override
    public synchronized void run() {
        while (BazaPodataka.aktivnaVezaSBazomPodataka) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Thread.currentThread().setName("Dretva za dodavanje nove osobe");

            BazaPodataka.aktivnaVezaSBazomPodataka = true;

            try {
                BazaPodataka.spremanjeNoveOsobe(osoba);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        BazaPodataka.aktivnaVezaSBazomPodataka = false;
        notifyAll();
    }
}
