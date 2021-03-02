package main.java.hr.java.covidportal.enumeracije;

/**
 * Koristi se za određivanje vrijednosti pojedinog simptoma.
 */
public enum VrijednostiSimptoma {
    RIJETKO("RIJETKO"),
    SREDNJE("SREDNJE"),
    CESTO("ČESTO");

    private final String vrijednost;

    VrijednostiSimptoma(String vrijednost) {
        this.vrijednost = vrijednost;
    }

    public String getVrijednost() {
        return vrijednost;
    }
}