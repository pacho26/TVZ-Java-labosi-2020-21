package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.database.BazaPodataka;
import main.java.hr.java.covidportal.model.Zupanija;

import java.util.List;
import java.util.concurrent.Callable;

public class DohvacanjeSvihZupanijaNit implements Callable<List<Zupanija>> {

    @Override
    public synchronized List<Zupanija> call() throws Exception {
        Thread.currentThread().setName("Dretva za dohvaćanje županija");
        while(BazaPodataka.aktivnaVezaSBazomPodataka) {
            wait();
        }
        BazaPodataka.aktivnaVezaSBazomPodataka = true;
        List<Zupanija> listaZupanija = BazaPodataka.dohvacanjeSvihZupanija();

        BazaPodataka.aktivnaVezaSBazomPodataka = false;
        notifyAll();

        return listaZupanija;
    }
}
