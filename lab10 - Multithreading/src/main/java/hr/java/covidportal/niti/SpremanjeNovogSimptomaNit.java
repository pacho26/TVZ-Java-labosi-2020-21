package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.database.BazaPodataka;
import main.java.hr.java.covidportal.model.Simptom;

import java.sql.SQLException;

public class SpremanjeNovogSimptomaNit implements Runnable{
    private Simptom simptom;

    public SpremanjeNovogSimptomaNit(Simptom simptom) {
        this.simptom = simptom;
    }

    @Override
    public synchronized void run() {
        while(BazaPodataka.aktivnaVezaSBazomPodataka) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Thread.currentThread().setName("Dretva za dodavanje novog simptoma");

            BazaPodataka.aktivnaVezaSBazomPodataka = true;

            try {
                BazaPodataka.spremanjeNovogSimptoma(simptom);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        BazaPodataka.aktivnaVezaSBazomPodataka = false;
        notifyAll();
    }
}
