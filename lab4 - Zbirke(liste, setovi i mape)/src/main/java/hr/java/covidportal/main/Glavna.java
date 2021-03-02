package hr.java.covidportal.main;

import hr.java.covidportal.enumeracije.VrijednostiSimptoma;
import hr.java.covidportal.iznimke.BolestIstihSimptoma;
import hr.java.covidportal.iznimke.DuplikatorKontaktiraneOsobe;
import hr.java.covidportal.iznimke.PremalenIndeksKontakta;
import hr.java.covidportal.iznimke.PrevelikIndeksKontakta;
import hr.java.covidportal.model.*;
import hr.java.covidportal.sort.CovidSorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Koristi se za evidentiranje bolesti kod određenog broja osoba.
 */

public class Glavna {

    private static final Logger logger = LoggerFactory.getLogger(Glavna.class);
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    /**
     * Koristi se za pokretanje programa koji će od korisnika tražiti unos županija, bolesti(i njihovih simptoma) i
     * podataka o osobama te će ne kraju ispisati sve unesene podatke.
     *
     * @param args argumenti komandne linije(ne koriste se)
     */
    public static void main(String[] args) {

        logger.info("Program je uspješno pokrenut.");

        Scanner unos = new Scanner(System.in);

        SortedSet<Zupanija> zupanije = new TreeSet<>(new CovidSorter());
        Set<Simptom> simptomi = new HashSet<>();
        Set<Bolest> bolesti = new HashSet<>();
        List<Osoba> osobe = new ArrayList<>();
        Map<Bolest, List<Osoba>> mapaBolesti = new HashMap<>();

        System.out.print("Unesite broj županija koje želite unijeti: ");
        Integer brojZupanija = unos.nextInt();
        unos.nextLine();
        System.out.println("UNOS ŽUPANIJA");
        for (int i = 0; i < brojZupanija; i++) {
            zupanije.add(unesiZupaniju(unos, i));
        }
        logger.info("Sve županije su uspješno unesene.");


        System.out.print("\nUnesite broj simptoma koje želite unijeti: ");
        Integer brojSimptoma = unos.nextInt();
        unos.nextLine();
        System.out.println("UNOS SIMPTOMA");
        for (int i = 0; i < brojSimptoma; i++) {
            simptomi.add(unesiSimptom(unos, i));
        }
        logger.info("Svi simptomi su uspješno uneseni.");


        System.out.print("\nUnesite broj bolesti i virusa koje želite unijeti: ");
        Integer brojBolesti = unos.nextInt();
        unos.nextLine();
        System.out.println("UNOS BOLESTI/VIRUSA");
        for (int i = 0; i < brojBolesti; i++) {
            Boolean ponavljanjeSimptoma = true;
            do {
                try {
                    Bolest tmp = unesiBolest(unos, simptomi, i, brojSimptoma);
                    bolesti.add(tmp);
                    List<Bolest> listaBolesti = new ArrayList<>(bolesti);
                    provjeraSimptoma(listaBolesti, tmp, i);
                    ponavljanjeSimptoma = false;
                } catch (BolestIstihSimptoma ex) {
                    System.out.println(ex.getMessage());
                    logger.error("Već je unesena bolest sa istim simptomima!", ex);
                }
            } while (ponavljanjeSimptoma);
        }
        logger.info("Sve bolesti su uspješno unesene.");


        System.out.print("\nUnesite broj osoba koje želite unijeti: ");
        Integer brojOsoba = unos.nextInt();
        unos.nextLine();
        System.out.println("UNOS OSOBA");
        for (int i = 0; i < brojOsoba; i++) {
            osobe.add(unesiOsobu(unos, zupanije, bolesti, osobe, i, brojBolesti, brojZupanija));
            System.out.println("\n");
        }
        logger.error("Sve osobe su uspješno unesene.");


        PopunjavanjeMapeBolesti(bolesti, osobe, mapaBolesti);

        Ispis(osobe, brojOsoba, mapaBolesti, zupanije);

        //1.ZADATAK
        osobe.stream()
                .sorted(Comparator.comparing(Osoba::getPrezime)
                        .thenComparing(Osoba::getKorisnickoIme))
                .forEach(System.out::println);

        //2.ZADATAK
        System.out.println("\nPrva osoba po abecednom redu imena pa prezimena: ");
        System.out.println(osobe.stream()
                .min(Comparator.comparing(Osoba::getIme)
                        .thenComparing(Osoba::getPrezime))
                .get());


        System.out.println("\nZadnja osoba po abecednom redu prezimena pa imena: ");
        System.out.println(osobe.stream()
                .max(Comparator.comparing(Osoba::getPrezime)
                        .thenComparing(Osoba::getIme))
                .get());

        //3.ZADATAK
        System.out.println("\nMapa s imenom osoba kao key, a listom osoba koje imaju to ime kao value:");
        Map<String, List<Osoba>> mapaImenaOsoba = osobe.stream()
                .collect(Collectors.groupingBy(Osoba::getIme));
        System.out.println(mapaImenaOsoba);
    }

    /**
     * Popunjava <code>for each</code> petljom svaki <code>key</code> (pritom stvara i novi listu kao
     * <code>value</code> tog <code>key</code>-a. U drugoj <code>for each</code> petlji se te liste popunjavaju
     * osobama koje imaju bolest/virus isti kao <code>key</code> tog elementa mape.
     *
     * @param bolesti set koji sadrži sve unesene bolesti
     * @param osobe lista koja sadrži sve unesene osobe
     * @param mapaBolesti mapa koju popunjujemo u ovoj metodi
     */
    private static void PopunjavanjeMapeBolesti(Set<Bolest> bolesti, List<Osoba> osobe, Map<Bolest, List<Osoba>> mapaBolesti) {
        for(Bolest bolest : bolesti){
            mapaBolesti.put(bolest, new ArrayList<>());
        }

        for(Osoba osoba : osobe){
            if(mapaBolesti.containsKey(osoba.getZarazenBolescu())){
                mapaBolesti.get(osoba.getZarazenBolescu()).add(osoba);
            }
        }
    }

    /**
     * Koristi se za unos podataka određene osobe.
     *
     * @param unos     unos s tipkovnice
     * @param zupanije set koje sadrži unesene županije
     * @param bolesti  set koje sadrži unesene bolesti
     * @param osobe    lista koje sadrži unesene osobe
     * @param i        indeks iz <code>for</code> petlje
     * @return vraća novi objekt tipa <code>Osoba</code> s unesenim podacima
     */
    private static Osoba unesiOsobu(Scanner unos, Set<Zupanija> zupanije, Set<Bolest> bolesti, List<Osoba> osobe,
                                    int i, Integer brojBolesti, Integer brojZupanija) {
        System.out.println("Unesite podatke " + (i + 1) + ". osobe.");
        System.out.print("Unesite ime osobe: ");
        String ime = unos.nextLine();
        System.out.print("Unesite prezime osobe: ");
        String prezime = unos.nextLine();
        System.out.print("Unesite korisničko ime osobe: ");
        String korisnickoIme = unos.nextLine();

        Integer starost = 0;
        Boolean greska = true;
        do {
            try {
                System.out.print("Unesite starost osobe: ");
                starost = unos.nextInt();
                unos.nextLine();
                greska = false;
            } catch (InputMismatchException ex) {
                logger.error("Pogreška kod unosa brojčane vrijednosti. Unesen je string koji se ne može parsirati.",
                        ex);
                System.out.println("Zahtjeva se unos brojčane vrijednosti! Pokušajte ponovo.");
                unos.nextLine();
            }
        } while (greska);

        System.out.println("Unesite županiju prebivališta osobe: ");
        Zupanija zupanija = odaberiZupaniju(unos, zupanije, brojZupanija);

        System.out.println("Unesite bolest osobe: ");
        Bolest zarazenBolescu = odaberiBolest(unos, bolesti, brojBolesti);

        List<Osoba> kontakti = odaberiKontakte(unos, osobe, i);
        return new Osoba.Builder()
                .withIme(ime)
                .withPrezime(prezime)
                .withStarost(starost)
                .withZupanija(zupanija)
                .withZarazenBolescu(zarazenBolescu)
                .withKontaktiraneOsobe(kontakti)
                .withKorisnickoIme(korisnickoIme)
                .build();
    }

    /**
     * Koristi se za odabir osoba koje su bile u kontaktu s određenom osobom.
     *
     * @param unos  unos s tipkovnice
     * @param osobe lista koje sadrži unesene osobe
     * @param i     indeks iz <code>for</code> petlje
     * @return vraća polje tipa <code>Osoba</code> sa svim osobama s kojima je određena osoba bila u kontaktu
     */
    private static List<Osoba> odaberiKontakte(Scanner unos, List<Osoba> osobe, int i) {
        if (i == 0) {
            List<Osoba> prviZarazeni = new ArrayList<>();
            return prviZarazeni;
        } else {
            Integer brojKontaktiranihOsoba = 0;
            Boolean greska = true;
            do {
                try {
                    System.out.print("Unesite broj osoba koje su bile u kontaktu s tom osobom: ");
                    brojKontaktiranihOsoba = unos.nextInt();
                    unos.nextLine();
                    if(brojKontaktiranihOsoba > osobe.size() || brojKontaktiranihOsoba < 0){
                        System.out.println("Neispravan unos! Pokušajte ponovo.");
                        continue;
                    }
                    greska = false;
                } catch (InputMismatchException ex) {
                    logger.error("Pogreška kod unosa brojčane vrijednosti. Unesen je string koji se ne može parsirati.",
                            ex);
                    System.out.println("Zahtjeva se unos brojčane vrijednosti! Pokušajte ponovo.");
                    unos.nextLine();
                }
            } while (greska);
            List<Osoba> kontaktiraneOsobe = new ArrayList<>();
            Integer odabranaOsoba = 0;
            Boolean imaDuplikata = false, kriviIndeksKontakta = true;
            for (int j = 0; j < brojKontaktiranihOsoba; j++) {
                System.out.println("Odaberite " + (j + 1) + ". osobu koja je bila u kontaktu:");
                do {
                    for (int k = 0; k < i; k++) {
                        System.out.println((k + 1) + ". " + osobe.get(k).getIme() + " " + osobe.get(k).getPrezime());
                    }
                    greska = true;
                    do {
                        try {
                            System.out.print("Odabir: ");
                            odabranaOsoba = unos.nextInt();
                            unos.nextLine();
                            greska = false;
                        } catch (InputMismatchException ex) {
                            logger.error("Pogreška kod unosa brojčane vrijednosti. Unesen je string koji se " +
                                    "ne može parsirati.", ex);
                            System.out.println("Zahtjeva se unos brojčane vrijednosti! " +
                                    "Pokušajte ponovo.");
                            unos.nextLine();
                        }
                    } while (greska);

                    try {
                        provjeraIndeksa(i, odabranaOsoba);
                        kriviIndeksKontakta = false;
                    } catch (PrevelikIndeksKontakta ex1) {
                        logger.error("Unio se indeks kontakta koji je veći od najvećeg indeksa kontaktiranih osoba"
                                , ex1);
                        System.out.println(ex1.getMessage());
                    } catch (PremalenIndeksKontakta ex2) {
                        logger.error("Unio se indeks kontakta koji je manji od najmanjeg indeksa kontaktiranih osoba"
                                , ex2);
                        System.out.println(ex2.getMessage());
                    }

                    if (j > 0 && !kriviIndeksKontakta) {
                        imaDuplikata = true;
                        try {
                            provjeraDuplikata(osobe, kontaktiraneOsobe, odabranaOsoba);
                            imaDuplikata = false;
                        } catch (DuplikatorKontaktiraneOsobe ex) {
                            logger.error("Ta osoba se već unijela kao kontakt!", ex);
                            System.out.println(ex.getMessage());
                        }
                    }
                } while (odabranaOsoba < 1 || odabranaOsoba > i || imaDuplikata || kriviIndeksKontakta);
                kontaktiraneOsobe.add(osobe.get(odabranaOsoba - 1));
            }
            return kontaktiraneOsobe;
        }
    }

    /**
     * Provjerava je li uneseni broj unutar veći od broja kontaktiranih osoba ili je manji od 1. Ako je veći, baca se
     * iznimka <code>PrevelikIndeksKontakta</code>, a ako je manji, baca se iznimka <code>PremalenIndeksKontakta</code>.
     *
     * @param i             indeks iz <code>for</code> petlje
     * @param odabranaOsoba indeks odabrane kontaktirane osobe
     * @throws PremalenIndeksKontakta iznimka koja se baca u slučaju kad je odabrani broj manji od 1
     * @throws PrevelikIndeksKontakta iznimka koja se baca u slučaju kad je odabrani broj veći od broja kontaktiranih
     *                                osoba
     */
    private static void provjeraIndeksa(Integer i, Integer odabranaOsoba) throws PremalenIndeksKontakta,
            PrevelikIndeksKontakta {
        if (odabranaOsoba > i)
            throw new PrevelikIndeksKontakta("Unijeli ste prevelik indeks za kontaktiranu osobu! " +
                    "Pokušajte ponovo.");
        else if (odabranaOsoba < 1)
            throw new PremalenIndeksKontakta("Unijeli ste premalen indeks za kontaktiranu osobu! " +
                    "Pokušajte ponovo.");
    }

    /**
     * Provjerava je li odabrana osoba već unesena kao kontakt u polje kontaktiranih osoba.
     *
     * @param osobe             lista koje sadrži unesene osobe
     * @param kontaktiraneOsobe lista koje sadrži sve kontaktirane osobe
     * @param odabranaOsoba     indeks odabrane unesene kontaktirane osobe
     * @throws DuplikatorKontaktiraneOsobe iznimka koja se baca u slučaju kad je odabrana osoba već unesena kao kontakt
     */
    private static void provjeraDuplikata(List<Osoba> osobe, List<Osoba> kontaktiraneOsobe, Integer odabranaOsoba)
            throws DuplikatorKontaktiraneOsobe {
        for (int i = 0; i < kontaktiraneOsobe.size() - 1; i++) {
            if (osobe.get(odabranaOsoba - 1).getIme().equals(kontaktiraneOsobe.get(i).getIme()) &&
                    osobe.get(odabranaOsoba - 1).getPrezime().equals(kontaktiraneOsobe.get(i).getPrezime())) {
                throw new DuplikatorKontaktiraneOsobe("Već ste unijeli tu osobu kao kontakt! Pokušajte ponovo.");
            }
        }
    }

    /**
     * Koristi se za odabir bolesti nakon što se ispišu sve ranije unesene bolesti.
     *
     * @param unos    unos s tipkovnice
     * @param bolesti set koje sadrži sve unesene bolesti
     * @param brojBolesti broj unesenih bolesti
     * @return vraća objekt tipa <code>Bolest</code> od koje osoba boluje
     */
    private static Bolest odaberiBolest(Scanner unos, Set<Bolest> bolesti, Integer brojBolesti) {
        Integer odabranaBolest = 0;
        Boolean greska = true;
        List<Bolest> listaBolesti = new ArrayList<>(bolesti);
        do {
            for (int j = 0; j < brojBolesti; j++) {
                System.out.println((j + 1) + ". " + listaBolesti.get(j).getNaziv());
            }
            do {
                try {
                    System.out.print("Odabir: ");
                    odabranaBolest = unos.nextInt();
                    unos.nextLine();
                    greska = false;
                } catch (InputMismatchException ex) {
                    logger.error("Pogreška kod unosa brojčane vrijednosti. Unesen je string koji se ne može parsirati.",
                            ex);
                    System.out.println("Zahtjeva se unos brojčane vrijednosti! Pokušajte ponovo.");
                    unos.nextLine();
                }
            } while (greska);
            if (odabranaBolest < 1 || odabranaBolest > brojBolesti) {
                System.out.println("Neispravan unos, molimo pokušajte ponovno!");
            }
        } while (odabranaBolest < 1 || odabranaBolest > brojBolesti);
        Bolest zarazenBolescu = listaBolesti.get(odabranaBolest - 1);
        return zarazenBolescu;
    }

    /**
     * Koristi se za odabir županije nakon što se ispišu sve ranije unesene županije.
     *
     * @param unos     unos s tipkovnice
     * @param zupanije set koje sadrži sve unesene županije
     * @return vraća objekt tipa <code>Zupanija</code> u kojoj određena osoba živi
     */
    private static Zupanija odaberiZupaniju(Scanner unos, Set<Zupanija> zupanije, Integer brojZupanija) {
        Integer odabranaZupanija = 0;
        Zupanija zupanija;
        List<Zupanija> listaZupanija = new ArrayList<>(zupanije);
        boolean greska = true;
        do {
            for (int j = 0; j < brojZupanija; j++) {
                System.out.println((j + 1) + ". " + listaZupanija.get(j).getNaziv());
            }
            do {
                try {
                    System.out.print("Odabir: ");
                    odabranaZupanija = unos.nextInt();
                    unos.nextLine();
                    greska = false;
                } catch (InputMismatchException ex) {
                    logger.error("Pogreška kod unosa brojčane vrijednosti. Unesen je string koji se ne može parsirati.",
                            ex);
                    System.out.println("Zahtjeva se unos brojčane vrijednosti! Pokušajte ponovo.");
                    unos.nextLine();
                }
            } while (greska);
            if (odabranaZupanija < 1 || odabranaZupanija > brojZupanija) {
                System.out.println("Neispravan unos, molimo pokušajte ponovno!");
            }
        } while (odabranaZupanija < 1 || odabranaZupanija > brojZupanija);
        zupanija = listaZupanija.get(odabranaZupanija - 1);
        return zupanija;
    }

    /**
     * Ispisuje sve podatke svih unesenih osoba(ime i prezime, starost, županiju prebivališta, bolest te osobe koje su
     * bile s njom u kontaktu).
     *
     * @param osobe lista koje sadrži sve unesene osobe sa svim njihovim podacima
     * @param brojOsoba broj unesenih osoba
     * @param mapaBolesti mapa koja kao <code>key</code> ima sve unesene bolesti, a kao <code>value</code> ima listu
     *                    osoba koje boluju od te bolesti ili virusa
     * @param zupanije sortirani set svih županija, sortiran po postotku broja zaraženih
     */
    private static void Ispis(List<Osoba> osobe, Integer brojOsoba, Map<Bolest, List<Osoba>> mapaBolesti,
                              SortedSet<Zupanija> zupanije) {
        System.out.println("\n-------POPIS OSOBA-------");
        for (int i = 0; i < brojOsoba; i++) {
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

        System.out.print("\n");
        mapaBolesti.entrySet().forEach(bolest -> {
            if(bolest.getValue().size() > 0) {
                if(bolest.getKey() instanceof Virus){
                    System.out.print("Od virusa " + bolest.getKey().getNaziv() + " boluje: ");
                }
                else{
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

        double prosjekZarazenih = ((double)zupanije.last().getBrojZarazenih() / zupanije.last().getBrojStanovnika())
                * 100;
        System.out.println("Županija sa najviše zaraženih osoba: " + zupanije.last().getNaziv() + " (" +
                df2.format(prosjekZarazenih) + "%)");
    }

    /**
     * Provjerava da li se pojavila bolest ranije pojavila bolest s istim simptomima
     *
     * @param listaBolesti lista koja sadržava sve dosad unesene bolesti
     * @param tmp zadnja unesena bolest
     * @param indeks trenutni indeks iz <code>for</code> petlje u <code>main</code>funkciji
     * @throws BolestIstihSimptoma iznimka koju baca kada se ponovi bolest s istim simptomima
     */
    private static void provjeraSimptoma(List<Bolest> listaBolesti, Bolest tmp, int indeks) throws BolestIstihSimptoma {
        if(listaBolesti.size() == indeks){
            throw new BolestIstihSimptoma("Već ste unijeli bolest/virus sa istim simptomima! " +
                    "Pokušajte ponovo.");
        }

        Integer brojac = 0;
        List<Simptom> zadnjiSimptomi = new ArrayList<>(tmp.getSimptomi());
        for(int i = 0; i < listaBolesti.size(); i++){
            List<Simptom> simptomiIndeksa = new ArrayList<>(listaBolesti.get(i).getSimptomi());
            if(tmp.getNaziv().equals(listaBolesti.get(i).getNaziv())){
                continue;
            }
            else if(zadnjiSimptomi.size() == simptomiIndeksa.size()){
                for(int j = 0; j < zadnjiSimptomi.size(); j++){
                    if(zadnjiSimptomi.get(j).getNaziv().equals(simptomiIndeksa.get(j).getNaziv())){
                        brojac++;
                    }
                    if(brojac == zadnjiSimptomi.size()){
                        throw new BolestIstihSimptoma("Već ste unijeli bolest/virus sa istim simptomima! " +
                                "Pokušajte ponovo.");
                    }
                }
            }
        }
    }

    /**
     * Koristi se za unos bolesti. Prvo se unosi podatak o tome je li bolest koju unosimo <code>Virus</code> ili
     * <code>Bolest</code>. Zatim se unosi ime bolesti te njeni simptomi.
     *
     * @param unos     unos s tipkovnice
     * @param simptomi set svih unesenih simptoma
     * @param i        indeks iz <code>for</code> petlje
     * @return vraća novi objekt tipa <code>Bolest</code>
     */
    private static Bolest unesiBolest(Scanner unos, Set<Simptom> simptomi, int i, Integer brojSimptoma) {
        System.out.println("Unosite li bolest ili virus?\n1) BOLEST\n2) VIRUS");
        Boolean greska = true;
        Integer odabirBolestIliVirus = 0;
        do {
            try {
                System.out.print("Odabir: ");
                odabirBolestIliVirus = unos.nextInt();
                unos.nextLine();
                greska = false;
            } catch (InputMismatchException ex) {
                logger.error("Pogreška kod unosa brojčane vrijednosti. Unesen je string koji se ne može parsirati.",
                        ex);
                System.out.println("Zahtjeva se unos brojčane vrijednosti! Pokušajte ponovo.");
                unos.nextLine();
            }
        } while (greska);
        System.out.print("Unesite naziv " + (i + 1) + ". bolesti/virusa: ");
        String naziv = unos.nextLine();
        greska = true;
        Integer brojSimptomaBolesti = 0;
        do {
            try {
                System.out.print("Unesite broj simptoma " + (i + 1) + ". bolesti: ");
                brojSimptomaBolesti = unos.nextInt();
                greska = false;
            } catch (InputMismatchException ex) {
                logger.error("Pogreška kod unosa brojčane vrijednosti. Unesen je string koji se ne može parsirati.",
                        ex);
                System.out.println("Zahtjeva se unos brojčane vrijednosti! Pokušajte ponovo.");
                unos.nextLine();
            }
        } while (greska);

        Set<Simptom> simptomiBolesti = new HashSet<>();
        List<Simptom> listaSimptoma = new ArrayList<>(simptomi);
        for (int j = 0; j < brojSimptomaBolesti; j++) {
            Integer indexOdabranogSimptoma = odaberiSimptom(unos, simptomi, j, brojSimptoma);
            simptomiBolesti.add(listaSimptoma.get(indexOdabranogSimptoma - 1));
        }

        if (odabirBolestIliVirus == 1) {
            return new Bolest(naziv, simptomiBolesti);
        } else {
            return new Virus(naziv, simptomiBolesti);
        }

    }

    /**
     * Koristi se za odabir simptoma nakon što se ispišu svi ranije uneseni simptomi.
     *
     * @param unos     unos s tipkovnice
     * @param simptomi set svih unesenih simptoma
     * @param j        indeks iz <code>for</code> petlje
     * @return vraća indeks odabranog simptoma
     */
    private static Integer odaberiSimptom(Scanner unos, Set<Simptom> simptomi, int j, Integer brojSimptoma) {
        Integer odabraniSimptom = 0;
        Boolean greska = true;
        List<Simptom> listaSimptoma = new ArrayList<>(simptomi);
        do {
            System.out.println("Odaberite " + (j + 1) + ". simptom: ");
            for (int k = 0; k < brojSimptoma; k++) {
                System.out.println((k + 1) + ". " + listaSimptoma.get(k).getNaziv() + " " +
                        listaSimptoma.get(k).getVrijednost().getVrijednost());
            }
            do {
                try {
                    System.out.print("Odabir: ");
                    odabraniSimptom = unos.nextInt();
                    unos.nextLine();
                    greska = false;
                } catch (InputMismatchException ex) {
                    logger.error("Pogreška kod unosa brojčane vrijednosti. Unesen je string koji se ne može parsirati.",
                            ex);
                    System.out.println("Zahtjeva se unos brojčane vrijednosti! Pokušajte ponovo.");
                    unos.nextLine();
                }
            } while (greska);
            if ((odabraniSimptom < 1 || odabraniSimptom > brojSimptoma)) {
                System.out.println("Neispravan unos, molimo pokušajte ponovno!");
            }
        } while (odabraniSimptom < 1 || odabraniSimptom > brojSimptoma);
        return odabraniSimptom;
    }

    /**
     * Koristi se za unos simptoma. Prvo se unosi njegov <code>naziv</code>, a zatm njegova <code>vrijednost</code>.
     *
     * @param unos unos s tipkovnice
     * @param i    indeks iz <code>for</code> petlje
     * @return vraća simptom čije smo podatke unijeli
     */
    private static Simptom unesiSimptom(Scanner unos, int i) {
        VrijednostiSimptoma vrijednost = null;
        boolean krivoUnesenaVrijednostSimptoma = true;
        System.out.print("Unesite naziv " + (i + 1) + ". simptoma: ");
        String naziv = unos.nextLine();
        while (krivoUnesenaVrijednostSimptoma){
            System.out.print("Unesite vrijednost " + (i + 1) + ". simptoma (" +
                    VrijednostiSimptoma.RIJETKO.getVrijednost() + ", " + VrijednostiSimptoma.SREDNJE.getVrijednost() +
                    " ili " + VrijednostiSimptoma.CESTO.getVrijednost() + "): ");
            String unesenaVrijednost = unos.nextLine();
            switch (unesenaVrijednost) {
                case "RIJETKO":
                    vrijednost = VrijednostiSimptoma.RIJETKO;
                    krivoUnesenaVrijednostSimptoma = false;
                    break;
                case "SREDNJE":
                    vrijednost = VrijednostiSimptoma.SREDNJE;
                    krivoUnesenaVrijednostSimptoma = false;
                    break;
                case "ČESTO":
                    vrijednost = VrijednostiSimptoma.CESTO;
                    krivoUnesenaVrijednostSimptoma = false;
                    break;
                default:
                    System.out.println("Krivo unesena vrijednost simptoma! Pokušajte ponovo.");
            }
        }
        return new Simptom(naziv, vrijednost);
    }

    /**
     * Koristi se za unos županije. Prvo se unosi <code>naziv</code> županije, a zatim <code>brojStanovnika</code>.
     *
     * @param unos unos s tipkovnice
     * @param i    indeks iz <code>for</code> petlje
     * @return vraća županiju čije smo podatke unijeli
     */
    private static Zupanija unesiZupaniju(Scanner unos, int i) {
        System.out.print("Unesite naziv " + (i + 1) + ". županije: ");
        String naziv = unos.nextLine();
        Integer brojStanovnika = 0, brojZarazenih = 0;
        Boolean greska = true;
        do {
            try {
                System.out.print("Unesite broj stavnovnika " + (i + 1) + ". županije: ");
                brojStanovnika = unos.nextInt();
                unos.nextLine();
                greska = false;
            } catch (InputMismatchException ex) {
                logger.error("Pogreška kod unosa brojčane vrijednosti. Unesen je string koji se ne može parsirati.",
                        ex);
                System.out.println("Zahtjeva se unos brojčane vrijednosti! Pokušajte ponovo.");
                unos.nextLine();
            }
        } while (greska);

        do {
            try {
                System.out.print("Unesite broj zaraženih stanovnika " + (i + 1) + ". županije: ");
                brojZarazenih = unos.nextInt();
                unos.nextLine();
                greska = false;
            } catch (InputMismatchException ex) {
                logger.error("Pogreška kod unosa brojčane vrijednosti. Unesen je string koji se ne može parsirati.",
                        ex);
                System.out.println("Zahtjeva se unos brojčane vrijednosti! Pokušajte ponovo.");
                unos.nextLine();
                greska = true;
            }
        } while (greska);
        return new Zupanija(naziv, brojStanovnika, brojZarazenih);
    }
}