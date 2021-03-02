package hr.java.covidportal.main;

import hr.java.covidportal.enumeracije.VrijednostiSimptoma;
import hr.java.covidportal.genericsi.KlinikaZaInfektivneBolesti;
import hr.java.covidportal.model.*;
import hr.java.covidportal.sort.CovidSorter;
import hr.java.covidportal.sort.VirusSorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Koristi se za evidentiranje bolesti kod određenog broja osoba.
 */

public class Glavna{

    private static final Logger logger = LoggerFactory.getLogger(Glavna.class);
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    /**
     * Koristi se za pokretanje programa koji će od korisnika tražiti unos županija, bolesti(i njihovih simptoma) i
     * podataka o osobama te će ne kraju ispisati sve unesene podatke.
     *
     * @param args argumenti komandne linije(ne koriste se)
     */
    public static void main(String[] args) throws IOException{

        logger.info("Program je uspješno pokrenut.");

        //---------------------------------------2. ZADATAK: čitanje i ispis------------------------------
        Path tekstualnaDatoteka = Path.of("dat/tekstualna.txt");
        CitanjeIspisDrugiZadatak(tekstualnaDatoteka);

        System.out.println();
        Scanner unos = new Scanner(System.in);

        List<Zupanija> zupanije = new ArrayList<>();
        List<Simptom> simptomi = new ArrayList<>();
        List<Bolest> bolesti = new ArrayList<>();
        List<Osoba> osobe = new ArrayList<>();
        Map<Bolest, List<Osoba>> mapaBolesti = new HashMap<>();
        CitanjeZupanija(zupanije);
        zupanije.sort(new CovidSorter());

        CitanjeSimptoma(simptomi);

        CitanjeBolesti(simptomi, bolesti);

        CitanjeVirusa(simptomi, bolesti);

        CitanjeOsoba(zupanije, bolesti, osobe);

        KlinikaZaInfektivneBolesti<Virus, Osoba> klinikaUnesenihVirusa = new KlinikaZaInfektivneBolesti<>();
        for (Bolest bolest : bolesti) {
            if (bolest instanceof Virus) {
                klinikaUnesenihVirusa.dodavanjeNovogVirusa((Virus) bolest);
            }
        }
        List<Virus> virusi = klinikaUnesenihVirusa.getListaVirusa().stream().collect(Collectors.toList());

        KlinikaZaInfektivneBolesti<Virus, Osoba> klinikaOsobaZarazenihVirusom = new KlinikaZaInfektivneBolesti<>();
        for (Osoba osoba : osobe) {
            if (osoba.getZarazenBolescu() instanceof Virus) {
                klinikaOsobaZarazenihVirusom.dodavanjeNoveOsobe(osoba);
            }
        }

        PopunjavanjeMapeBolesti(bolesti, osobe, mapaBolesti);

        Ispis(osobe, mapaBolesti, zupanije);

        SortiranjeMjerenjeIspisVirusa(virusi);

        FiltriranjePoPrezimenu(unos, osobe);

        MapiranjeVirusaIBrojaSimptoma(bolesti);

        SerijalizacijaFiltriranihZupanija(zupanije);

        DeserijalizacijaFiltriranihZupanija();

        //-------------------------------------------1. ZADATAK-------------------------------------------
        PrviZadatak(bolesti);

        //-------------------------------------------2. ZADATAK: pisanje----------------------------------
        PisanjeDrugiZadatak(tekstualnaDatoteka, bolesti);
    }

    /**
     * Služi za zapisivanje svih potrebnih podataka u tekstualnu datoteku
     *
     * @param tekstualnaDatoteka datoteka u koju se zapisuje
     * @param bolesti lista svih bolesti i virusa
     */
    private static void PisanjeDrugiZadatak(Path tekstualnaDatoteka, List<Bolest> bolesti) {
        Integer sumaNazivaBolesti = bolesti.stream()
                .filter(b -> !(b instanceof Virus))
                .mapToInt(b -> b.getNaziv().length())
                .sum();

        Double prosjekNazivaBolesti = bolesti.stream()
                .mapToDouble(b -> b.getNaziv().length()).average()
                .getAsDouble();

        Bolest najkraciNazivBolesti = bolesti.stream()
                .min(Comparator.comparing(Bolest::getNaziv))
                .get();

        Bolest najduziNazivBolesti = bolesti.stream()
                .max(Comparator.comparing(Bolest::getNaziv))
                .get();

        try{
            Files.writeString(tekstualnaDatoteka, "Suma svih duljina naziva bolesti: " +
                    sumaNazivaBolesti.toString());
            Files.writeString(tekstualnaDatoteka, "\nProsječna duljina svih naziva bolesti: " +
                    prosjekNazivaBolesti.toString(), StandardOpenOption.APPEND);
            Files.writeString(tekstualnaDatoteka, "\nBolest s najkraćim nazivom: " +
                    najkraciNazivBolesti.toString(), StandardOpenOption.APPEND);
            Files.writeString(tekstualnaDatoteka, "\nBolest s najdužim nazivom: " +
                    najduziNazivBolesti.toString(), StandardOpenOption.APPEND);
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    /**
     * Služi za serijalizaciju svih filtriranih bolesti te za deserijalizaciju
     *
     * @param bolesti lista svih bolesti
     */
    private static void PrviZadatak(List<Bolest> bolesti) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("dat/filtriraneBolesti.dat"))) {
            out.writeObject(bolesti.stream()
                    .filter(b -> b.getNaziv().toLowerCase().startsWith("lj")
                            && b.getNaziv().toLowerCase().contains("gitis") && !(b instanceof Virus))
                    .collect(Collectors.toList()));
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        List <Bolest> procitaneFiltriraneBolesti = new ArrayList<>();
        if(Files.exists(Path.of("dat/filtriraneBolesti.dat"))){
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("dat/filtriraneBolesti.dat"))) {
                procitaneFiltriraneBolesti = (List<Bolest>) in.readObject();
                System.out.println("\nFiltrirane bolesti:");
                procitaneFiltriraneBolesti.stream().forEach(System.out::println);
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
            catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Služi za čitanje i ispis stringa pročitanog u tekstualnoj datoteci
     *
     * @param tekstualnaDatoteka datoteka iz koje se čita
     */
    private static void CitanjeIspisDrugiZadatak(Path tekstualnaDatoteka) {
        if (Files.exists(Path.of("dat/tekstualna.txt"))) {
            try {
                String tekstualniZapisDatoteke = Files.readString(tekstualnaDatoteka);
                System.out.println("Ovo je sadržaj tekstualne datoteke:\n" + tekstualniZapisDatoteke);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void DeserijalizacijaFiltriranihZupanija() {
        try (ObjectInputStream ulaz = new ObjectInputStream(new FileInputStream("dat/serijalizirani.dat"))) {
            List<Zupanija> procitaneZupanije = (List<Zupanija>) ulaz.readObject();

            System.out.println("\nDeserijalizacija županija s postotkom zaraženih stanovnika većim od 2%: ");
            for (Zupanija zupanija : procitaneZupanije) {
                System.out.println(zupanija.getNaziv() + " -> " +
                        (zupanija.getBrojZarazenih().doubleValue() / zupanija.getBrojStanovnika() * 100) + "% zaraženih");
            }
        }
        catch (IOException ex) {
            System.err.println(ex);
        }
        catch (ClassNotFoundException ex) {
            System.err.println(ex);
        }
    }

    private static void SerijalizacijaFiltriranihZupanija(List<Zupanija> zupanije) {
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("dat/serijalizirani.dat"))){
            out.writeObject(zupanije.stream()
                    .filter(z -> (z.getBrojZarazenih().doubleValue() / z.getBrojStanovnika()) * 100 > 2)
                    .collect(Collectors.toList()));
        }
        catch (IOException ex){
            System.err.println(ex);
        }
    }

    private static void CitanjeOsoba(List<Zupanija> zupanije, List<Bolest> bolesti, List<Osoba> osobe) {
        File osobeFile = new File("dat/osobe.txt");
        try (BufferedReader in = new BufferedReader(new FileReader(osobeFile))){
            System.out.println("Učitavanje podataka o osobama…");
            String procitanaLinija;
            while((procitanaLinija = in.readLine()) != null){
                Long idOsobe = Long.parseLong(procitanaLinija);
                String imeOsobe = in.readLine();
                String prezimeOsobe = in.readLine();
                Integer starostOsobe = Integer.parseInt(in.readLine());
                Long idZupanije = Long.parseLong(in.readLine());
                Zupanija tempZupanija = null;
                for (Zupanija zupanija : zupanije) {
                    if (zupanija.getId().equals(idZupanije)) {
                        tempZupanija = zupanija;
                    }
                }
                Long idBolestiIliVirusa = Long.parseLong(in.readLine());
                Bolest tempBolest = null;
                for (Bolest bolest : bolesti) {
                    if (bolest.getId().equals(idBolestiIliVirusa)) {
                        tempBolest = bolest;
                    }
                }
                String stringKontakata = in.readLine();
                List<Osoba> tempKontaktiranihOsoba = new ArrayList<>();
                String[] kontaktiOsobe = stringKontakata.split(",");
                for (String s : kontaktiOsobe) {
                    for (Osoba osoba : osobe) {
                        if (osoba.getId().toString().equals(s)) {
                            tempKontaktiranihOsoba.add(osoba);
                        }
                    }
                }
                osobe.add(new Osoba.Builder()
                        .withId(idOsobe)
                        .withIme(imeOsobe)
                        .withPrezime(prezimeOsobe)
                        .withStarost(starostOsobe)
                        .withZupanija(tempZupanija)
                        .withZarazenBolescu(tempBolest)
                        .withKontaktiraneOsobe(tempKontaktiranihOsoba)
                        .build());
            }
            logger.info("Podaci o osobama su uspješno pročitani.");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void CitanjeVirusa(List<Simptom> simptomi, List<Bolest> bolesti) {
        File virusiFile = new File("dat/virusi.txt");
        try (BufferedReader in = new BufferedReader(new FileReader(virusiFile))) {
            System.out.println("Učitavanje podataka o virusima…");
            String procitanaLinija;
            while ((procitanaLinija = in.readLine()) != null) {
                Long idVirusa = Long.parseLong(procitanaLinija);
                String nazivVirusa = in.readLine();
                String stringSimptomaVirusa = in.readLine();
                String[] simptomiVirusa = stringSimptomaVirusa.split(",");
                Set<Simptom> setSimptomaVirusa = new HashSet();
                for (int i = 0; i < simptomiVirusa.length; i++) {
                    for(int j = 0; j < simptomi.size(); j++){
                        if(simptomi.get(j).getId().toString().equals(simptomiVirusa[i])){
                            setSimptomaVirusa.add(simptomi.get(j));
                        }
                    }
                }
                bolesti.add(new Virus(idVirusa, nazivVirusa, setSimptomaVirusa));

            }
            logger.info("Podaci o virusima su uspješno pročitani.");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void CitanjeBolesti(List<Simptom> simptomi, List<Bolest> bolesti) {
        File bolestiFile = new File("dat/bolesti.txt");
        try (BufferedReader in = new BufferedReader(new FileReader(bolestiFile))) {
            System.out.println("Učitavanje podataka o bolestima…");
            String procitanaLinija;
            while ((procitanaLinija = in.readLine()) != null) {
                Long idBolesti = Long.parseLong(procitanaLinija);
                String nazivBolesti = in.readLine();
                String stringSimptomaBolesti = in.readLine();
                String[] simptomiBolesti = stringSimptomaBolesti.split(",");
                Set<Simptom> setSimptomaBolesti = new HashSet();
                for (String s : simptomiBolesti) {
                    for (Simptom simptom : simptomi) {
                        if (simptom.getId().toString().equals(s)) {
                            setSimptomaBolesti.add(simptom);
                        }
                    }
                }
                bolesti.add(new Bolest(nazivBolesti, idBolesti, setSimptomaBolesti));
            }
            logger.info("Podaci o bolestima su uspješno pročitani.");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void CitanjeSimptoma(List<Simptom> simptomi) {
        File simptomiFile = new File("dat/simptomi.txt");
        try (BufferedReader in = new BufferedReader(new FileReader(simptomiFile))) {
            System.out.println("Učitavanje podataka o simptomima…");
            String procitanaLinija;
            while ((procitanaLinija = in.readLine()) != null) {
                Long idSimptoma = Long.parseLong(procitanaLinija);
                String nazivSimptoma = in.readLine();
                VrijednostiSimptoma vrijednostSimptoma = VrijednostiSimptoma.valueOf(in.readLine());
                simptomi.add(new Simptom(idSimptoma, nazivSimptoma, vrijednostSimptoma));
            }
            logger.info("Podaci o simptomima su uspješno pročitani.");
        }
        catch (IOException ex) {
            System.err.println(ex);
        }
    }

    private static void CitanjeZupanija(List<Zupanija> zupanije) {
        File zupanijeFile = new File("dat/zupanije.txt");
        try (BufferedReader in = new BufferedReader(new FileReader(zupanijeFile))) {
            System.out.println("Učitavanje podataka o županijama…");
            String procitanaLinija;
            while ((procitanaLinija = in.readLine()) != null) {
                Long idZupanije = Long.parseLong(procitanaLinija);
                String nazivZupanije = in.readLine();
                Integer brojStanovnika = Integer.parseInt(in.readLine());
                Integer brojZarazenih = Integer.parseInt(in.readLine());
                zupanije.add(new Zupanija(nazivZupanije, idZupanije, brojStanovnika, brojZarazenih));
            }
            logger.info("Podaci o županijama su uspješno pročitani.");
        }
        catch (IOException ex) {
            System.err.println(ex);
        }
    }

    private static void MapiranjeVirusaIBrojaSimptoma(List<Bolest> bolesti) {
        System.out.println("\nPopis bolesti/virusa i njihov broj simptoma: ");
        bolesti.stream()
                .map(bolest -> bolest.getNaziv() + " -> " + bolest.getSimptomi().size() + " simptoma")
                .forEach(System.out::println);
    }

    private static void FiltriranjePoPrezimenu(Scanner unos, List<Osoba> osobe) {
        System.out.print("\nUnesite string za pretragu po prezimenu: ");
        String zaPretragu = unos.nextLine();
        String zaPretraguLowerCase = zaPretragu.toLowerCase();
        List<Osoba> filtiraneOsobe = Optional.of(osobe.stream()
                .filter(p -> p.getPrezime().toLowerCase().contains(zaPretraguLowerCase))
                .collect(Collectors.toList()))
                .orElse(null);
        if (filtiraneOsobe.isEmpty()) {
            System.out.println("Nema osoba koje u prezimenu sadrže '" + zaPretragu + "'.");
        } else {
            System.out.println("Osobe čije prezime sadrži '" + zaPretragu + "' su: ");
            for (Osoba osoba : filtiraneOsobe) {
                System.out.println(osoba);
            }
        }
    }

    private static void SortiranjeMjerenjeIspisVirusa(List<Virus> virusi) {
        Instant start = Instant.now();
        List<Virus> listaVirusaSortiranjeLambda = virusi.stream()
                .sorted(Comparator.comparing(ImenovaniEntitet::getNaziv).reversed())
                .collect(Collectors.toList());
        Instant end = Instant.now();
        System.out.println("\nPopis svih virusa(sortirano silazno po abecedi):");
        listaVirusaSortiranjeLambda.stream().forEach(System.out::println);
        System.out.println("Vrijeme sortiranje S LAMBDOM: " + Duration.between(start, end).toMillis() + " ms");

        start = Instant.now();
        virusi.sort(new VirusSorter());
        end = Instant.now();
        System.out.println("Vrijeme sortiranja BEZ LAMBDE: " + Duration.between(start, end).toMillis() + " ms");
    }

    /**
     * Popunjava <code>for each</code> petljom svaki <code>key</code> (pritom stvara i novi listu kao
     * <code>value</code> tog <code>key</code>-a. U drugoj <code>for each</code> petlji se te liste popunjavaju
     * osobama koje imaju bolest/virus isti kao <code>key</code> tog elementa mape.
     *
     * @param bolesti     set koji sadrži sve unesene bolesti
     * @param osobe       lista koja sadrži sve unesene osobe
     * @param mapaBolesti mapa koju popunjujemo u ovoj metodi
     */
    private static void PopunjavanjeMapeBolesti(List<Bolest> bolesti, List<Osoba> osobe,
                                                Map<Bolest, List<Osoba>> mapaBolesti) {
        for (Bolest bolest : bolesti) {
            mapaBolesti.put(bolest, new ArrayList<>());
        }

        for (Osoba osoba : osobe) {
            if (mapaBolesti.containsKey(osoba.getZarazenBolescu())) {
                mapaBolesti.get(osoba.getZarazenBolescu()).add(osoba);
            }
        }
    }

    /**
     * Ispisuje sve podatke svih unesenih osoba(ime i prezime, starost, županiju prebivališta, bolest te osobe koje su
     * bile s njom u kontaktu).
     *
     * @param osobe       lista koje sadrži sve unesene osobe sa svim njihovim podacima
     * @param mapaBolesti mapa koja kao <code>key</code> ima sve unesene bolesti, a kao <code>value</code> ima listu
     *                    osoba koje boluju od te bolesti ili virusa
     * @param zupanije    sortirani set svih županija, sortiran po postotku broja zaraženih
     */
    private static void Ispis(List<Osoba> osobe, Map<Bolest, List<Osoba>> mapaBolesti,
                              List<Zupanija> zupanije) {
        System.out.println("\n-------------POPIS OSOBA-------------");
        for (int i = 0; i < osobe.size(); i++) {
            System.out.println("\n" + (i + 1) + ". OSOBA:");
            System.out.println("Ime i prezime: " + osobe.get(i).getIme() + " " + osobe.get(i).getPrezime());
            System.out.println("Starost: " + osobe.get(i).getStarost());
            System.out.println("Županija prebivališta: " + osobe.get(i).getZupanija().getNaziv());
            System.out.println("Zaražen bolešću: " + osobe.get(i).getZarazenBolescu().getNaziv());
            if (i == 0 || osobe.get(i).getKontaktiraneOsobe().size() == 0) {
                System.out.println("Nema kontaktiranih osoba.");
            } else {
                System.out.println("Kontaktirane osobe:");
                for (int j = 0; j < osobe.get(i).getKontaktiraneOsobe().size(); j++) {
                    System.out.println("- " + osobe.get(i).getKontaktiraneOsobe().get(j).getIme() + " " +
                            osobe.get(i).getKontaktiraneOsobe().get(j).getPrezime());
                }
            }
        }
        System.out.println("-------------------------------------");

        System.out.print("\n");
        mapaBolesti.entrySet().forEach(bolest -> {
            if (bolest.getValue().size() > 0) {
                if (bolest.getKey() instanceof Virus) {
                    System.out.print("Od virusa " + bolest.getKey().getNaziv() + " boluje: ");
                } else {
                    System.out.print("Od bolesti " + bolest.getKey().getNaziv() + " boluje: ");
                }
                for (int i = 0; i < bolest.getValue().size(); i++) {
                    System.out.print(bolest.getValue().get(i).getIme() + " " + bolest.getValue().get(i).getPrezime());
                    if (i != (bolest.getValue().size() - 1)) {
                        System.out.print(", ");
                    }
                }
                System.out.print("\n");
            }
        });

        double prosjekZarazenih = ((double) zupanije.get(zupanije.size() - 1).getBrojZarazenih() /
                zupanije.get(zupanije.size() - 1).getBrojStanovnika() * 100);
        System.out.println("Županija sa najviše zaraženih osoba: " + zupanije.get(zupanije.size() - 1).getNaziv() + " (" +
                df2.format(prosjekZarazenih) + "%)");
    }
}