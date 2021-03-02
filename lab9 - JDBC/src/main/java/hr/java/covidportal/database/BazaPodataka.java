package main.java.hr.java.covidportal.database;

import main.java.hr.java.covidportal.enumeracije.VrijednostiSimptoma;
import main.java.hr.java.covidportal.model.*;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BazaPodataka {

    private static final String DATABASE_FILE = "src/main/resources/database.properties";

    /**
     * Otvara vezu s bazom podataka.
     *
     * @return vraća otvorenu vezu
     * @throws SQLException baca se u slučaju neuspješnog spajanja s bazom podataka
     */
    public static Connection otvaranjeVeze() throws SQLException {
        Properties properties = new Properties();
        Connection veza = null;

        try {
            properties.load(new FileReader(DATABASE_FILE));

            String urlBazePodataka = properties.getProperty("bazaPodatakaUrl");
            String korisnickoIme = properties.getProperty("korisnickoIme");
            String lozinka = properties.getProperty("lozinka");
            veza = DriverManager.
                    getConnection(urlBazePodataka, korisnickoIme, lozinka);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return veza;
    }

    /**
     * Zatvara vezu s bazom podataka.
     *
     * @param veza veza koja se zatvara
     * @throws SQLException baca se u slučaju neuspješnog zatvaranja veze s bazom podataka
     */
    public static void zatvaranjeVeze(Connection veza) throws SQLException {
        veza.close();
    }

    /**
     * Pronalazi i vraća objekt tipa <code>Bolest</code> s navedenim identifikacijskim brojem
     *
     * @param idBolesti identifikacijski broj bolesti preko kojega se dohvaća određena bolest iz baze podataka
     * @param veza veza s kojom se spaja na bazu podataka
     * @return bolest s navedenim identifikacijskim brojem
     * @throws SQLException baca se u slučaju neuspješnog dohvaćanja iz baze podataka
     */
    private static List<Long> dohvatiIdSimptomeBolesti(Long idBolesti, Connection veza) throws SQLException {
        List<Long> listaIdSimptomaBolesti = new ArrayList<>();

        PreparedStatement upit = veza.prepareStatement("SELECT * FROM BOLEST_SIMPTOM WHERE BOLEST_ID = ?");
        upit.setLong(1, idBolesti);
        ResultSet resultSet = upit.executeQuery();
        while (resultSet.next()) {
            Long idSimptoma = resultSet.getLong("simptom_id");
            listaIdSimptomaBolesti.add(idSimptoma);
        }

        return listaIdSimptomaBolesti;
    }

    /**
     * Puni i vraća novu listu svim dohvaćenim objektima tipa <code>Bolest</code> iz baze podataka.
     *
     * @return lista sa svim dohvaćenim bolestima
     * @throws SQLException baca se u slučaju neuspješnog izvršenja upita(statement-a) ili neuspješnog
     * otvaranja/zatvaranja veze
     */
    public static List<Bolest> dohvacanjeSvihBolesti() throws SQLException {
        List<Bolest> listaBolesti = new ArrayList<>();

        Connection veza = otvaranjeVeze();
        Statement statement = veza.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM BOLEST");
        while (resultSet.next()) {
            List<Simptom> listaSimptomaTrazena = new ArrayList<>();

            String naziv = resultSet.getString("naziv");
            Long id = resultSet.getLong("id");
            List<Long> listaIdentifikatoraSimptoma = dohvatiIdSimptomeBolesti(id, veza);

            for (Long identifikator : listaIdentifikatoraSimptoma) {
                Simptom noviSimptom = dohvacanjeSimptomaPremaId(identifikator);
                listaSimptomaTrazena.add(noviSimptom);
            }

            if(resultSet.getBoolean("virus")) {
                Virus noviVirus = new Virus(id, naziv, listaSimptomaTrazena);
                listaBolesti.add(noviVirus);
            }
            else {
                Bolest novaBolest = new Bolest(id, naziv, listaSimptomaTrazena);
                listaBolesti.add(novaBolest);
            }
        }

        zatvaranjeVeze(veza);
        return listaBolesti;
    }

    /**
     * Pronalazi i vraća objekt tipa <code>Bolest</code> s navedenim identifikacijskim brojem.
     *
     * @param identifikator identifikacijski broj preko kojeg se pronalazi bolest
     * @return objekt tipa <code>Bolest</code> s navedenim identifikacijskim brojem
     * @throws SQLException baca se u slučaju neuspješnog otvaranja/zatvaranja veze ili neuspješnog izvršavanja upita
     */
    private static Bolest dohvacanjeBolestiPremaId (Long identifikator) throws SQLException {
        Connection veza = otvaranjeVeze();
        PreparedStatement upit = veza.prepareStatement("SELECT * FROM BOLEST WHERE ID = ?");
        upit.setLong(1, identifikator);
        ResultSet resultSet = upit.executeQuery();
        Bolest novaBolest = null;

        while (resultSet.next()) {
            String naziv = resultSet.getString("naziv");
            Long id = resultSet.getLong("id");
            Boolean jeVirus = resultSet.getBoolean("virus");
            List<Simptom> listaSimptomaTrazena = new ArrayList<>();

            List<Long> listaIdentifikatoraSimptoma = dohvatiIdSimptomeBolesti(identifikator, veza);
            for (Long idSimptoma : listaIdentifikatoraSimptoma) {
                Simptom noviSimptom = dohvacanjeSimptomaPremaId(idSimptoma);
                listaSimptomaTrazena.add(noviSimptom);
            }

            if (jeVirus) {
                novaBolest = new Virus(id, naziv, listaSimptomaTrazena);
            }
            else {
                novaBolest = new Bolest(id, naziv, listaSimptomaTrazena);
            }
        }

        zatvaranjeVeze(veza);
        return novaBolest;
    }

    /**
     * Sprema novi objekt tipa <code>Bolest</code> u bazu podataka.
     *
     * @param bolest nova bolest koja se unosi u bazu podataka
     * @param jeVirus podatak koji govori je li nova bolest ujedno i virus
     * @throws SQLException baca se u slučaju neuspješnog otvaranja/zatvaranja veze ili neuspješnog izvršavanja upita
     */
    public static void spremanjeNoveBolesti (Bolest bolest, Boolean jeVirus) throws SQLException {
        Connection veza = otvaranjeVeze();
        PreparedStatement upit = veza.prepareStatement("INSERT INTO BOLEST(NAZIV, VIRUS) VALUES (?,?)",
                Statement.RETURN_GENERATED_KEYS);
        upit.setString(1, bolest.getNaziv());
        upit.setBoolean(2, jeVirus);
        upit.executeUpdate();

        ResultSet resultSet = upit.getGeneratedKeys();

        if (resultSet.next()) {
            Long idDodaneBolesti = Long.parseLong(resultSet.getString(1));

            for (Simptom simptom : bolest.getSimptomi()) {
                PreparedStatement upitZaSimptome =
                        veza.prepareStatement("INSERT INTO BOLEST_SIMPTOM(BOLEST_ID, SIMPTOM_ID) VALUES (?, ?)");
                upitZaSimptome.setLong(1, idDodaneBolesti);
                upitZaSimptome.setLong(2, simptom.getId());
                upitZaSimptome.executeUpdate();
            }
        }
        zatvaranjeVeze(veza);
    }

    /**
     * Puni i vraća novu listu svim dohvaćenim objektima tipa <code>Simptom</code> iz baze podataka
     *
     * @return lista sa svim dohvaćenim simptomima
     * @throws SQLException baca se u slučaju neuspješnog otvaranja/zatvaranja veze ili neuspješnog izvršavanja upita
     */
    public static List<Simptom> dohvacanjeSvihSimptoma() throws SQLException {
        List<Simptom> listaSimptoma = new ArrayList<>();

        Connection veza = otvaranjeVeze();
        Statement statement = veza.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM SIMPTOM");
        while (resultSet.next()) {
            Long id = resultSet.getLong("id");
            String naziv = resultSet.getString("naziv");
            String vrijednost = resultSet.getString("vrijednost");
            VrijednostiSimptoma vrijednostSimptoma = VrijednostiSimptoma.valueOf(vrijednost);

            Simptom noviSimptom = new Simptom(id, naziv, vrijednostSimptoma);
            listaSimptoma.add(noviSimptom);
        }

        zatvaranjeVeze(veza);
        return listaSimptoma;
    }

    /**
     * Pronalazi i vraća objekt tipa <code>Simptom</code> s navedenim identifikacijskim brojem.
     *
     * @param identifikator identifikacijski broj preko kojeg se pronalazi simptom
     * @return objekt tipa <code>Simptom</code> s navedenim identifikacijskim brojem
     * @throws SQLException baca se u slučaju neuspješnog otvaranja/zatvaranja veze ili neuspješnog izvršavanja upita
     */
    private static Simptom dohvacanjeSimptomaPremaId (Long identifikator) throws SQLException {
        Connection veza = otvaranjeVeze();
        PreparedStatement upit = veza.prepareStatement("SELECT * FROM SIMPTOM WHERE ID = ?");
        upit.setLong(1, identifikator);
        ResultSet resultSet = upit.executeQuery();

        Simptom simptom = null;
        while (resultSet.next()) {
            Long id = resultSet.getLong("id");
            String naziv = resultSet.getString("naziv");
            String vrijednost = resultSet.getString("vrijednost");
            VrijednostiSimptoma vrijednostSimptoma = VrijednostiSimptoma.valueOf(vrijednost);

            simptom = new Simptom(id, naziv, vrijednostSimptoma);
        }

        zatvaranjeVeze(veza);
        return simptom;
    }

    /**
     * Sprema novi objekt tipa <code>Simptom</code> u bazu podataka.
     *
     * @param noviSimptom nova bolest koja se unosi u bazu podataka
     * @throws SQLException baca se u slučaju neuspješnog otvaranja/zatvaranja veze ili neuspješnog izvršavanja upita
     */
    public static void spremanjeNovogSimptoma (Simptom noviSimptom) throws SQLException {
        Connection veza = otvaranjeVeze();
        PreparedStatement upit = veza.prepareStatement("INSERT INTO SIMPTOM(NAZIV, VRIJEDNOST) VALUES (?, ?)");
        upit.setString(1, noviSimptom.getNaziv());
        upit.setString(2, noviSimptom.getVrijednost().getVrijednost());
        upit.executeUpdate();

        zatvaranjeVeze(veza);
    }

    /**
     * Puni i vraća novu listu svim dohvaćenim objektima tipa <code>Zupanija</code> iz baze podataka
     *
     * @return lista sa svim dohvaćenim županijama
     * @throws SQLException baca se u slučaju neuspješnog otvaranja/zatvaranja veze ili neuspješnog izvršavanja upita
     */
    public static List<Zupanija> dohvacanjeSvihZupanija() throws SQLException {
        List<Zupanija> listaZupanija = new ArrayList<>();

        Connection veza = otvaranjeVeze();
        Statement statement = veza.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM ZUPANIJA");
        while (resultSet.next()) {
            Long id = resultSet.getLong("id");
            String naziv = resultSet.getString("naziv");
            Integer brojStanovnika = resultSet.getInt("broj_stanovnika");
            Integer brojZarazenih = resultSet.getInt("broj_zarazenih_stanovnika");

            Zupanija novaZupanija = new Zupanija(id, naziv, brojStanovnika, brojZarazenih);
            listaZupanija.add(novaZupanija);
        }

        zatvaranjeVeze(veza);
        return listaZupanija;
    }

    /**
     * Pronalazi i vraća objekt tipa <code>Zupanija</code> s navedenim identifikacijskim brojem.
     *
     * @param identifikator identifikacijski broj preko kojeg se pronalazi županija
     * @return objekt tipa <code>Zupanija</code> s navedenim identifikacijskim brojem
     * @throws SQLException baca se u slučaju neuspješnog otvaranja/zatvaranja veze ili neuspješnog izvršavanja upita
     */
    public static Zupanija dohvacanjeZupanijePremaId (Long identifikator) throws SQLException {
        Connection veza = otvaranjeVeze();
        Zupanija trazenaZupanija = null;

        PreparedStatement upit = veza.prepareStatement("SELECT * FROM ZUPANIJA WHERE ID = ?");
        upit.setLong(1, identifikator);
        ResultSet resultSet = upit.executeQuery();
        while (resultSet.next()) {
            Long id = resultSet.getLong("id");
            String naziv = resultSet.getString("naziv");
            Integer brojStanovnika = resultSet.getInt("broj_stanovnika");
            Integer brojZarazenih = resultSet.getInt("broj_zarazenih_stanovnika");

            trazenaZupanija = new Zupanija(id, naziv, brojStanovnika, brojZarazenih);
        }

        zatvaranjeVeze(veza);
        return trazenaZupanija;
    }

    /**
     * Sprema novi objekt tipa <code>Zupanija</code> u bazu podataka.
     *
     * @param novaZupanija nova županija koja se unosi u bazu podataka
     * @throws SQLException baca se u slučaju neuspješnog otvaranja/zatvaranja veze ili neuspješnog izvršavanja upita
     */
    public static void spremanjeNoveZupanije (Zupanija novaZupanija) throws SQLException {
        Connection veza = otvaranjeVeze();

        PreparedStatement upit = veza.prepareStatement
                ("INSERT INTO ZUPANIJA(NAZIV, BROJ_STANOVNIKA, BROJ_ZARAZENIH_STANOVNIKA) VALUES(?, ?, ?)");
        upit.setString(1, novaZupanija.getNaziv());
        upit.setInt(2, novaZupanija.getBrojStanovnika());
        upit.setInt(3, novaZupanija.getBrojZarazenih());
        upit.executeUpdate();

        zatvaranjeVeze(veza);
    }

    /**
     * Puni i vraća novu listu svim dohvaćenim osobama s kojima je osoba navedenog identifikacijskog broja bila u
     * kontaktu
     *
     * @param identifikator identifikacijski broj osobe čije kontakte želimo dohvatiti
     * @param veza veza s kojom se spaja na bazu podataka
     * @return lista svih kontakata osobe s navedenim identifikacijskim brojem
     * @throws SQLException baca se u slučaju neuspješnog izvođenja upita
     */
    private static List<Long> dohvatiIdKontaktiranihOsoba (Long identifikator, Connection veza) throws SQLException {
        List<Long> listaIdKontaktiranihOsoba = new ArrayList<>();
        PreparedStatement upit = veza.prepareStatement("SELECT * FROM KONTAKTIRANE_OSOBE WHERE OSOBA_ID = ?");
        upit.setLong(1, identifikator);
        ResultSet resultSet = upit.executeQuery();
        while(resultSet.next()) {
            Long idKontakta = resultSet.getLong("kontaktirana_osoba_id");
            listaIdKontaktiranihOsoba.add(idKontakta);
        }

        return listaIdKontaktiranihOsoba;
    }

    /**
     * Puni i vraća novu listu svim dohvaćenim objektima tipa <code>Osoba</code> iz baze podataka
     *
     * @return lista sa svim dohvaćenim osobama
     * @throws SQLException baca se u slučaju neuspješnog otvaranja/zatvaranja veze ili neuspješnog izvršavanja upita
     */
    public static List<Osoba> dohvatiSveOsobe () throws SQLException {
        Connection veza = otvaranjeVeze();
        List<Osoba> listaOsoba = new ArrayList<>();

        Statement statement = veza.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM OSOBA");
        while(resultSet.next()) {
            Long idOsobe = resultSet.getLong("id");
            String ime = resultSet.getString("ime");
            String prezime = resultSet.getString("prezime");
            Date datumRodjenja = resultSet.getDate("datum_rodjenja");
            LocalDate datumRodjenjaLocal = datumRodjenja.toLocalDate();
            Long idZupanije = resultSet.getLong("zupanija_id");
            Long idBolesti = resultSet.getLong("bolest_id");
            Zupanija zupanija = dohvacanjeZupanijePremaId(idZupanije);
            Bolest bolest = dohvacanjeBolestiPremaId(idBolesti);
            List<Osoba> kontaktiraneOsobe = new ArrayList<>();

            listaOsoba.add(new Osoba.Builder()
                    .withId(idOsobe)
                    .withIme(ime)
                    .withPrezime(prezime)
                    .withZupanija(zupanija)
                    .withZarazenBolescu(bolest)
                    .withKontaktiraneOsobe(kontaktiraneOsobe)
                    .withDatumRodjenja(datumRodjenjaLocal)
                    .withStarost(datumRodjenjaLocal)
                    .build());
        }

        for (Osoba osoba : listaOsoba) {
            List<Long> listaIdKontaktiranih = dohvatiIdKontaktiranihOsoba(osoba.getId(), veza);
            for (Long idKontakta : listaIdKontaktiranih) {
                for (Osoba mogucaOsoba : listaOsoba) {
                    if (mogucaOsoba.getId().equals(idKontakta)) {
                        osoba.getKontaktiraneOsobe().add(mogucaOsoba);
                    }
                }
            }
        }

        zatvaranjeVeze(veza);
        return listaOsoba;
    }

    /**
     * Pronalazi i vraća objekt tipa <code>Osoba</code> s navedenim identifikacijskim brojem.
     *
     * @param identifikator identifikacijski broj preko kojeg se pronalazi osoba
     * @return objekt tipa <code>Osoba</code> s navedenim identifikacijskim brojem
     * @throws SQLException baca se u slučaju neuspješnog otvaranja/zatvaranja veze ili neuspješnog izvršavanja upita
     */
    private static Osoba dohvatiOsobuPremaId (Long identifikator) throws SQLException {
        Connection veza = otvaranjeVeze();
        Osoba trazenaOsoba = null;

        PreparedStatement upit = veza.prepareStatement("SELECT * FROM OSOBA WHERE ID = ?");
        upit.setLong(1, identifikator);
        ResultSet resultSet = upit.executeQuery();
        while (resultSet.next()) {
            String ime = resultSet.getString("ime");
            String prezime = resultSet.getString("prezime");
            Date datumRodjenja = resultSet.getDate("datum_rodjenja");
            LocalDate datumRodjenjaLocal = datumRodjenja.toLocalDate();
            Long idZupanije = resultSet.getLong("zupanija_id");
            Long idBolesti = resultSet.getLong("bolest_id");
            Zupanija zupanija = dohvacanjeZupanijePremaId(idZupanije);
            Bolest bolest = dohvacanjeBolestiPremaId(idBolesti);
            List<Osoba> kontaktiraneOsobe = new ArrayList<>();

            trazenaOsoba = new Osoba.Builder()
                    .withIme(ime)
                    .withPrezime(prezime)
                    .withZupanija(zupanija)
                    .withZarazenBolescu(bolest)
                    .withKontaktiraneOsobe(kontaktiraneOsobe)
                    .withDatumRodjenja(datumRodjenjaLocal)
                    .build();
        }

        List<Long> listaIdKontaktiranih = dohvatiIdKontaktiranihOsoba(identifikator, veza);
        List<Osoba> listaSvihOsoba = dohvatiSveOsobe();
        for (Long idKontakta : listaIdKontaktiranih) {
            for (Osoba kontaktOsoba : listaSvihOsoba) {
                if (idKontakta.equals(kontaktOsoba.getId())) {
                    trazenaOsoba.getKontaktiraneOsobe().add(kontaktOsoba);
                }
            }
        }

        zatvaranjeVeze(veza);
        return trazenaOsoba;
    }

    /**
     * Sprema novi objekt tipa <code>Osoba</code> u bazu podataka.
     *
     * @param osoba nova županija koja se unosi u bazu podataka
     * @throws SQLException baca se u slučaju neuspješnog otvaranja/zatvaranja veze ili neuspješnog izvršavanja upita
     */
    public static void spremanjeNoveOsobe(Osoba osoba) throws SQLException {
        Connection veza = otvaranjeVeze();

        PreparedStatement upit = veza.prepareStatement
                ("INSERT INTO OSOBA(IME, PREZIME, DATUM_RODJENJA, ZUPANIJA_ID, BOLEST_ID)" +
                        "VALUES(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        upit.setString(1, osoba.getIme());
        upit.setString(2, osoba.getPrezime());
        Date datumRodjenjaDate = Date.valueOf(osoba.getDatumRodjenja());
        upit.setDate(3, datumRodjenjaDate);
        upit.setLong(4, osoba.getZupanija().getId());
        upit.setLong(5, osoba.getZarazenBolescu().getId());
        upit.executeUpdate();

        ResultSet resultSet = upit.getGeneratedKeys();

        if (resultSet.next()) {
            Long idDodaneOsobe = Long.parseLong(resultSet.getString(1));

            for (Osoba moguciKontakt : osoba.getKontaktiraneOsobe()) {
                PreparedStatement upitZaKontakte =
                        veza.prepareStatement("INSERT INTO KONTAKTIRANE_OSOBE(OSOBA_ID, KONTAKTIRANA_OSOBA_ID)" +
                                "VALUES (?, ?)");
                upitZaKontakte.setLong(1, idDodaneOsobe);
                upitZaKontakte.setLong(2, moguciKontakt.getId());
                upitZaKontakte.executeUpdate();
            }
        }

        zatvaranjeVeze(veza);
    }

    // ------------------------------1. ZADATAK------------------------------
    public static void uklanjanjeZupanije(Long idZupanije) throws SQLException {
        Connection veza = otvaranjeVeze();
        PreparedStatement upit = veza.prepareStatement("DELETE FROM KONTAKTIRANE_OSOBE\n" +
                "\n" +
                "WHERE OSOBA_ID IN\n" +
                "\n" +
                "(SELECT ID FROM OSOBA WHERE ZUPANIJA_ID = ?)\n" +
                "\n" +
                "OR KONTAKTIRANA_OSOBA_ID IN\n" +
                "\n" +
                "(SELECT ID FROM OSOBA WHERE ZUPANIJA_ID = ?);\n" +
                "\n" +
                "DELETE FROM OSOBA WHERE ZUPANIJA_ID = ?;\n" +
                "DELETE FROM ZUPANIJA WHERE ID = ?;");

        upit.setLong(1, idZupanije);
        upit.setLong(2, idZupanije);
        upit.setLong(3, idZupanije);  //AKO RADI, NE DIRAM :) Puno vremena mi je oduzela ova greška
        upit.setLong(4, idZupanije);
        upit.executeUpdate();

        zatvaranjeVeze(veza);
    }

    // ------------------------------2. ZADATAK------------------------------
    public static List<Osoba> popisOsobaOdredjeneZupanije(Long idZupanijeOsoba) throws SQLException {
        Connection veza = otvaranjeVeze();
        List<Osoba> listaOsoba = new ArrayList<>();
        PreparedStatement upit = veza.prepareStatement("SELECT OSOBA.* FROM OSOBA WHERE ZUPANIJA_ID = ?;");
        upit.setLong(1, idZupanijeOsoba);
        ResultSet resultSet = upit.executeQuery();

        while (resultSet.next()) {
            Long idOsobe = resultSet.getLong("id");
            String ime = resultSet.getString("ime");
            String prezime = resultSet.getString("prezime");
            Date datumRodjenja = resultSet.getDate("datum_rodjenja");
            LocalDate datumRodjenjaLocal = datumRodjenja.toLocalDate();
            Long idZupanije = resultSet.getLong("zupanija_id");
            Long idBolesti = resultSet.getLong("bolest_id");
            Zupanija zupanija = dohvacanjeZupanijePremaId(idZupanije);
            Bolest bolest = dohvacanjeBolestiPremaId(idBolesti);
            List<Osoba> kontaktiraneOsobe = new ArrayList<>();

            listaOsoba.add(new Osoba.Builder()
                    .withId(idOsobe)
                    .withIme(ime)
                    .withPrezime(prezime)
                    .withZupanija(zupanija)
                    .withZarazenBolescu(bolest)
                    .withKontaktiraneOsobe(kontaktiraneOsobe)
                    .withDatumRodjenja(datumRodjenjaLocal)
                    .withStarost(datumRodjenjaLocal)
                    .build());
        }

        zatvaranjeVeze(veza);
        return listaOsoba;
    }

    public static void main(String[] args) {

    }
}
