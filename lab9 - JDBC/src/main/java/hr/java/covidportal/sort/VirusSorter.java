package main.java.hr.java.covidportal.sort;

import main.java.hr.java.covidportal.model.Virus;

import java.util.Comparator;

public class VirusSorter implements Comparator<Virus> {
    @Override
    public int compare(Virus o1, Virus o2) {
        if(o1.getNaziv().compareTo(o2.getNaziv()) < 0){
            return 1;
        }
        else if(o1.getNaziv().compareTo(o2.getNaziv()) > 0){
            return -1;
        }
        else{
            return 0;
        }
    }
}
