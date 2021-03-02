package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.database.BazaPodataka;

import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.Callable;

public class BrojVirusaNit implements Callable<String> {

    @Override
    public synchronized String call() throws Exception {
        Integer brojBolesti = 0;

        while (BazaPodataka.aktivnaVezaSBazomPodataka) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        BazaPodataka.aktivnaVezaSBazomPodataka = true;

        LocalDate pocetak = LocalDate.now();
        try {
            brojBolesti = BazaPodataka.dohvacanjeUkupnogBrojaBolesti();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        LocalDate kraj = LocalDate.now();

        Long razlika = ChronoUnit.SECONDS.between(pocetak, kraj);
        Date result = new Date(razlika);
        String datum = sdf.format(result);
        BazaPodataka.aktivnaVezaSBazomPodataka = false;
        notifyAll();

        String zavrsni = brojBolesti.toString() + " " + datum;

        return zavrsni;
    }
}
