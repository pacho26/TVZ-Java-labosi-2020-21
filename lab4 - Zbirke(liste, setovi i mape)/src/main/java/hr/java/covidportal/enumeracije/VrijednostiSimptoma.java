package hr.java.covidportal.enumeracije;

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