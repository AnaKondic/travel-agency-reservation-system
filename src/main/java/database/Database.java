package database;

import agencija.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Database {
    private static String DB_user = "root";
    private static String DB_password = "";
    private static String connectionUrl;
    private static int port = 3306;
    private static String DB_name = "agencija";
    private static Connection connection;
    public static List<Admin> admini = retrieveDataFromTable("admin", Admin.class);
    public static List<Klijent> klijenti = retrieveDataFromTable("klijent", Klijent.class);
    public static List<BankovniRacun> racuni = retrieveDataFromTable("bankovni_racun", BankovniRacun.class);
    public static List<Aranzman> aranzmani = retrieveDataFromTable("aranzman", Aranzman.class);
    public static List<Rezervacija> rezervacije = retrieveDataFromTable("rezervacija", Rezervacija.class);
    public static List<Smjestaj> smjestaji = retrieveDataFromTable("smjestaj", Smjestaj.class);

    public static void DBConnect() throws SQLException /*, ClassNotFoundException*/ {
        //Class.forName("com.mysql.cj.jdbc.Driver");
        connectionUrl = "jdbc:mysql://localhost" + ":" + port + "/" + DB_name;
        connection = DriverManager.getConnection(connectionUrl, DB_user, DB_password);
    }

    public static void main(String[] args) {
        try {
            DBConnect();
            System.out.println("Uspjesno ste se konektovali na bazu:" + connectionUrl);
            ResultSet resultSet = null;
            Statement statement = connection.createStatement();
            statement.close();
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        } //catch (ClassNotFoundException e) {
            //throw new RuntimeException(e);
        //}
    }
    public static void addClientToDatabase(int id, String ime, String prezime, String broj_telefona, String jmbg, String broj_racuna, String korisnicko_ime, String lozinka){
        try {
            DBConnect();
            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO `klijent` (id,ime,prezime,broj_telefona,jmbg, broj_racuna, korisnicko_ime, lozinka) VALUE (?,?,?,?,?,?,?,?)");
            pstmt.setInt(1, id);
            pstmt.setString(2, ime);
            pstmt.setString(3, prezime);
            pstmt.setString(4, broj_telefona);
            pstmt.setString(5, jmbg);
            pstmt.setString(6, broj_racuna);
            pstmt.setString(7, korisnicko_ime);
            pstmt.setString(8, lozinka);
            pstmt.executeUpdate();

            klijenti.add(new Klijent(id, ime, prezime, korisnicko_ime, lozinka, broj_telefona, jmbg, broj_racuna));

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void addAdminToDataBase(int id, String ime, String prezime, String korisnicko_ime, String lozinka){
        try {
            DBConnect();
            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO `admin` (id, ime, prezime, korisnicko_ime, lozinka) VALUE (?,?,?,?,?)");
            pstmt.setInt(1, id);
            pstmt.setString(2, ime);
            pstmt.setString(3, prezime);
            pstmt.setString(4, korisnicko_ime);
            pstmt.setString(5, lozinka);
            pstmt.executeUpdate();

            admini.add(new Admin(id, ime, prezime, korisnicko_ime, lozinka));

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void changeAdminPassword(String username, String newPassword){
        try {
            DBConnect();
            String updateQuery = "UPDATE admin SET lozinka = ? WHERE korisnicko_ime = ?";
            PreparedStatement pstmt = connection.prepareStatement(updateQuery);
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);
            pstmt.executeUpdate();

            admini = retrieveDataFromTable("admin", Admin.class);

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void changeClientPassword(String username, String newPassword){
        try {
            DBConnect();
            String updateQuery = "UPDATE klijent SET lozinka = ? WHERE korisnicko_ime = ?";
            PreparedStatement pstmt = connection.prepareStatement(updateQuery);
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);
            pstmt.executeUpdate();

            klijenti = retrieveDataFromTable("klijent", Klijent.class);

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void addArrangementToDataBase(String id, String naziv, String destinacija, String prevoz, Date datum_polaska, Date datum_dolaska, String cijena, Integer smjestaj){
        try {
            DBConnect();

            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO `aranzman` (id, naziv_putovanja, destinacija, prevoz, datum_polaska, datum_dolaska, cijena_aranzmana, Smjestaj_id) VALUE (?,?,?,?,?,?,?,?)");
            pstmt.setString(1, id);
            pstmt.setString(2, naziv);
            pstmt.setString(3, destinacija);
            pstmt.setString(4, prevoz);
            pstmt.setDate(5, (java.sql.Date) datum_polaska);
            pstmt.setDate(6, (java.sql.Date) datum_dolaska);
            pstmt.setString(7, cijena);
            pstmt.setObject(8, smjestaj);

            pstmt.executeUpdate();

            java.sql.Date polazak = (java.sql.Date) datum_polaska;
            java.sql.Date dolazak = (java.sql.Date) datum_dolaska;


            aranzmani.add(new Aranzman(id, naziv, destinacija, prevoz, cijena, polazak, dolazak, smjestaj));

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void addLodgingToDataBase(String naziv, String vrsta_sobe, int id, String broj_zvjezdica, String cjena_po_nocenju) {
        try {
            DBConnect();

            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO `smjestaj` (id, naziv, broj_zvjezdica, vrsta_sobe, cjena_po_nocenju) VALUE (?,?,?,?,?)");
            pstmt.setInt(1, id);
            pstmt.setString(2, naziv);
            pstmt.setString(3, broj_zvjezdica);
            pstmt.setString(4, vrsta_sobe);
            pstmt.setString(5, cjena_po_nocenju);

            pstmt.executeUpdate();

            smjestaji.add(new Smjestaj(naziv, vrsta_sobe, id, broj_zvjezdica, cjena_po_nocenju));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void addReservationToDataBase(int Klijent_id, String Aranzman_id, String ukupna_cijena, String placena_cijena){
        try {
            DBConnect();

            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO `rezervacija` (Klijent_id, Aranzman_id, ukupna_cijena, placena_cijena) VALUE (?,?,?,?)");
            pstmt.setInt(1, Klijent_id);
            pstmt.setString(2, Aranzman_id);
            pstmt.setString(3, ukupna_cijena);
            pstmt.setString(4, placena_cijena);

            pstmt.executeUpdate();

            rezervacije.add(new Rezervacija(Klijent_id, Aranzman_id, ukupna_cijena, placena_cijena));

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void cancelArrangement(String id) throws SQLException{
        try {
            DBConnect();

            PreparedStatement updateAranzman = null;

            connection.setAutoCommit(false);

            String updateQuery = "UPDATE aranzman SET cijena_aranzmana = ? WHERE id = ?";
            updateAranzman = connection.prepareStatement(updateQuery);
            updateAranzman.setString(1, "-1");
            updateAranzman.setString(2, id);
            updateAranzman.executeUpdate();

            connection.commit();

            aranzmani = retrieveDataFromTable("aranzman", Aranzman.class);

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void clientGotMessage(String id){
        try {
            DBConnect();
            for (Aranzman aranzman : aranzmani){
                if (aranzman.getId().equals(id)){
                    String updateQuery = "UPDATE aranzman SET cijena_aranzmana = ? WHERE id = ?";
                    PreparedStatement pstmt1 = connection.prepareStatement(updateQuery);
                    pstmt1.setString(1, "0");
                    pstmt1.setString(2, id);
                    pstmt1.executeUpdate();

                    String updateQueryRez = "UPDATE rezervacija SET placena_cijena = ? WHERE Aranzman_id = ?";
                    PreparedStatement pstmt2 = connection.prepareStatement(updateQueryRez);
                    pstmt2.setString(1, "0");
                    pstmt2.setString(2, id);
                    pstmt2.executeUpdate();

                    aranzmani = retrieveDataFromTable("aranzman", Aranzman.class);
                    rezervacije = retrieveDataFromTable("rezervacija", Rezervacija.class);
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void clientCancelReservation(int clientId, String arrId){
        try {
            DBConnect();

            PreparedStatement updateReservation = null;

            connection.setAutoCommit(false);

            String updateQuery = "UPDATE rezervacija SET placena_cijena = ? WHERE Klijent_id = ? AND Aranzman_id = ?";
            updateReservation = connection.prepareStatement(updateQuery);
            updateReservation.setString(1, "-1");
            updateReservation.setInt(2, clientId);
            updateReservation.setString(3, arrId);
            updateReservation.executeUpdate();

            connection.commit();

            rezervacije = retrieveDataFromTable("rezervacija", Rezervacija.class);

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void updateBankAccountBalance(String jmbg, String amount, String operation){
        try {
            DBConnect();

            PreparedStatement updateBank = null;

            connection.setAutoCommit(false);

            BigDecimal newAmount = new BigDecimal(amount);

            BigDecimal currBalance = null;
            for (BankovniRacun racun : racuni){
                if (racun.getJmbg().equals(jmbg)){
                    currBalance = racun.getStanje();
                }
            }

            BigDecimal newBalance = null;
            if (operation.equals("add")){
                assert currBalance != null;
                newBalance = currBalance.add(newAmount);
            }else if (operation.equals("sub")){
                assert currBalance != null;
                newBalance = currBalance.subtract(newAmount);
            }

            String updateQuery = "UPDATE bankovni_racun SET stanje = ? WHERE jmbg = ?";
            updateBank = connection.prepareStatement(updateQuery);
            updateBank.setBigDecimal(1, newBalance);
            updateBank.setString(2, jmbg);

            updateBank.executeUpdate();

            connection.commit();

            racuni = retrieveDataFromTable("bankovni_racun", BankovniRacun.class);

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void updateReservation(int clientId, String arrId, String updatedPrice){
        try {
            DBConnect();

            PreparedStatement updateReservation = null;

            connection.setAutoCommit(false);

            String updateQuery = "UPDATE rezervacija SET placena_cijena = ? WHERE Klijent_id = ? AND Aranzman_id = ?";
            updateReservation = connection.prepareStatement(updateQuery);
            updateReservation.setString(1, updatedPrice);
            updateReservation.setInt(2, clientId);
            updateReservation.setString(3, arrId);
            updateReservation.executeUpdate();

            connection.commit();

            rezervacije = retrieveDataFromTable("rezervacija", Rezervacija.class);


        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static <T> List<T> retrieveDataFromTable(String tableName, Class<T> clazz){
        List<T> dataList = new ArrayList<>();
        try {
           DBConnect();
           String query = "SELECT * FROM " + tableName;
           try (Statement statement = connection.createStatement()){
               try (ResultSet resultSet = statement.executeQuery(query)){
                    while (resultSet.next()){
                        T dataInstances = mapResultSetToClassInstance(resultSet, clazz);
                        dataList.add(dataInstances);
                    }
               }
           }
       }catch (SQLException e){
           e.printStackTrace();
       }
        return dataList;
    }

    public static <T> T mapResultSetToClassInstance(ResultSet resultSet, Class<T> clazz) throws SQLException{
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field f: fields){
                f.setAccessible(true);
                String fieldName = f.getName();
                Object value = resultSet.getObject(fieldName);
                f.set(instance, value);
            }
            return instance;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
