package main.java.hr.java.covidportal.enumeracije;

/**
 * Koristi se za određivanje vrijednosti pojedinog simptoma.
 */
public enum VrijednostiSimptoma {
    Produktivni("Produktivni"),
    Intenzivno("Intenzivno"),
    Visoka("Visoka"),
    Jaka("Jaka");

    private final String vrijednost;

    VrijednostiSimptoma(String vrijednost) {
        this.vrijednost = vrijednost;
    }

    public String getVrijednost() {
        return vrijednost;
    }

    @Override
    public String toString() {
        return getVrijednost();
    }
}