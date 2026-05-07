package com.example.agencija;

import agencija.*;
import database.Database;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AdminPage implements Initializable{
    @FXML
    Label adminUsername;
    @FXML
    Label adminFullName;
    @FXML
    Label adminCounter;
    @FXML
    Label adminsCount;
    @FXML
    Label clientsCount;
    @FXML
    Label bookingsCount;
    @FXML
    Label moneyCount;
    @FXML
    Label dueCount;
    static Label staticAdminUsername;
    static Label staticAdminFullName;
    @FXML
    Button btLogOut;
    @FXML
    HBox addAdminView;
    @FXML
    VBox dashboardView;
    @FXML
    VBox reservationsView;
    @FXML
    VBox arrangementsView;
    @FXML
    Button btnDashboard;
    @FXML
    Button btnAddAdmin;
    @FXML
    Button btnReservations;
    @FXML
    Button btnArrangements;
    @FXML
    Button btnChangePass;
    @FXML
    TextField tfAdminName;
    @FXML
    TextField tfAdminLastName;
    @FXML
    TextField tfAdminUsername;
    @FXML
    Button btClear;
    @FXML
    Button btUpdate;
    @FXML
    TableView<agencija.Admin> adminTableView;
    @FXML
    TableColumn<Admin, String> idColumn;
    @FXML
    TableColumn<Admin, String> nameColumn;
    @FXML
    TableColumn<Admin, String> lastNameColumn;
    @FXML
    TableColumn<Admin, String> usernameColumn;
    @FXML
    TableColumn<Admin, String> passwordColumn;
    @FXML
    TableView<Rezervacija> reservationsTableView;
    @FXML
    TableColumn<Rezervacija, Integer> clientIdColumn;
    @FXML
    TableColumn<Rezervacija, String> arrangementIdColumn;
    @FXML
    TableColumn<Rezervacija, String> totalPriceColumn;
    @FXML
    TableColumn<Rezervacija, String> paymentColumn;
    @FXML
    TableColumn<Rezervacija, String> contactColumn;
    @FXML
    TextArea reservationsTxtArea;
    @FXML
    AnchorPane passwordField;
    @FXML
    TextField tfOldPassword;
    @FXML
    TextField tfNewPassword;
    @FXML
    Button btnPassFieldClose;
    @FXML
    Button btnPassFieldUpdate;
    @FXML
    Label reservationsMoney;
    @FXML
    Label reservationsDue;
    @FXML
    Label selectedArr;
    @FXML
    Label infoMessage;
    @FXML
    TextArea arrTextArea;
    @FXML
    AnchorPane arrView1;
    @FXML
    AnchorPane arrView2;
    @FXML
    AnchorPane arrView3;
    @FXML
    AnchorPane arrView4;
    @FXML
    Button btnAddNew;
    @FXML
    Button btnCancelArr;
    @FXML
    Button btnAddArr1;
    @FXML
    Button btnGoBack1;
    @FXML
    Button btnAddArr2;
    @FXML
    Button btnGoBack2;
    @FXML
    Button btnGoBack3;
    @FXML
    RadioButton rbOneDay1;
    @FXML
    RadioButton rbExcursion1;
    @FXML
    RadioButton rbOneDay2;
    @FXML
    RadioButton rbExcursion2;
    @FXML
    TextField tfArrName1;
    @FXML
    TextField tfDestArr1;
    @FXML
    TextField tfDepDate1;
    @FXML
    TextField tfPrice1;
    @FXML
    TextField tfArrName2;
    @FXML
    TextField tfDestArr2;
    @FXML
    TextField tfDepDate2;
    @FXML
    TextField tfPrice2;
    @FXML
    TextField tfRetDate;
    @FXML
    TextField tfLodging;
    @FXML
    TextField tfStar;
    @FXML
    TextField tfNightPrice;
    @FXML
    ChoiceBox<String> cbTransport;
    @FXML
    ChoiceBox<String> cbRoom;
    @FXML
    ChoiceBox<String> cbArrangements;
    @FXML
    Button btnCancel;
    @FXML
    Button btnExit;
    @FXML
    Button btnMinimize;
    @FXML
    Button btnBack;
    @FXML
    Button btnForward;

    public static List<Rezervacija> updatedResList = updateReservationList();
    public static List<Aranzman> updatedArrangList = updatetArrangementsList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        staticAdminUsername = adminUsername;
        staticAdminFullName = adminFullName;

        adminCounter.setText(Database.admini.size() + " admins");
        adminsCount.setText(Database.admini.size()+"");
        clientsCount.setText(Database.klijenti.size()+"");
        bookingsCount.setText(updatedResList.size()+"");
        totalMoneyCount();
        totalBalanceDue();

        addAdminListData();
        addReservationslListData();

        showArrangements();

        rbOneDay1.setSelected(true);
        rbExcursion2.setSelected(true);

        ToggleGroup tg1 = new ToggleGroup();
        rbOneDay1.setToggleGroup(tg1);
        rbExcursion1.setToggleGroup(tg1);
        ToggleGroup tg2 = new ToggleGroup();
        rbOneDay2.setToggleGroup(tg2);
        rbExcursion2.setToggleGroup(tg2);

        cbTransport.getItems().addAll("Avion", "Autobus", "Samostalan");
        cbRoom.getItems().setAll("Jednokrevetna", "Dvokrevetna", "Trokrevetna", "Apartman");
    }

    public static List<Rezervacija> updateReservationList(){
        List<Rezervacija> newList = new ArrayList<>();
        for (Rezervacija rezervacija : Database.rezervacije){
            for (Aranzman aranzman : Database.aranzmani){
                if (aranzman.getId().equals(rezervacija.getAranzman_id())){
                    if(!(aranzman.getCijena_aranzmana().equals("-1") || rezervacija.getPlacena_cijena().equals("0") || rezervacija.getPlacena_cijena().equals("-1")) && !checkIfPast(aranzman.getDatum_polaska())){
                        newList.add(rezervacija);
                    }
                }
            }
        }

        return newList;
    }

    public static List<Aranzman> updatetArrangementsList(){
        List<Aranzman> newList = new ArrayList<>();
        for (Aranzman aranzman : Database.aranzmani){
            if (!(aranzman.getCijena_aranzmana().equals("0") || aranzman.getCijena_aranzmana().equals("-1")) && !checkIfPast(aranzman.getDatum_polaska())){
                newList.add(aranzman);
            }
        }

        return newList;
    }

    public void totalMoneyCount(){
        Database.racuni = Database.retrieveDataFromTable("bankovni_racun", BankovniRacun.class);
        for (BankovniRacun racun : Database.racuni){
            if (racun.getJmbg().equals("1102541293")){
                moneyCount.setText(racun.getStanje()+"KM");
                reservationsMoney.setText(racun.getStanje()+"KM");
            }
        }
    }

    public void totalBalanceDue(){
        double sum = 0;
        for (Rezervacija rezervacija : updatedResList){
            sum += Double.parseDouble(rezervacija.getUkupna_cijena());
            sum -= Double.parseDouble(rezervacija.getPlacena_cijena());
        }
        dueCount.setText(sum+"KM");
        reservationsDue.setText(sum+"KM");
    }

    /*--------------------------- Change password field ------------------------------*/
    public void closePasswordChange(ActionEvent event){
        passwordField.setVisible(false);
        clearPasswordFields();
        tfOldPassword.setStyle("");
        tfNewPassword.setStyle("");
    }

    public void updatePasswordChange(ActionEvent event){
        String password = getAdminPassword();
        if(tfOldPassword.getText().isEmpty() && tfNewPassword.getText().isEmpty()){
            textFieldError(tfOldPassword);
            textFieldError(tfNewPassword);
        }
        else if (tfOldPassword.getText().isEmpty() || !password.equals(tfOldPassword.getText().trim())){
            textFieldError(tfOldPassword);
        } else if (tfNewPassword.getText().isEmpty()) {
            textFieldError(tfNewPassword);
        }else {
            Database.changeAdminPassword(adminUsername.getText().trim(), tfNewPassword.getText().trim());
            addAdminListData();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information message");
            alert.setHeaderText(null);
            alert.setContentText("You have successfully changed Your password!");
            alert.showAndWait();

            tfNewPassword.setStyle("");
            tfOldPassword.setStyle("");
            clearPasswordFields();

        }
    }

    private String getAdminPassword(){
        for (Admin admin : Database.admini){
            if(adminUsername.getText().trim().equals(admin.getKorisnicko_ime())){
                return admin.getLozinka();
            }
        }
        return "";
    }

    private void textFieldError(TextField tf){
        tf.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
    }

    private void clearPasswordFields(){
        tfOldPassword.clear();
        tfNewPassword.clear();
    }

    /*--------------------------- Add admin page ------------------------------*/

    // Dodajemo podatke u tabelu za admine
    public void addAdminListData(){
        ObservableList<Admin> adminListData = FXCollections.observableArrayList();
        for (Admin admin : Database.admini){
            adminListData.add(admin);
        }

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("ime"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("prezime"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("korisnicko_ime"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("lozinka"));

        adminTableView.setItems(adminListData);
    }

    public void clearAddAdminFields(ActionEvent event){
        clearTextFields();
    }

    public void updateAddAdminFields(ActionEvent event){
        List<String> adminUsernames = new ArrayList<>();
        for (Admin a : Database.admini){
            adminUsernames.add(a.getKorisnicko_ime());
        }

        Alert alert;

        try {
            if(tfAdminName.getText().isEmpty() || tfAdminLastName.getText().isEmpty() || tfAdminUsername.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else if (adminUsernames.contains(tfAdminUsername.getText().trim())) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Username " + tfAdminUsername.getText() + " already exists!");
                alert.showAndWait();
            }else {
                int id = Database.admini.size() + 1;
                String name = tfAdminName.getText().trim();
                String lastName = tfAdminLastName.getText().trim();
                String username = tfAdminUsername.getText().trim();
                String password = "12345678";

                Database.addAdminToDataBase(id, name, lastName, username, password);
                addAdminListData();
                int counter = Integer.parseInt(adminsCount.getText())+1;
                adminsCount.setText(counter+"");
                adminCounter.setText(counter+" admins");

                clearTextFields();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void clearTextFields(){
        tfAdminName.setText("");
        tfAdminLastName.setText("");
        tfAdminUsername.setText("");
    }

    /*--------------------------- Reservations page ------------------------------*/

    // Dodajemo podatke u tabelu za rezervacije
    public void addReservationslListData(){
        ObservableList<Rezervacija> reservationsListData = FXCollections.observableArrayList();
        for (Rezervacija rezervacija : updatedResList){
            reservationsListData.add(rezervacija);
        }

        clientIdColumn.setCellValueFactory(new PropertyValueFactory<>("Klijent_id"));
        arrangementIdColumn.setCellValueFactory(new PropertyValueFactory<>("Aranzman_id"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("ukupna_cijena"));
        paymentColumn.setCellValueFactory(new PropertyValueFactory<>("placena_cijena"));

        reservationsTableView.setItems(reservationsListData);

        // Popunjavamo kolonu contactColumn sa brojevima telefona onih klijenata za ciju rezervaciju istice rok za uplatu
        contactColumn.setCellValueFactory(cellData -> {
            Rezervacija rezervacija = cellData.getValue();

            String contact = "";
            for (Aranzman aranzman : Database.aranzmani){
                if(aranzman.getId().equals(rezervacija.getAranzman_id())){
                    Date date = aranzman.getDatum_polaska();
                    double totalPrice = Double.parseDouble(rezervacija.getUkupna_cijena());
                    double payment = Double.parseDouble(rezervacija.getPlacena_cijena());
                    if ((checkDate(date) && payment == totalPrice/2) || (checkIfThreeDaysLeft(date) && payment!=totalPrice)) {
                      for (Klijent klijent : Database.klijenti){
                          if (klijent.getId() == rezervacija.getKlijent_id()){
                              contact = klijent.getBroj_telefona();
                          }
                      }
                    }else {
                        contact = "/";
                    }
                }
            }

            return new SimpleStringProperty(String.valueOf(contact));
        });

        // Posebno oznacimo redove u tabeli koji sadrze rezervacija za ciju uplatu je ostalo jos 3 dana ili manje
        reservationsTableView.setRowFactory(tv -> new TableRow<Rezervacija>() {
            @Override
            protected void updateItem(Rezervacija item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    for (Aranzman aranzman : Database.aranzmani){
                        if(aranzman.getId().equals(item.getAranzman_id())){
                            Date date = aranzman.getDatum_polaska();
                            double totalPrice = Double.parseDouble(item.getUkupna_cijena());
                            double payment = Double.parseDouble(item.getPlacena_cijena());

                            if ((checkDate(date) && payment == totalPrice/2) || (checkIfThreeDaysLeft(date) && payment!=totalPrice)) {
                                setStyle("-fx-background-color: #DF7857;");
                            } else {
                                setStyle("");
                            }
                        }
                    }
                }
            }
        });
    }


    // Uzimamo podatke iz tabele za rezervacije i smjestamo u TextArea-u
    public void getTableItems(MouseEvent event){
        int index = reservationsTableView.getSelectionModel().getSelectedIndex();
        if (index <= -1) return;

        String arrangementName = null;

        reservationsTxtArea.clear();
        infoMessage.setText("");

        for (Aranzman aranzman : Database.aranzmani){
            if(aranzman.getId().equals(arrangementIdColumn.getCellData(index))){
                arrangementName = aranzman.getNaziv_putovanja();
                reservationsTxtArea.appendText("'" + arrangementName + "'" + " was booked by following clients:\n\n");
                break;
            }
        }

        for (Rezervacija rezervacija : updatedResList){
            if (rezervacija.getAranzman_id().equals(arrangementIdColumn.getCellData(index))){
                int numeration = 0;
                for (Klijent klijent : Database.klijenti){
                    if(klijent.getId() == rezervacija.getKlijent_id()){
                        numeration++;
                        double totalPrice = Double.parseDouble(rezervacija.getUkupna_cijena());
                        double payment = Double.parseDouble(rezervacija.getPlacena_cijena());
                        double due = totalPrice - payment;
                        for (Aranzman aranzman : Database.aranzmani){
                            if (aranzman.getId().equals(rezervacija.getAranzman_id())){
                                selectedArr.setText(aranzman.getNaziv_putovanja());

                                Date date = aranzman.getDatum_polaska();
                                if((checkDate(date) && payment == totalPrice/2) || (checkIfThreeDaysLeft(date) && payment!=totalPrice)){
                                    reservationsTxtArea.appendText(numeration+". " + klijent.getIme() + " " + klijent.getPrezime() + " (" + klijent.getBroj_telefona()+ ")" + " paid " + payment + "KM and owes "+ due + "KM\n");
                                }else {
                                    reservationsTxtArea.appendText(numeration+". " + klijent.getIme() + " " + klijent.getPrezime() + " paid " + payment + "KM and owes "+ due + "KM\n");
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    private boolean checkDate(Date departureDate){
        Date currentDate = new Date();
        long diffInMillies = departureDate.getTime() - currentDate.getTime();
        long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);
        if (diffInDays > 14 && diffInDays <= 17){
            return true;
        }else {
            return false;
        }
    }

    private static boolean checkIfThreeDaysLeft(Date departureDate){
        Date currentDate = new Date();
        long diffInMillies = departureDate.getTime() - currentDate.getTime();
        long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);
        if (diffInDays >0 && diffInDays<=3){
            return true;
        }else {
            return false;
        }
    }

    private static boolean checkIfPast(Date date){
        Date currDate = new Date();
        if (currDate.after(date)){
            return true;
        }else {
            return false;
        }
    }

    /*--------------------------- Arrangements page ------------------------------*/
    public void showArrangements(){
        int num = 0;
        arrTextArea.clear();
        cbArrangements.getItems().clear();
        for (Aranzman aranzman : Database.aranzmani) {
            if (!(aranzman.getCijena_aranzmana().equals("-1") || aranzman.getCijena_aranzmana().equals("0"))) {
                num++;
                arrTextArea.appendText(num + ". " + aranzman);
                cbArrangements.getItems().add(aranzman.getNaziv_putovanja());
                if (aranzman.getSmjestaj_id() != null ){
                    for (Smjestaj smjestaj : Database.smjestaji){
                        if(aranzman.getSmjestaj_id() == smjestaj.getId()){
                            arrTextArea.appendText(", " + smjestaj.getNaziv());
                        }
                    }
                }
                arrTextArea.appendText("\n");
            }
        }
    }

    public void setNewArrView(ActionEvent event){
        arrView1.setVisible(false);
        arrView2.setVisible(true);
    }

    public void setCancelArrView(ActionEvent event){
        arrView1.setVisible(false);
        arrView4.setVisible(true);
    }

    public void goBack(ActionEvent event){
        arrView1.setVisible(true);
        arrView2.setVisible(false);
        arrView3.setVisible(false);
        arrView4.setVisible(false);
        clearArrFields();
    }

    public void setArrView3(ActionEvent event){
        arrView2.setVisible(false);
        arrView3.setVisible(true);
        rbExcursion2.setSelected(true);
    }

    public void setArrView2(ActionEvent event){
        arrView2.setVisible(true);
        arrView3.setVisible(false);
        rbOneDay1.setSelected(true);
    }

    public void addOneDayArr(ActionEvent event){
        Alert alert;
        try {
            if(tfArrName1.getText().isEmpty() || tfDestArr1.getText().isEmpty() || tfDepDate1.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill out all required fields!");
                alert.showAndWait();
                throw new RuntimeException("Please fill out all required fields!");
            }else {
                String id = (Database.aranzmani.size()+1)+"";
                String name = tfArrName1.getText().trim();
                String destination = tfDestArr1.getText().trim();
                String departure = tfDepDate1.getText().trim();
                String price = tfPrice1.getText().trim();

                java.sql.Date date = convertDepartureDate(departure);
                isNumeric(price);

                Database.addArrangementToDataBase(id, name, destination, null, date, date, price, null);
                updatedArrangList = updatetArrangementsList();
                showArrangements();

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText("");
                alert.setContentText("You've successfully added new arrangement.");
                alert.showAndWait();

                clearArrFields();
            }
        }catch (Exception e){
            String message = e.getMessage();
            System.out.println(message);
        }
    }

    public void addPackageArr(ActionEvent event){
        Alert alert;
        try {
            if(tfArrName2.getText().isEmpty() || tfDestArr2.getText().isEmpty() || tfDepDate2.getText().isEmpty() ||  cbTransport.getValue()==null || cbRoom.getValue() == null || tfRetDate.getText().isEmpty() || tfLodging.getText().isEmpty() || tfStar.getText().isEmpty() || tfNightPrice.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill out all required fields!");
                alert.showAndWait();
                throw new RuntimeException("Please fill out all required fields!");
            }else {
                int id = Database.smjestaji.size()+1;
                String lodging = tfLodging.getText().trim();
                String room = cbRoom.getValue().trim();
                String stars = tfStar.getText().trim();
                String pricePerNight = tfNightPrice.getText().trim();

                isNumeric(pricePerNight);
                isNumeric(stars);

                Database.addLodgingToDataBase(lodging,room,id,stars,pricePerNight);
                Database.smjestaji.add(new Smjestaj(lodging, room, id, stars,pricePerNight));

                String idArr = (Database.aranzmani.size()+1)+"";
                String name = tfArrName2.getText().trim();
                String destination = tfDestArr2.getText().trim();
                String transport = cbTransport.getValue().trim();
                String departure = tfDepDate2.getText().trim();
                String returnDate = tfRetDate.getText().trim();
                String price = tfPrice2.getText().trim();

                java.sql.Date date = convertDepartureDate(departure);
                java.sql.Date retDate = convertDepartureDate(returnDate);
                isNumeric(price);

                Database.addArrangementToDataBase(idArr, name, destination, transport, date, retDate, price, id);
                updatedArrangList = updatetArrangementsList();
                showArrangements();

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText("");
                alert.setContentText("You've successfully added new arrangement.");
                alert.showAndWait();

                clearArrFields();
            }
        }catch (Exception e){
            String message = e.getMessage();
            System.out.println(message);
        }
    }

    public void cancelArrangement(ActionEvent event){
        try {
            if(cbArrangements.getValue() == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("You have not chosen any arrangements!");
                alert.showAndWait();
                throw new IllegalArgumentException("You have not chosen any arrangements!\"");
            }else {
                String name = cbArrangements.getValue().trim();
                for (Aranzman aranzman : Database.aranzmani){
                    if(aranzman.getNaziv_putovanja().equals(name)){
                        try {
                            Database.cancelArrangement(aranzman.getId());
                            double moneyLost = moneyLost(aranzman.getId());

                            cbArrangements.setValue(null);
                            updatedResList = updateReservationList();
                            updatedArrangList = updatetArrangementsList();
                            Database.updateBankAccountBalance("1102541293", moneyLost+"", "sub");

                            showArrangements();
                            addReservationslListData();
                            totalMoneyCount();
                            totalBalanceDue();

                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Information Message");
                            alert.setHeaderText("");
                            alert.setContentText("By canceling " + aranzman.getNaziv_putovanja() + " agency has lost " + moneyLost + "KM.");
                            alert.showAndWait();
                        }catch (SQLException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }catch (IllegalArgumentException e){
            String message = e.getMessage();
            System.out.println(message);
        }
    }

    private java.sql.Date convertDepartureDate(String date) throws ParseException {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            java.sql.Date convereted = new java.sql.Date(sdf.parse(date).getTime());
            return convereted;
        }catch (ParseException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error Message");
            alert.setContentText("Incorrect date format!");
            alert.showAndWait();
            throw new IllegalArgumentException("Incorrect date format!");
        }
    }

    public boolean isNumeric(String str) throws NumberFormatException{
        try {
            Double.parseDouble(str);
            return true;
        }catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error Message");
            alert.setContentText("Invalid input, value is not a number!");
            alert.showAndWait();
            throw new NumberFormatException("Invalid input, value is not a number!");
        }
    }

    private void clearArrFields(){
        tfArrName1.clear();
        tfArrName2.clear();
        tfDestArr1.clear();
        tfDestArr2.clear();
        tfDepDate1.clear();
        tfDepDate2.clear();
        tfPrice1.clear();
        tfPrice2.clear();
        tfRetDate.clear();
        tfLodging.clear();
        tfStar.clear();
        tfNightPrice.clear();

        cbTransport.setValue(null);
        cbRoom.setValue(null);
        cbArrangements.setValue(null);
    }

    private double moneyLost(String id){
        double sum = 0;
        for (Rezervacija rezervacija : Database.rezervacije){
            if(rezervacija.getAranzman_id().equals(id) && !rezervacija.getPlacena_cijena().equals("-1")){
                sum += Double.parseDouble(rezervacija.getPlacena_cijena());
            }
        }
        return sum;
    }


    /*--------------------------- Basic Functionalities ------------------------------*/
    public void switchForm(ActionEvent event){
        if(event.getSource() == btnDashboard){
            dashboardView.visibleProperty().set(true);
            addAdminView.visibleProperty().set(false);
            reservationsView.visibleProperty().set(false);
            arrangementsView.visibleProperty().set(false);
        }else if(event.getSource() == btnAddAdmin){
            dashboardView.visibleProperty().set(false);
            addAdminView.visibleProperty().set(true);
            reservationsView.visibleProperty().set(false);
            arrangementsView.visibleProperty().set(false);
        } else if (event.getSource() == btnReservations) {
            dashboardView.visibleProperty().set(false);
            addAdminView.visibleProperty().set(false);
            reservationsView.visibleProperty().set(true);
            arrangementsView.visibleProperty().set(false);
        } else if (event.getSource() == btnArrangements) {
            dashboardView.visibleProperty().set(false);
            addAdminView.visibleProperty().set(false);
            reservationsView.visibleProperty().set(false);
            arrangementsView.visibleProperty().set(true);

            arrView1.setVisible(true);
            arrView2.setVisible(false);
            arrView3.setVisible(false);
            arrView4.setVisible(false);
        } else if (event.getSource() == btnChangePass) {
            passwordField.visibleProperty().set(true);
        }
    }

    public void btnMouseEntered(MouseEvent event){
        Node source = (Node) event.getSource();
        if(source == btnDashboard){
            mouseEnteredMenuBtn(btnDashboard);
        } else if (source == btnAddAdmin) {
            mouseEnteredMenuBtn(btnAddAdmin);
        } else if (source == btnReservations) {
            mouseEnteredMenuBtn(btnReservations);
        } else if (source == btnArrangements) {
            mouseEnteredMenuBtn(btnArrangements);
        } else if (source == btLogOut) {
            mouseEnteredMenuBtn(btLogOut);
        } else if (source == btnChangePass) {
            mouseEnteredMenuBtn(btnChangePass);
        } else if (source == btnMinimize) {
            mouseEnteredMenuBtn(btnMinimize);
        } else if (source == btnExit) {
            mouseEnteredMenuBtn(btnExit);
        } else if (source == btUpdate) {
            mouseEnteredGreenBtn(btUpdate);
        } else if (source == btnPassFieldUpdate) {
            mouseEnteredGreenBtn(btnPassFieldUpdate);
        } else if (source == btnAddNew) {
            mouseEnteredGreenBtn(btnAddNew);
        } else if (source == btnCancelArr) {
            mouseEnteredGreenBtn(btnCancelArr);
        } else if (source == btnAddArr1) {
            mouseEnteredGreenBtn(btnAddArr1);
        } else if (source == btnAddArr2) {
            mouseEnteredGreenBtn(btnAddArr2);
        } else if (source == btnCancel) {
            mouseEnteredGreenBtn(btnCancel);
        } else if (source == btnPassFieldClose) {
            mouseEnteredWhiteBtn(btnPassFieldClose);
        } else if (source == btClear) {
            mouseEnteredWhiteBtn(btClear);
        }else if (source == btnGoBack1) {
            mouseEnteredFuncBtn(btnGoBack1);
        } else if (source == btnGoBack2) {
            mouseEnteredFuncBtn(btnGoBack2);
        } else if (source == btnGoBack3) {
            mouseEnteredFuncBtn(btnGoBack3);
        }else if(source == btnBack){
            mouseEnteredMenuBtn(btnBack);
        } else if (source == btnForward) {
            mouseEnteredMenuBtn(btnForward);
        }
    }

    public void btnMouseExited(MouseEvent event){
        Node source = (Node) event.getSource();
        if(source == btnDashboard){
            mouseExitedMenuBtn(btnDashboard);
        } else if (source == btnAddAdmin) {
            mouseExitedMenuBtn(btnAddAdmin);
        } else if (source == btnReservations) {
            mouseExitedMenuBtn(btnReservations);
        } else if (source == btnArrangements) {
            mouseExitedMenuBtn(btnArrangements);
        } else if (source == btLogOut) {
            mouseExitedMenuBtn(btLogOut);
        } else if (source == btnChangePass) {
            mouseExitedMenuBtn(btnChangePass);
        } else if (source == btnMinimize) {
            mouseExitedMenuBtn(btnMinimize);
        } else if (source == btnExit) {
            mouseExitedMenuBtn(btnExit);
        } else if (source == btUpdate) {
            mouseExitedGreenBtn(btUpdate);
        } else if (source == btnPassFieldUpdate) {
            mouseExitedGreenBtn(btnPassFieldUpdate);
        } else if (source == btnAddNew) {
            mouseExitedGreenBtn(btnAddNew);
        } else if (source == btnCancelArr) {
            mouseExitedGreenBtn(btnCancelArr);
        } else if (source == btnAddArr1) {
            mouseExitedGreenBtn(btnAddArr1);
        } else if (source == btnAddArr2) {
            mouseExitedGreenBtn(btnAddArr2);
        } else if (source == btnCancel) {
            mouseExitedGreenBtn(btnCancel);
        }else if (source == btnPassFieldClose) {
            mouseExitedWhiteBtn(btnPassFieldUpdate);
        } else if (source == btClear) {
            mouseExitedWhiteBtn(btClear);
        } else if (source == btnGoBack1) {
            mouseExitedFuncBtn(btnGoBack1);
        } else if (source == btnGoBack2) {
            mouseExitedFuncBtn(btnGoBack2);
        } else if (source == btnGoBack3) {
            mouseExitedFuncBtn(btnGoBack3);
        }else if(source == btnBack){
            mouseExitedMenuBtn(btnBack);
        } else if (source == btnForward) {
            mouseExitedMenuBtn(btnForward);
        }
    }

    private void mouseEnteredMenuBtn(Button button){
        button.setStyle("-fx-background-color: #C4E7D9;");
    }

    private void mouseExitedMenuBtn(Button button){
        button.setStyle("-fx-background-color: #80BCBD");
        button.setTextFill(Color.valueOf("#fff"));
    }

    private void mouseEnteredWhiteBtn(Button button){
        button.setTextFill(Color.valueOf("#fff"));
        button.setStyle("-fx-background-color: #9ED2BE;");
    }

    private void mouseEnteredFuncBtn(Button button){
        button.setStyle("-fx-background-color: #EBC8A3;");
    }

    private void mouseExitedFuncBtn(Button button){
        button.setStyle("-fx-background-color: #DAAD86");
    }

    private void mouseExitedWhiteBtn(Button button){
        button.setTextFill(Color.valueOf("#9ED2BE"));
        button.setStyle("-fx-background-color: #fff;");
    }

    private void mouseEnteredGreenBtn(Button button){
        button.setTextFill(Color.valueOf("#9ED2BE"));
        button.setStyle("-fx-background-color: #fff;");
    }

    private void mouseExitedGreenBtn(Button button){
        button.setTextFill(Color.valueOf("#fff"));
        button.setStyle("-fx-background-color: #9ED2BE;");
    }

    public void adminLogOut(ActionEvent event) throws IOException{
        Main m = new Main();
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Confirmation Message");
        a.setHeaderText(null);
        a.setContentText("Are you sure you want to log out?");
        a.showAndWait();
        if (a.getResult() == ButtonType.OK){
            m.changeScene("main.fxml");
        }
    }
    /*--------------------------- Back/Forward navigation ------------------------------*/
    public void navigation(ActionEvent event){
        Node source = (Node) event.getSource();
        if (source == btnForward){
            if (dashboardView.isVisible()){
                dashboardView.setVisible(false);
                addAdminView.setVisible(true);
            } else if (addAdminView.isVisible()) {
                addAdminView.setVisible(false);
                reservationsView.setVisible(true);
            } else if (reservationsView.isVisible()) {
                reservationsView.setVisible(false);
                arrangementsView.setVisible(true);
            }
        } else if (source == btnBack) {
            if (arrangementsView.isVisible()){
                arrangementsView.setVisible(false);
                reservationsView.setVisible(true);
            } else if (reservationsView.isVisible()) {
                reservationsView.setVisible(false);
                addAdminView.setVisible(true);
            } else if (addAdminView.isVisible()) {
                addAdminView.setVisible(false);
                dashboardView.setVisible(true);
            }
        }
    }


    /*--------------------------- Exit and Minimize buttons ------------------------------*/
    public void onMinimize(ActionEvent event){
        ((Stage) Main.stage.getScene().getWindow()).setIconified(true);
    }

    public void onExit(ActionEvent event){
        System.exit(0);
    }

}
