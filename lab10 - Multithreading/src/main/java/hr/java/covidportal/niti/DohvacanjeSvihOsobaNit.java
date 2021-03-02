package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.database.BazaPodataka;
import main.java.hr.java.covidportal.model.Osoba;

import java.util.List;
import java.util.concurrent.Callable;

public class DohvacanjeSvihOsobaNit implements Callable<List<Osoba>> {

    @Override
    public synchronized List<Osoba> call() throws Exception {
        Thread.currentThread().setName("Dretva za dohvaćanje osoba");
        while (BazaPodataka.aktivnaVezaSBazomPodataka) {
            wait();
        }
        BazaPodataka.aktivnaVezaSBazomPodataka = true;
        List<Osoba> listaOsoba = BazaPodataka.dohvatiSveOsobe();

        BazaPodataka.aktivnaVezaSBazomPodataka = false;
        notifyAll();

        return listaOsoba;
    }
}
