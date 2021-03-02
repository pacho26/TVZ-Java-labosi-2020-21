package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.database.BazaPodataka;
import main.java.hr.java.covidportal.model.Virus;

import java.sql.SQLException;

public class SpremanjeNovogVirusaNit implements Runnable{
    private Virus virus;
    private final Boolean jeVirus;

    public SpremanjeNovogVirusaNit(Virus virus, Boolean jeVirus) {
        this.virus = virus;
        this.jeVirus = jeVirus;
    }

    @Override
    public synchronized void run() {
        while(BazaPodataka.aktivnaVezaSBazomPodataka) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Thread.currentThread().setName("Dretva za dodavanje novog virusa");

            BazaPodataka.aktivnaVezaSBazomPodataka = true;

            try {
                BazaPodataka.spremanjeNoveBolesti(virus, jeVirus);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        BazaPodataka.aktivnaVezaSBazomPodataka = false;
        notifyAll();
    }
}
