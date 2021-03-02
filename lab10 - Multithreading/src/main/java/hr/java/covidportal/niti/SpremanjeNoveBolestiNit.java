package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.database.BazaPodataka;
import main.java.hr.java.covidportal.model.Bolest;

import java.sql.SQLException;

public class SpremanjeNoveBolestiNit implements Runnable{
    private Bolest bolest;
    private final Boolean jeVirus;

    public SpremanjeNoveBolestiNit(Bolest bolest, Boolean jeVirus) {
        this.bolest = bolest;
        this.jeVirus = jeVirus;

    }

    @Override
    public synchronized void run() {
        while (BazaPodataka.aktivnaVezaSBazomPodataka) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Thread.currentThread().setName("Dretva za dodavanje nove bolesti");

            BazaPodataka.aktivnaVezaSBazomPodataka = true;

            try {
                BazaPodataka.spremanjeNoveBolesti(bolest, jeVirus);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        BazaPodataka.aktivnaVezaSBazomPodataka = false;
        notifyAll();
    }
}
