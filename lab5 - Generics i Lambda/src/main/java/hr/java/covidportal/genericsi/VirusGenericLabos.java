package hr.java.covidportal.genericsi;

import hr.java.covidportal.model.Virus;

import java.util.ArrayList;
import java.util.List;

public class VirusGenericLabos<T extends Virus> {
    private List<T> virusiGenericsLista;

    public VirusGenericLabos(List<T> virusiGeneric) {
        this.virusiGenericsLista = virusiGeneric;
    }

    public VirusGenericLabos(){
        this(10);
    }

    public VirusGenericLabos(int kapacitet){
        int postaviKapacitet = kapacitet > 0 ? kapacitet : 10;
        virusiGenericsLista = new ArrayList<>(postaviKapacitet);
    }

    public List<T> getVirusiGenerics() {
        return virusiGenericsLista;
    }

    public void setVirusiGenericsLista(List<T> virusiGenericsLista) {
        this.virusiGenericsLista = virusiGenericsLista;
    }

    public void add(T v){
        virusiGenericsLista.add(v);
    }

    public Virus get(int index){
        return virusiGenericsLista.get(index);
    }

    public void brisiPosljednjeg(){
        virusiGenericsLista.remove(virusiGenericsLista.get(virusiGenericsLista.size() - 1));
    }

    @Override
    public String toString() {
        return "VirusGenericLabos{" +
                "virusiGenericsLista=" + virusiGenericsLista +
                '}';
    }
}
