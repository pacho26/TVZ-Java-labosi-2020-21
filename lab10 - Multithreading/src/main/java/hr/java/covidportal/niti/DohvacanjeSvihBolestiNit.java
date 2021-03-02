package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.database.BazaPodataka;
import main.java.hr.java.covidportal.model.Bolest;

import java.util.List;
import java.util.concurrent.Callable;

public class DohvacanjeSvihBolestiNit implements Callable<List<Bolest>> {

    @Override
    public synchronized List<Bolest> call() throws Exception {
        Thread.currentThread().setName("Dretva za dohvaćanje bolesti i virusa");
        while (BazaPodataka.aktivnaVezaSBazomPodataka) {
            wait();
        }
        BazaPodataka.aktivnaVezaSBazomPodataka = true;
        List<Bolest> listaBolesti = BazaPodataka.dohvacanjeSvihBolesti();

        BazaPodataka.aktivnaVezaSBazomPodataka = false;
        notifyAll();

        return listaBolesti;
    }
}
