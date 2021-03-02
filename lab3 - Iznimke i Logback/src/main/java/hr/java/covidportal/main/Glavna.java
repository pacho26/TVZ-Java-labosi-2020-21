package hr.java.covidportal.main;

import hr.java.covidportal.iznimke.*;
import hr.java.covidportal.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Koristi se za evidentiranje bolesti kod određenog broja osoba.
 */

public class Glavna {

    private static final Logger logger = LoggerFactory.getLogger(Glavna.class);

    private static final Integer BROJ_ZUPANIJA = 3;
    private static final Integer BROJ_SIMPTOMA = 3;
    private static final Integer BROJ_BOLESTI = 4;
    private static final Integer BROJ_OSOBA = 3;

    /**
     * Koristi se za pokretanje programa koji će od korisnika tražiti unos županija, bolesti(i njihovih simptoma) i
     * podataka o osobama te će ne kraju ispisati sve unesene podatke.
     *
     * @param args argumenti komandne linije(ne koriste se)
     */
    public static void main(String[] args) {

        logger.info("Program je uspješno pokrenut.");

        Scanner unos = new Scanner(System.in);
        Zupanija[] zupanije = new Zupanija[BROJ_ZUPANIJA];
        Simptom[] simptomi = new Simptom[BROJ_SIMPTOMA];
        Bolest[] bolesti = new Bolest[BROJ_BOLESTI];
        Osoba[] osobe = new Osoba[BROJ_OSOBA];

        System.out.println("UNOS ŽUPANIJA");
        for (int i = 0; i < BROJ_ZUPANIJA; i++) {
            zupanije[i] = unesiZupaniju(unos, i);
        }
        logger.info("Sve županije su uspješno unesene.");

        System.out.println("\nUNOS SIMPTOMA");
        for (int i = 0; i < BROJ_SIMPTOMA; i++) {
            simptomi[i] = unesiSimptom(unos, i);
        }
        logger.info("Svi simptomi su uspješno uneseni.");

        System.out.println("\nUNOS BOLESTI/VIRUSA");
        for (int i = 0; i < BROJ_BOLESTI; i++) {
            Boolean ponavljanjeSimptoma = true;
            do {
                try {
                    bolesti[i] = unesiBolest(unos, simptomi, i, bolesti);
                    provjeraSimptoma(i, bolesti);
                    ponavljanjeSimptoma = false;
                }
                catch (BolestIstihSimptoma ex){
                    System.out.println(ex.getMessage());
                    logger.error("Već je unesena bolest sa istim simptomima!", ex);
                }
            } while(ponavljanjeSimptoma);
        }
        logger.info("Sve bolesti su uspješno unesene.");

        System.out.println("\nUNOS OSOBA");
        for (int i = 0; i < BROJ_OSOBA; i++) {
            osobe[i] = unesiOsobu(unos, zupanije, bolesti, osobe, i);
            System.out.println("\n");
        }
        logger.info("Sve osobe su uspješno unesene.");

        Ispis(osobe);
    }

    /**
     * Koristi se za unos podataka određene osobe.
     *
     * @param unos unos s tipkovnice
     * @param zupanije polje koje sadrži unesene županije
     * @param bolesti polje koje sadrži unesene bolesti
     * @param osobe polje koje sadrži unesene osobe
     * @param i indeks iz <code>for</code> petlje
     * @return vraća novi objekt tipa <code>Osoba</code> s unesenim podacima
     */
    private static Osoba unesiOsobu(Scanner unos, Zupanija[] zupanije, Bolest[] bolesti, Osoba[] osobe, int i) {
        System.out.println("Unesite podatke " + (i + 1) + ". osobe.");
        System.out.print("Unesite ime osobe: ");
        String ime = unos.nextLine();
        System.out.print("Unesite prezime osobe: ");
        String prezime = unos.nextLine();

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
        Zupanija zupanija = odaberiZupaniju(unos, zupanije);

        System.out.println("Unesite bolest osobe: ");
        Bolest zarazenBolescu = odaberiBolest(unos, bolesti);

        Osoba[] kontakti = odaberiKontakte(unos, osobe, i);
        return new Osoba.Builder()
                .withIme(ime)
                .withPrezime(prezime)
                .withStarost(starost)
                .withZupanija(zupanija)
                .withZarazenBolescu(zarazenBolescu)
                .withKontaktiraneOsobe(kontakti)
                .build();
    }

    /**
     * Koristi se za odabir osoba koje su bile u kontaktu s određenom osobom.
     *
     * @param unos unos s tipkovnice
     * @param osobe polje koje sadrži unesene osobe
     * @param i indeks iz <code>for</code> petlje
     * @return vraća polje tipa <code>Osoba</code> sa svim osobama s kojima je određena osoba bila u kontaktu
     */
    private static Osoba[] odaberiKontakte(Scanner unos, Osoba[] osobe, int i) {
        if (i == 0) {
            Osoba[] prviZarazeni = new Osoba[0];
            return prviZarazeni;
        } else {
            Integer brojKontaktiranihOsoba = 0;
            Boolean greska = true;
            do {
                try {
                    System.out.print("Unesite broj osoba koje su bile u kontaktu s tom osobom: ");
                    brojKontaktiranihOsoba = unos.nextInt();
                    unos.nextLine();
                    greska = false;
                } catch (InputMismatchException ex) {
                    logger.error("Pogreška kod unosa brojčane vrijednosti. Unesen je string koji se ne može parsirati.",
                        ex);
                    System.out.println("Zahtjeva se unos brojčane vrijednosti! Pokušajte ponovo.");
                    unos.nextLine();
                }
            } while (greska);
            Osoba[] kontaktiraneOsobe = new Osoba[brojKontaktiranihOsoba];
            Integer odabranaOsoba = 0;
            Boolean imaDuplikata = false, kriviIndeksKontakta = true;
            for (int j = 0; j < brojKontaktiranihOsoba; j++) {
                System.out.println("Odaberite " + (j + 1) + ". osobu koja je bila u kontaktu:");
                do {
                    for (int k = 0; k < i; k++) {
                        System.out.println((k + 1) + ". " + osobe[k].getIme() + " " + osobe[k].getPrezime());
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
                    }
                    catch (PrevelikIndeksKontakta ex1){
                        logger.error("Unio se indeks kontakta koji je veći od najvećeg indeksa kontaktiranih osoba"
                                , ex1);
                        System.out.println(ex1.getMessage());
                    }
                    catch (PremalenIndeksKontakta ex2){
                        logger.error("Unio se indeks kontakta koji je manji od najmanjeg indeksa kontaktiranih osoba"
                                , ex2);
                        System.out.println(ex2.getMessage());
                    }

                    if (j > 0 && kriviIndeksKontakta == false) {
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
                kontaktiraneOsobe[j] = osobe[odabranaOsoba - 1];
            }
            return kontaktiraneOsobe;
        }
    }

    /**
     * Provjerava je li uneseni broj unutar veći od broja kontaktiranih osoba ili je manji od 1. Ako je veći, baca se
     * iznimka <code>PrevelikIndeksKontakta</code>, a ako je manji, baca se iznimka <code>PremalenIndeksKontakta</code>.
     *
     * @param i indeks iz <code>for</code> petlje
     * @param odabranaOsoba indeks odabrane kontaktirane osobe
     * @throws PremalenIndeksKontakta iznimka koja se baca u slučaju kad je odabrani broj manji od 1
     * @throws PrevelikIndeksKontakta iznimka koja se baca u slučaju kad je odabrani broj veći od broja kontaktiranih
     * osoba
     */
    private static void provjeraIndeksa(Integer i, Integer odabranaOsoba) throws PremalenIndeksKontakta,
            PrevelikIndeksKontakta{
        if (odabranaOsoba > i)
            throw new PrevelikIndeksKontakta("Unijeli ste prevelik indeks za kontaktiranu osobu! " +
                    "Pokušajte ponovo.");
        else if(odabranaOsoba < 1)
            throw new PremalenIndeksKontakta("Unijeli ste premalen indeks za kontaktiranu osobu! " +
                    "Pokušajte ponovo.");
        }

    /**
     * Provjerava je li odabrana osoba već unesena kao kontakt u polje kontaktiranih osoba.
     *
     * @param osobe polje koje sadrži unesene osobe
     * @param kontaktiraneOsobe polje koje sadrži sve kontaktirane osobe
     * @param odabranaOsoba indeks odabrane unesene kontaktirane osobe
     * @throws DuplikatorKontaktiraneOsobe iznimka koja se baca u slučaju kad je odabrana osoba već unesena kao kontakt
     */
    private static void provjeraDuplikata(Osoba[] osobe, Osoba[] kontaktiraneOsobe, Integer odabranaOsoba)
            throws DuplikatorKontaktiraneOsobe {
        for (int i = 0; i < kontaktiraneOsobe.length - 1; i++) {
            if (osobe[odabranaOsoba - 1].getIme() == kontaktiraneOsobe[i].getIme() &&
                    osobe[odabranaOsoba - 1].getPrezime() == kontaktiraneOsobe[i].getPrezime()) {
                throw new DuplikatorKontaktiraneOsobe("Već ste unijeli tu osobu kao kontakt! Pokušajte ponovo.");
            }
        }
    }

    /**
     * Koristi se za odabir bolesti nakon što se ispišu sve ranije unesene bolesti.
     *
     * @param unos unos s tipkovnice
     * @param bolesti polje koje sadrži sve unesene bolesti
     * @return vraća objekt tipa <code>Bolest</code> od koje osoba boluje
     */
    private static Bolest odaberiBolest(Scanner unos, Bolest[] bolesti) {
        Integer odabranaBolest = 0;
        Bolest zarazenBolescu;
        Boolean greska = true;
        do {
            for (int j = 0; j < BROJ_BOLESTI; j++) {
                System.out.println((j + 1) + ". " + bolesti[j  ].getNaziv());
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
            if (odabranaBolest < 1 || odabranaBolest > BROJ_BOLESTI) {
                System.out.println("Neispravan unos, molimo pokušajte ponovno!");
            }
        } while (odabranaBolest < 1 || odabranaBolest > BROJ_BOLESTI);
        zarazenBolescu = bolesti[odabranaBolest - 1];
        return zarazenBolescu;
    }

    /**
     * Koristi se za odabir županije nakon što se ispišu sve ranije unesene županije.
     *
     * @param unos unos s tipkovnice
     * @param zupanije polje koje sadrži sve unesene županije
     * @return vraća objekt tipa <code>Zupanija</code> u kojoj određena osoba živi
     */
    private static Zupanija odaberiZupaniju(Scanner unos, Zupanija[] zupanije) {
        Integer odabranaZupanija = 0;
        Zupanija zupanija;
        Boolean greska = true;
        do {
            for (int j = 0; j < BROJ_ZUPANIJA; j++) {
                System.out.println((j + 1) + ". " + zupanije[j].getNaziv());
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
            if (odabranaZupanija < 1 || odabranaZupanija > BROJ_ZUPANIJA) {
                System.out.println("Neispravan unos, molimo pokušajte ponovno!");
            }
        } while (odabranaZupanija < 1 || odabranaZupanija > BROJ_ZUPANIJA);
        zupanija = zupanije[odabranaZupanija - 1];
        return zupanija;
    }

    /**
     * Ispisuje sve podatke svih unesenih osoba(ime i prezime, starost, županiju prebivališta, bolest te osobe koje su
     * bile s njom u kontaktu).
     *
     * @param osobe polje koje sadrži sve unesene osobe sa svim njihovim podacima
     */
    private static void Ispis(Osoba[] osobe) {
        System.out.println("\n------POPIS OSOBA------");
        for (int i = 0; i < BROJ_OSOBA; i++) {
            System.out.println("\n" + (i + 1) + ". OSOBA:");
            System.out.println("Ime i prezime: " + osobe[i].getIme() + " " + osobe[i].getPrezime());
            System.out.println("Starost: " + osobe[i].getStarost());
            System.out.println("Županija prebivališta: " + osobe[i].getZupanija().getNaziv());
            System.out.println("Zaražen bolešću: " + osobe[i].getZarazenBolescu().getNaziv());
            if (i == 0 || osobe[i].getKontaktiraneOsobe().length == 0) {
                System.out.println("Nema kontaktiranih osoba.");
            } else {
                System.out.println("Kontaktirane osobe:");
                for (int j = 0; j < osobe[i].getKontaktiraneOsobe().length; j++) {
                    System.out.println("- " + osobe[i].getKontaktiraneOsobe()[j].getIme() + " " +
                            osobe[i].getKontaktiraneOsobe()[j].getPrezime());
                }
            }
        }
    }

    /**
     * Provjerava da li je već unesena bolest sa istim simptomima.
     *
     * @param i indeks iz <code>for</code> petlje
     * @param bolesti polje koje sadrži sve unesene bolesti
     * @throws BolestIstihSimptoma iznimka koja se baca kad se utvrdi da je već unesena bolest sa istim simptomima
     */
    private static void provjeraSimptoma(int i, Bolest[] bolesti) throws BolestIstihSimptoma {
        for (int j = 0; j < i; j++) {
            if (bolesti[i].getSimptomi().length == bolesti[j].getSimptomi().length) {
                String[] s1 = new String[bolesti[i].getSimptomi().length];
                String[] s2 = new String[bolesti[i].getSimptomi().length];
                for (int k = 0; k < bolesti[i].getSimptomi().length; k++) {
                    s1[k] = bolesti[i].getSimptomi()[k].getNaziv();
                    s2[k] = bolesti[j].getSimptomi()[k].getNaziv();
                }
                Arrays.sort(s1);
                Arrays.sort(s2);
                int brojac = 0;
                for(int a = 0; a < s1.length; a++){
                    if(s1[a].equals(s2[a])){
                        brojac++;
                    }
                }
                if(brojac == s1.length){
                     throw new BolestIstihSimptoma("Već ste unijeli bolest/virus sa istim simptomima! " +
                                    "Pokušajte ponovo.");
                }
            }
        }
    }

    /**
     * Koristi se za unos bolesti. Prvo se unosi podatak o tome je li bolest koju unosimo <code>Virus</code> ili
     * <code>Bolest</code>. Zatim se unosi ime bolesti te njeni simptomi.
     *
     * @param unos unos s tipkovnice
     * @param simptomi polje svih unesenih simptoma
     * @param i indeks iz <code>for</code> petlje
     * @return vraća novi objekt tipa <code>Bolest</code>
     */
    private static Bolest unesiBolest(Scanner unos, Simptom[] simptomi, int i, Bolest[] bolesti) {
        System.out.println("Unosite li bolest ili virus?\n1) BOLEST\n2) VIRUS");
        Boolean greska = true;
        String naziv = null;
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
        Boolean predugacakNazivVirusa = true, dupliNazivVirusa = true;
        do {
            try {
                System.out.print("Unesite naziv " + (i + 1) + ". bolesti/virusa: ");
                naziv = unos.nextLine();
                provjeraNaziva(naziv, odabirBolestIliVirus);
                provjeraDuplihNaziva(naziv, bolesti);
                predugacakNazivVirusa = false;
                dupliNazivVirusa = false;
            }
            catch(NeispravanNazivException ex1){  // 1.zadatak
                logger.error("Pogreška kod unosa virusa. Unesen je naziv virusa koji je duži od 15 znakova.");
                System.out.println(ex1.getMessage());
            }
            catch (DupliVirusException ex2){  // 2.zadatak
                logger.error("Pogreška kod unosa virusa. Unesen je naziv virusa koji je već ranije unesen.");
                System.out.println(ex2.getMessage());
            }
        }while(odabirBolestIliVirus == 2 && predugacakNazivVirusa && dupliNazivVirusa);
        logger.info("Unos naziva virusa uspješno je odrađen.");

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
        Simptom[] simptomiBolesti = new Simptom[brojSimptomaBolesti];
        for (int j = 0; j < brojSimptomaBolesti; j++) {
            Integer indexOdabranogSimptoma = odaberiSimptom(unos, simptomi, j);
            simptomiBolesti[j] = simptomi[indexOdabranogSimptoma - 1];
        }

        if (odabirBolestIliVirus == 1) {
            return new Bolest(naziv, simptomiBolesti);
        } else {
            return new Virus(naziv, simptomiBolesti);
        }

    }

    /**
     * Provjerava je li se unio virus čiji je naziv duži od 15 znakova.
     *
     * @param naziv naziv koji se unio i koji se provjerava
     * @param odabir odabir je smo li ranije odabrali <code>Bolest</code> ili <code>Virus</code>
     * @throws NeispravanNazivException
     */
    private static void provjeraNaziva(String naziv, Integer odabir) throws NeispravanNazivException{   // 1.zadatak
        if(naziv.length() > 15 && odabir == 2){
            throw new NeispravanNazivException("Uneseni naziv ima previše znakova! Pokušajte ponovo.");
        }
    }

    /**
     * Provjerava je li se do sada već ponovio virus istog naziva.
     *
     * @param naziv naziv koji smo unijeli i koji se proverava je li se dogodio
     * @param bolesti polje koje sadrži sve navedene bolesti
     */
    private static void provjeraDuplihNaziva(String naziv, Bolest[] bolesti){    // 2.zadatak
        for(int i = 0; i < bolesti.length; i++){
            if((bolesti[i] instanceof Virus) && bolesti[i].getNaziv().equals(naziv)){
                throw new DupliVirusException("Već je unesen virus sa istim nazivom! Pokušajte ponovo.");
            }
        }
    }

    /**
     * Koristi se za odabir simptoma nakon što se ispišu svi ranije uneseni simptomi.
     *
     * @param unos unos s tipkovnice
     * @param simptomi polje svih unesenih simptoma
     * @param j indeks iz <code>for</code> petlje
     * @return vraća indeks odabranog simptoma
     */
    private static Integer odaberiSimptom(Scanner unos, Simptom[] simptomi, int j) {
        Integer odabraniSimptom = 0;
        Boolean greska = true;
        do {
            System.out.println("Odaberite " + (j + 1) + ". simptom: ");
            for (int k = 0; k < BROJ_SIMPTOMA; k++) {
                System.out.println((k + 1) + ". " + simptomi[k].getNaziv() + " " + simptomi[k].getVrijednost());
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
            if ((odabraniSimptom < 1 || odabraniSimptom > BROJ_SIMPTOMA)) {
                System.out.println("Neispravan unos, molimo pokušajte ponovno!");
            }
        } while (odabraniSimptom < 1 || odabraniSimptom > BROJ_SIMPTOMA);
        return odabraniSimptom;
    }

    /**
     * Koristi se za unos simptoma. Prvo se unosi njegov <code>naziv</code>, a zatm njegova <code>vrijednost</code>.
     *
     * @param unos unos s tipkovnice
     * @param i indeks iz <code>for</code> petlje
     * @return vraća simptom čije smo podatke unijeli
     */
    private static Simptom unesiSimptom(Scanner unos, int i) {
        System.out.print("Unesite naziv " + (i + 1) + ". simptoma: ");
        String naziv = unos.nextLine();
        System.out.print("Unesite vrijednost " + (i + 1) + ". simptoma (RIJETKO, SREDNJE ILI ČESTO): ");
        String vrijednost = unos.nextLine();
        Simptom noviSimptom = new Simptom(naziv, vrijednost);
        return noviSimptom;
    }

    /**
     * Koristi se za unos županije. Prvo se unosi <code>naziv</code> županije, a zatim <code>brojStanovnika</code>.
     *
     * @param unos unos s tipkovnice
     * @param i indeks iz <code>for</code> petlje
     * @return vraća županiju čije smo podatke unijeli
     */
    private static Zupanija unesiZupaniju(Scanner unos, int i) {
        System.out.print("Unesite naziv " + (i + 1) + ". županije: ");
        String naziv = unos.nextLine();
        Integer brojStanovnika = 0;
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
        Zupanija zupanija = new Zupanija(naziv, brojStanovnika);
        return zupanija;
    }
}