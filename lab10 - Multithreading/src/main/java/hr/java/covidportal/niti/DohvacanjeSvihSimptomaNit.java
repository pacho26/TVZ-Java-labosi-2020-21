package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.database.BazaPodataka;
import main.java.hr.java.covidportal.model.Simptom;

import java.util.List;
import java.util.concurrent.Callable;

public class DohvacanjeSvihSimptomaNit implements Callable<List<Simptom>> {

    @Override
    public synchronized List<Simptom> call() throws Exception {
        Thread.currentThread().setName("Dretva za dohvaćanje simptoma");
        while(BazaPodataka.aktivnaVezaSBazomPodataka) {
            wait();
        }
        BazaPodataka.aktivnaVezaSBazomPodataka = true;
        List<Simptom> listaSimptoma = BazaPodataka.dohvacanjeSvihSimptoma();

        BazaPodataka.aktivnaVezaSBazomPodataka = false;
        notifyAll();

        return listaSimptoma;
    }
}
