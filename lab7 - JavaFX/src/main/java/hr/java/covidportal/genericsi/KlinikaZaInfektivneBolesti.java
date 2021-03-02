package main.java.hr.java.covidportal.genericsi;

import main.java.hr.java.covidportal.model.Osoba;
import main.java.hr.java.covidportal.model.Virus;

import java.util.ArrayList;
import java.util.List;

public class KlinikaZaInfektivneBolesti<T extends Virus, S extends Osoba> {
    private List<T> listaVirusa;
    private List<S> listaZarazenihOsoba;

    public KlinikaZaInfektivneBolesti(List<T> listaVirusa, List<S> listaZarazenihOsoba) {
        this.listaVirusa = listaVirusa;
        this.listaZarazenihOsoba = listaZarazenihOsoba;
    }

    public KlinikaZaInfektivneBolesti(){
        this(10);
    }

    public KlinikaZaInfektivneBolesti(int capacity) {
        int initCapacity = capacity > 0 ? capacity : 10;
        listaVirusa = new ArrayList<T>(initCapacity);
        listaZarazenihOsoba = new ArrayList<S>(initCapacity);
    }

    public void dodavanjeNovogVirusa(T v) {
        listaVirusa.add(v);
    }

    public void dodavanjeNoveOsobe(S o){
        listaZarazenihOsoba.add(o);
    }

    public List<T> getListaVirusa() {
        return listaVirusa;
    }

    public List<S> getListaZarazenihOsoba() {
        return listaZarazenihOsoba;
    }
}
