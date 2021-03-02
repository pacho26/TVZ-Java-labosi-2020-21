package main.java.hr.java.covidportal.sort;

import main.java.hr.java.covidportal.model.Zupanija;

import java.util.Comparator;

/**
 * Sortira županije po postotku broja zaraženih, koji se unutar <code>compare</code> funkcije izračunavaju
 * te uspoređuju
 */
public class CovidSorter implements Comparator<Zupanija> {
    @Override
    public int compare(Zupanija z1, Zupanija z2){
        double prosjek1 = (double)z1.getBrojZarazenih() / z1.getBrojStanovnika();
        double prosjek2 = (double)z2.getBrojZarazenih() / z2.getBrojStanovnika();
        if(prosjek1 > prosjek2){
            return 1;
        }
        else if(prosjek1 < prosjek2){
            return -1;
        }
        else{
            return 0;
        }
    }
}
