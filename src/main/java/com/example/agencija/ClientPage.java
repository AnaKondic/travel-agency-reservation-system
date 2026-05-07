package com.example.agencija;

import agencija.*;
import database.Database;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ClientPage implements Initializable {
    public ClientPage(){}
    @FXML
    Label clientFullName;
    @FXML
    Label clientUsername;
    static Label staticClientUsername;
    static Label staticClientFullName;
    static int clientId;
    static String clientPass;
    @FXML
    Button btLogOut;
    @FXML
    Button btnHome;
    @FXML
    Button btnReservations;
    @FXML
    Button btnArrangements;
    @FXML
    Button btnPassword;
    @FXML
    Button btnBookTour;
    @FXML
    AnchorPane passwordField;
    @FXML
    TextField tfOldPassword;
    @FXML
    TextField tfNewPassword;
    @FXML
    TextField tfAmount;
    @FXML
    TextField tfPrice;
    @FXML
    TextField tfDestination;
    @FXML
    TextField tfStarRating;
    @FXML
    TextField tfDate1;
    @FXML
    TextField tfDate2;
    @FXML
    ChoiceBox<String> cbRoomType;
    @FXML
    ChoiceBox<String> cbTransport;
    @FXML
    PasswordField pfPassword;
    @FXML
    Button btnPassFieldClose;
    @FXML
    Button btnPassFieldUpdate;
    @FXML
    Button btnPay;
    @FXML
    Button btnCancelRes;
    @FXML
    Button btnMinimize;
    @FXML
    Button btnBack;
    @FXML
    Button btnForward;
    @FXML
    Button btnExit;
    @FXML
    Button btnLowPriced;
    @FXML
    Button btnDate;
    @FXML
    Button btnFilter;
    @FXML
    VBox reservationsView;
    @FXML
    RadioButton rbActive;
    @FXML
    RadioButton rbPast;
    @FXML
    RadioButton rbCanceled;
    @FXML
    RadioButton rbOneDay;
    @FXML
    RadioButton rbTravel;
    @FXML
    TableView<Aranzman> resTableView;
    @FXML
    TableColumn<Aranzman, String> idCol;
    @FXML
    TableColumn<Aranzman, String> nameCol;
    @FXML
    TableColumn<Aranzman, String> destCol;
    @FXML
    TableColumn<Aranzman, String> priceCol;
    @FXML
    TableColumn<Aranzman, String> dueCol;
    @FXML
    GridPane arrGridPane;
    @FXML
    ScrollPane arrScrollPane;
    @FXML
    AnchorPane arrangementsView;
    @FXML
    AnchorPane homeView;
    @FXML
    Label infoMessage;
    @FXML
    Label lblResName;
    @FXML
    Label moneySpent;
    public static List<AnchorPane> paneList;
    public static List<Aranzman> availableArrangList;
    public static List<Aranzman> pastArrangList;
    public static List<Aranzman> activeArrangList;
    public static List<Aranzman> canceledArrangList;
    public static List<Rezervacija> activeResList;
    public static List<Rezervacija> pastResList;
    public static List<Rezervacija> canceledResList;
    private ObservableList<Aranzman> cardListData = getCardListData();
    private ObservableList<Aranzman> filteredList = FXCollections.observableArrayList();;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        staticClientUsername = clientUsername;
        staticClientFullName = clientFullName;

        paneList = new ArrayList<>();
        paneList.add(homeView);
        paneList.add(arrangementsView);

        ToggleGroup tg1 = new ToggleGroup();
        rbActive.setToggleGroup(tg1);
        rbPast.setToggleGroup(tg1);
        rbCanceled.setToggleGroup(tg1);
        rbActive.setSelected(true);

        ToggleGroup tg2 = new ToggleGroup();
        rbOneDay.setToggleGroup(tg2);
        rbTravel.setToggleGroup(tg2);

        cbRoomType.getItems().setAll("Jednokrevetna", "Dvokrevetna", "Trokrevetna", "Apartman");
        cbTransport.getItems().setAll("Avion", "Autobus", "Samostalan");

        clientId = LogIn.clientId;
        clientPass = LogIn.clientPass;

        getCancelationMessage();

        availableArrangList = getAvailableArrangements();

        canceledArrangList = getCanceledArrangements();
        canceledResList = getCanceledReservations();

        pastArrangList = getPastArrangements();
        pastResList = getPastReservations();

        activeArrangList = getActiveArrangements();
        activeResList = getActiveReservations();

        expirationDateMessage();

        displayCard(cardListData);
        addActiveResListData();
        moneySpent.setText(getTotalMoneySpent()+"KM");
    }

    /*--------------------------- Arrangements and Reservations Lists ------------------------------*/
    public ObservableList<Aranzman> getCardListData(){
        ObservableList<Aranzman> listData = FXCollections.observableArrayList();
       for (Aranzman aranzman : AdminPage.updatedArrangList){
           if (!checkExpirationDate(aranzman.getDatum_polaska())){
               listData.add(aranzman);
           }
       }

        return listData;
    }

    public static List<Aranzman> getAvailableArrangements(){
        List<Aranzman> newList = new ArrayList<>();
        for (Rezervacija rezervacija : Database.rezervacije){
            if (!rezervacija.getPlacena_cijena().equals("0")){
                for (Aranzman aranzman : Database.aranzmani){
                    if (aranzman.getId().equals(rezervacija.getAranzman_id()) && rezervacija.getKlijent_id()==clientId){
                        newList.add(aranzman);
                    }
                }
            }
        }
        return newList;
    }

    public static List<Aranzman> getActiveArrangements(){
        List<Aranzman> newList = new ArrayList<>();
        for (Aranzman aranzman : availableArrangList){
            if (!(pastArrangList.contains(aranzman) || canceledArrangList.contains(aranzman))){
                newList.add(aranzman);
            }
        }

        return newList;
    }
    public static List<Rezervacija> getActiveReservations(){
        List<Rezervacija> newList = new ArrayList<>();
        for (Aranzman aranzman : activeArrangList){
            for (Rezervacija rezervacija : Database.rezervacije){
                if(rezervacija.getAranzman_id().equals(aranzman.getId()) && rezervacija.getKlijent_id()==clientId){
                    newList.add(rezervacija);
                }
            }
        }
        return newList;
    }

    public static List<Aranzman> getPastArrangements(){
        List<Aranzman> newList = new ArrayList<>();
        for (Aranzman aranzman : availableArrangList){
            if(checkIfPast(aranzman.getDatum_polaska()) && !canceledArrangList.contains(aranzman)){
                newList.add(aranzman);
            }
        }

        return newList;
    }

    public static List<Rezervacija> getPastReservations(){
        List<Rezervacija> newList = new ArrayList<>();
        for (Rezervacija rezervacija : Database.rezervacije){
            for (Aranzman aranzman : pastArrangList){
                if (rezervacija.getAranzman_id().equals(aranzman.getId()) &&  rezervacija.getKlijent_id()==clientId){
                    newList.add(rezervacija);
                }
            }
        }
        return newList;
    }

    public static List<Aranzman> getCanceledArrangements(){
        List<Aranzman> newList = new ArrayList<>();
        for (Aranzman aranzman : availableArrangList){
            for (Rezervacija rezervacija : Database.rezervacije){
                if(rezervacija.getAranzman_id().equals(aranzman.getId()) && rezervacija.getKlijent_id()==clientId){
                    if (rezervacija.getPlacena_cijena().equals("-1")){
                        newList.add(aranzman);
                    }else if(checkIfCanceled(rezervacija, aranzman)) {
                        newList.add(aranzman);
                        Database.updateBankAccountBalance(getClientJmbg(), rezervacija.getPlacena_cijena(), "add");
                        Database.clientCancelReservation(clientId, aranzman.getId());
                    }
                }
            }
        }
        return newList;
    }

    public static List<Rezervacija> getCanceledReservations(){
        List<Rezervacija> newList = new ArrayList<>();
        for (Rezervacija rezervacija : Database.rezervacije){
            for (Aranzman aranzman : canceledArrangList){
                if (rezervacija.getAranzman_id().equals(aranzman.getId()) &&  rezervacija.getKlijent_id()==clientId){
                    newList.add(rezervacija);
                }
            }
        }
        return newList;
    }

    // Provjeravamo da li izlet/putovanje spada u otkazane rezervacije
    private static boolean checkIfCanceled(Rezervacija rezervacija, Aranzman aranzman){
        double totalPrice = Double.parseDouble(rezervacija.getUkupna_cijena());
        double firstPayment = totalPrice/2;
        double payedPrice = Double.parseDouble(rezervacija.getPlacena_cijena());
        Date date = aranzman.getDatum_polaska();

        if ((checkExpirationDate(date) && firstPayment==payedPrice) || (checkIfPast(date) && totalPrice!=payedPrice)){
            return true;
        }else {
            return false;
        }
    }

    // Provjeravamo da li je isteklo vrijeme za uplatu ostatka izleta/putovanja
    private static boolean checkExpirationDate(Date departureDate){
        Date currentDate = new Date();
        long diffInMillies = departureDate.getTime() - currentDate.getTime();
        long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);
        if (diffInDays <= 14){
            return true;
        }else {
            return false;
        }
    }

    // Provjeravamo da li je za uplatu izleta/putovanja ostalo tri dana ili manje
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

    // Provjeravamo da li je protekao datum izleta/putovanja
    private static boolean checkIfPast(Date date){
        Date currDate = new Date();
        if (currDate.after(date)){
            return true;
        }else {
            return false;
        }
    }

    // Stanje bankovnog racuna klijenta
    private BigDecimal getClientAccountBalance(){
        for (Klijent klijent : Database.klijenti){
            if (clientId == klijent.getId()){
                for (BankovniRacun bankovniRacun : Database.racuni){
                    if (bankovniRacun.getBroj_racuna().equals(klijent.getBroj_racuna())){
                        return bankovniRacun.getStanje();
                    }
                }
            }
        }
        return null;
    }

    // Ukupna kolicina novca koji je klijent potrosio na izlete/putovanja
    private static double getTotalMoneySpent(){
        double sum = 0;
        for (Rezervacija rezervacija : activeResList){
            sum += Double.parseDouble(rezervacija.getPlacena_cijena());
        }
        for (Rezervacija rezervacija : pastResList){
            sum += Double.parseDouble(rezervacija.getPlacena_cijena());
        }
        return sum;
    }

    private static String getClientJmbg(){
        for (Klijent klijent : Database.klijenti){
            if (klijent.getId()==clientId){
                return klijent.getJmbg();
            }
        }
        return "";
    }

    // Poruka koja klijenta obavjestava o stanju na njegovom bankovnom racunu
    public void transactionMessage(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Information Message");
        alert.setContentText("Your bank account balance is " + getClientAccountBalance());
        alert.showAndWait();
    }

    // Poruka koja klijenta obavjestava da uskoro istice rok za uplatu rezervacije
    public void expirationDateMessage(){
        Alert alert;
        for (Aranzman aranzman : activeArrangList){
            for (Rezervacija rezervacija : activeResList){
                if (aranzman.getId().equals(rezervacija.getAranzman_id())){
                    Date date = aranzman.getDatum_polaska();
                    double totalPrice =  Double.parseDouble(rezervacija.getUkupna_cijena());
                    double firstPayment = totalPrice/2;
                    double payedPrice = Double.parseDouble(rezervacija.getPlacena_cijena());
                    if((checkDate(date) && firstPayment==payedPrice) || (checkIfThreeDaysLeft(date)) && totalPrice!=payedPrice){
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Please pay for the \""+ aranzman.getNaziv_putovanja()+"\" before the expiration date.");
                        alert.showAndWait();
                    }
                }
            }
        }
    }

    // Poruka koja klijenta obavjestava da je admin otkazao izlet/putovanje
    public void getCancelationMessage(){
        Alert alert;
        for (Aranzman aranzman : Database.aranzmani){
            if(aranzman.getCijena_aranzmana().equals("-1")){
                for (Rezervacija rezervacija : Database.rezervacije){
                    if (rezervacija.getAranzman_id().equals(aranzman.getId()) && rezervacija.getKlijent_id()==clientId){
                        BigDecimal payed = new BigDecimal(rezervacija.getPlacena_cijena());
                        BigDecimal newBalance = payed.add(getClientAccountBalance());
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Arrangement "+ aranzman.getNaziv_putovanja() + " was cancelled. Your bank account balance is " + newBalance);
                        alert.showAndWait();
                        Database.clientGotMessage(aranzman.getId());
                        break;
                    }
                }
            }
        }
    }
    /*--------------------------- Change password field ------------------------------*/
    public void closePasswordChange(ActionEvent event){
        passwordField.setVisible(false);
        clearPasswordFields();
        tfOldPassword.setStyle("");
        tfNewPassword.setStyle("");
    }

    public void updatePasswordChange(ActionEvent event){
        String password = getClientPassword();
        if(tfOldPassword.getText().isEmpty() && tfNewPassword.getText().isEmpty()){
            textFieldError(tfOldPassword);
            textFieldError(tfNewPassword);
        }
        else if (tfOldPassword.getText().isEmpty() || !password.equals(tfOldPassword.getText().trim())){
            textFieldError(tfOldPassword);
        } else if (tfNewPassword.getText().isEmpty()) {
            textFieldError(tfNewPassword);
        }else {
            Database.changeClientPassword(clientUsername.getText().trim(), tfNewPassword.getText().trim());

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

    private String getClientPassword(){
        for (Klijent klijent : Database.klijenti){
            if(clientUsername.getText().trim().equals(klijent.getKorisnicko_ime())){
                return klijent.getLozinka();
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
    /*--------------------------- Arrangements page ------------------------------*/
    public void displayCard(List<Aranzman> listData){
        arrGridPane.getChildren().clear();
        int row = 0;
        int column = 0;
        arrGridPane.getRowConstraints().clear();
        arrGridPane.getColumnConstraints().clear();

        for (int i = 0; i < listData.size(); i++) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cardArrangement.fxml"));
                AnchorPane pane = fxmlLoader.load();
                CardArrangement cardArr = fxmlLoader.getController();
                cardArr.setData(listData.get(i));
                if (column == 2){
                    column = 0;
                    row+=1;
                }

                arrGridPane.add(pane, column++, row);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //Iz liste izdvajamo samo jednodnevne izlete
    public void showOneDayArr(ActionEvent event){
        cardListData.clear();
        for (Aranzman aranzman : AdminPage.updatedArrangList){
            if (checkIfSameDate(aranzman.getDatum_polaska(), aranzman.getDatum_dolaska())){
                cardListData.add(aranzman);
            }
        }
        tfStarRating.setDisable(true);
        cbRoomType.setDisable(true);
        cbTransport.setDisable(true);

        displayCard(cardListData);
    }

    //Iz liste izdvajamo samo putovanja
    public void showTravelArr(ActionEvent event){
        cardListData.clear();
        for (Aranzman aranzman : AdminPage.updatedArrangList){
            if (!checkIfSameDate(aranzman.getDatum_polaska(), aranzman.getDatum_dolaska())){
                cardListData.add(aranzman);
            }
        }
        tfStarRating.setDisable(false);
        cbRoomType.setDisable(false);
        cbTransport.setDisable(false);

        displayCard(cardListData);
    }

    private boolean checkIfSameDate(Date date1, Date date2){
        long diffInMillies = date1.getTime() - date2.getTime();
        long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);
        if (diffInDays == 0){
            return true;
        }else {
            return false;
        }
    }

    // Filtriramo izlete/putovanja
    public void filterArrangements(ActionEvent event){
        filteredList.clear();
        filteredList.addAll(cardListData);
        try {
            if(!tfPrice.getText().isEmpty()){
               if (isNumeric(tfPrice.getText())){
                   double price = Double.parseDouble(tfPrice.getText());
                   filterByPrice(price);
               }
            }

            if (!tfDate1.getText().isEmpty() && !tfDate2.getText().isEmpty()){
                Date startDate = dateConversion(tfDate1.getText().trim());
                Date endDate = dateConversion(tfDate2.getText().trim());
                if (startDate.after(endDate)){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setTitle("Error Message");
                    alert.setContentText("Incorrect date input!");
                    alert.showAndWait();
                    throw new Exception("Incorrect date input!");
                }
                filterByDateRange(startDate,endDate);
            }

            if (!tfDestination.getText().isEmpty()){
                String destName = tfDestination.getText().trim();
                filterByDestination(destName);
            }

            if (!tfStarRating.getText().isEmpty()){
                if (isNumeric(tfStarRating.getText())){
                    double rating = Double.parseDouble(tfStarRating.getText().trim());
                    filterByRating(rating);
                }
            }

            if (cbRoomType.getValue()!=null){
                String roomType = cbRoomType.getValue().trim();
                filterByRoomType(roomType);
            }

            if (cbTransport.getValue()!=null){
                String transpType = cbTransport.getValue().trim();
                filterByTransportType(transpType);
            }

            clearAllFields();
            displayCard(filteredList);
        }catch (Exception e){
            String message = e.getMessage();
            System.out.println(message);
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
            alert.setContentText("Input is invalid, value is not a number!");
            alert.showAndWait();
            throw new NumberFormatException("Input is invalid, value is not a number!");
        }
    }

    private Date dateConversion(String dateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(dateString);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error Message");
            alert.setContentText("Incorrect date format!");
            alert.showAndWait();
            throw new IllegalArgumentException("Incorrect date format!");
        }
    }
    private void filterByPrice(double price){
        filteredList.removeIf(aranzman -> Double.parseDouble(aranzman.getCijena_aranzmana())>price);
    }
    public  void filterByDateRange(Date startDate, Date endDate) {
        filteredList.removeIf(arrangement -> !(arrangement.getDatum_polaska().after(startDate) && arrangement.getDatum_dolaska().before(endDate)));
    }
    private void filterByDestination(String destination){
        filteredList.removeIf(aranzman -> !aranzman.getDestinacija().equals(destination));
    }
    private void filterByRating(double rating){
        filteredList.removeIf(aranzman -> {
            for (Smjestaj smjestaj : Database.smjestaji) {
                if (aranzman.getSmjestaj_id() == null) {
                    return true;
                } else if (aranzman.getSmjestaj_id() == smjestaj.getId()) {
                    double stars = Double.parseDouble(smjestaj.getBroj_zvjezdica());
                    return stars != rating;
                }
            }
            return false;
        });
    }
    private void filterByRoomType(String type){
        filteredList.removeIf(aranzman -> {
            for (Smjestaj smjestaj : Database.smjestaji) {
                if (aranzman.getSmjestaj_id() == null) {
                    return true;
                } else if (aranzman.getSmjestaj_id() == smjestaj.getId()) {
                    return !smjestaj.getVrsta_sobe().equals(type);
                }
            }
            return false;
        });
    }

    private void filterByTransportType(String type){
        filteredList.removeIf(aranzman ->
                aranzman.getPrevoz() == null || !aranzman.getPrevoz().equals(type)
        );
    }

    private void clearAllFields(){
        tfPrice.clear();
        tfDate1.clear();
        tfDate2.clear();
        tfDestination.clear();
        tfStarRating.clear();
        cbTransport.setValue(null);
        cbRoomType.setValue(null);
    }

    //Sortiranje prema cijeni
    public void sortByPrice(ActionEvent event) {
        Comparator<Aranzman> priceComparator = Comparator.comparingDouble(arrangement -> {
            String priceString = arrangement.getCijena_aranzmana();
            try {
                return Double.parseDouble(priceString);
            } catch (NumberFormatException e) {
                return 0.0;
            }
        });

        Collections.sort(cardListData, priceComparator);
        displayCard(cardListData);
    }

    //Sortiranje prema vremenu do polaska
    public void sortByDate(ActionEvent event){
        Comparator<Aranzman> dateComparator = Comparator.comparing(Aranzman::getDatum_polaska);
        cardListData.sort(dateComparator);
        displayCard(cardListData);
    }

    /*--------------------------- Reservations page ------------------------------*/

    // Popunjavamo tabelu za aktivne rezervacije
    public void addActiveResListData(){
        resTableView.getItems().clear();
        ObservableList<Aranzman> listData = FXCollections.observableArrayList();
        for (Aranzman aranzman : activeArrangList){
            listData.add(aranzman);
        }

        nameCol.setCellValueFactory(new PropertyValueFactory<>("naziv_putovanja"));
        destCol.setCellValueFactory(new PropertyValueFactory<>("destinacija"));

        // Numeracija redova
        idCol.setCellFactory(col -> {
            TableCell<Aranzman, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(getIndex()+1+".");
                    }
                }
            };
            return cell;
        });

        // Popunjavamo kolonu priceCol sa ukupnim cijenama aranzmana
        priceCol.setCellValueFactory(cellData -> {
            Aranzman aranzman = cellData.getValue();

            Rezervacija reservation = getActiveReservationForAranzman(aranzman.getId());
            Double totalPrice = Double.parseDouble(reservation.getUkupna_cijena());

            return new SimpleStringProperty(String.valueOf(totalPrice));
        });

        // Popunjavamo kolonu dueCol sa kolicinom novca koju klijent duguje agenciji za rezervaciju
        dueCol.setCellValueFactory(cellData -> {
            Aranzman aranzman = cellData.getValue();

            Rezervacija reservation = getActiveReservationForAranzman(aranzman.getId());
            double totalPrice = Double.parseDouble(reservation.getUkupna_cijena());
            double payment = Double.parseDouble(reservation.getPlacena_cijena());
            double due = totalPrice-payment;

            return new SimpleStringProperty(String.valueOf(due));
        });

        resTableView.setItems(listData);

        // Posebno oznacimo redove u tabeli koji sadrze rezervacija za ciju uplatu je ostalo jos 3 dana ili manje
        resTableView.setRowFactory(tv -> new TableRow<Aranzman>() {
            @Override
            protected void updateItem(Aranzman item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    Rezervacija reservation = getActiveReservationForAranzman(item.getId());
                    if (reservation != null){
                        Date date = item.getDatum_polaska();
                        double totalPrice = Double.parseDouble(reservation.getUkupna_cijena());
                        double payment = Double.parseDouble(reservation.getPlacena_cijena());

                        if ((checkDate(date) && payment == totalPrice/2) || (checkIfThreeDaysLeft(date) && payment!=totalPrice)) {
                            setStyle("-fx-background-color: #DF7857;");
                        } else {
                            setStyle("");
                        }
                    }
                }
            }
        });
    }

    // Popunjavamo tabelu za protekle rezervacije
    public void addPastResListData(){
        resTableView.getItems().clear();
        ObservableList<Aranzman> listData = FXCollections.observableArrayList();
        for (Aranzman aranzman : pastArrangList){
            listData.add(aranzman);
        }

        nameCol.setCellValueFactory(new PropertyValueFactory<>("naziv_putovanja"));
        destCol.setCellValueFactory(new PropertyValueFactory<>("destinacija"));

        // Numeracija redova
        idCol.setCellFactory(col -> {
            TableCell<Aranzman, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(getIndex()+1+".");
                    }
                }
            };
            return cell;
        });

        // Popunjavamo kolonu priceCol sa ukupnim cijenama aranzmana
        priceCol.setCellValueFactory(cellData -> {
            Aranzman aranzman = cellData.getValue();

            Rezervacija reservation = getPastReservationForAranzman(aranzman.getId());
            Double totalPrice = Double.parseDouble(reservation.getUkupna_cijena());

            return new SimpleStringProperty(String.valueOf(totalPrice));
        });

        // Popunjavamo kolonu dueCol sa kolicinom novca koju klijent duguje agenciji za rezervaciju
        dueCol.setCellValueFactory(cellData -> {
            Aranzman aranzman = cellData.getValue();

            Rezervacija reservation = getPastReservationForAranzman(aranzman.getId());
            Double totalPrice = Double.parseDouble(reservation.getUkupna_cijena());
            Double payment = Double.parseDouble(reservation.getPlacena_cijena());
            Double due = totalPrice-payment;

            return new SimpleStringProperty(String.valueOf(due));
        });

        resTableView.setItems(listData);
    }

    // Popunjavamo tabelu za otkazane rezervacije
    public void addCanceledResListData(){
        resTableView.getItems().clear();
        ObservableList<Aranzman> listData = FXCollections.observableArrayList();
        for (Aranzman aranzman : canceledArrangList){
            listData.add(aranzman);
        }

        nameCol.setCellValueFactory(new PropertyValueFactory<>("naziv_putovanja"));
        destCol.setCellValueFactory(new PropertyValueFactory<>("destinacija"));

        // Numeracija redova
        idCol.setCellFactory(col -> {
            TableCell<Aranzman, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(getIndex()+1+".");
                    }
                }
            };
            return cell;
        });

        // Popunjavamo kolonu priceCol sa ukupnim cijenama aranzmana
        priceCol.setCellValueFactory(cellData -> {
            Aranzman aranzman = cellData.getValue();

            Rezervacija reservation = getCanceledReservationForAranzman(aranzman.getId());
            Double totalPrice = Double.parseDouble(reservation.getUkupna_cijena());

            return new SimpleStringProperty(String.valueOf(totalPrice));
        });

        // Popunjavamo kolonu dueCol sa kolicinom novca koju klijent duguje agenciji za rezervaciju
        dueCol.setCellValueFactory(cellData -> {
            Aranzman aranzman = cellData.getValue();

            return new SimpleStringProperty("/");
        });

        resTableView.setItems(listData);
    }

    public Rezervacija getActiveReservationForAranzman(String id){
        for (Rezervacija r : activeResList){
            if (id.equals(r.getAranzman_id())){
                return r;
            }
        }
        return null;
    }

    public Rezervacija getPastReservationForAranzman(String id){
        for (Rezervacija r : pastResList){
            if (id.equals(r.getAranzman_id())){
                return r;
            }
        }
        return null;
    }

    public Rezervacija getCanceledReservationForAranzman(String id){
        for (Rezervacija r : canceledResList){
            if (id.equals(r.getAranzman_id())){
                return r;
            }
        }
        return null;
    }

    // Uzimamo podatke iz tabele za rezervacije
    public void getTableItems(MouseEvent event){
        int index = resTableView.getSelectionModel().getSelectedIndex();
        if (index <= -1) return;

        String arrangementName = null;

        infoMessage.setVisible(false);

        for (Aranzman aranzman : Database.aranzmani){
            if(aranzman.getNaziv_putovanja().equals(nameCol.getCellData(index))){
                arrangementName = aranzman.getNaziv_putovanja();
                lblResName.setText(arrangementName);
                break;
            }
        }
    }

    // Provjeravamo da li je ostalo 3 ili manje dana za doplatu ostatka
    private boolean checkDate(Date departureDate){
        Date currentDate = new Date();
        long diffInMillies = departureDate.getTime() - currentDate.getTime();
        long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);
        return diffInDays <= 17 && diffInDays > 14;
    }

    // Uplata za ostatak izleta/putovanja
    public void payRemainingPrice(ActionEvent event){
        Alert alert;
        try {
            if(tfAmount.getText().isEmpty() || pfPassword.getText().isEmpty()){
                if (tfAmount.getText().isEmpty()){
                    textFieldError(tfAmount);
                }
                if (pfPassword.getText().isEmpty()){
                    textFieldError(pfPassword);
                }
                throw new IllegalArgumentException("Please fill in the empty fields!");
            } else if (!isNumeric(tfAmount.getText().trim())) {

            } else if (!pfPassword.getText().trim().equals(clientPass)) {
                pfPassword.clear();
                textFieldError(pfPassword);
                throw new Exception("Incorrect password!");
            }

            double payment = Double.parseDouble(tfAmount.getText().trim());
            double balance = Objects.requireNonNull(getClientAccountBalance()).doubleValue();
            if (payment > balance){
                throw new Exception("Not enough funds in Your bank account!");
            }else {
                int index = resTableView.getSelectionModel().getSelectedIndex();
                if (index <= -1){
                    throw new Exception("Arrangement is not selected!");
                }

                updateActiveReservation(nameCol.getCellData(index), payment);
                Database.updateBankAccountBalance(getClientJmbg(), payment+"", "sub");
                Database.updateBankAccountBalance("1102541293", payment+"", "add");

                transactionMessage();

                // Azuriranje lista i tabela
                activeArrangList = getActiveArrangements();
                activeResList = getActiveReservations();
                addActiveResListData();
                moneySpent.setText(getTotalMoneySpent()+"KM");

                tfAmount.setStyle("");
                pfPassword.setStyle("");
                tfAmount.clear();
                pfPassword.clear();
            }
        }catch (Exception e){
            String message = e.getMessage();
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error Message");
            alert.setContentText(message);
            alert.showAndWait();
            System.out.println(message);
        }
    }

    private void updateActiveReservation(String name, double payment){
        for (Rezervacija rezervacija : activeResList){
            for (Aranzman aranzman : activeArrangList){
                if (aranzman.getNaziv_putovanja().equals(name) && rezervacija.getAranzman_id().equals(aranzman.getId())){
                    double payedSoFar = Double.parseDouble(rezervacija.getPlacena_cijena());
                    double updatedPayment = payment+payedSoFar;
                    Database.updateReservation(clientId, aranzman.getId(), updatedPayment+"");
                }
            }
        }
    }

    public void cancelReservation(ActionEvent event){
        int index = resTableView.getSelectionModel().getSelectedIndex();
        if (index <= -1) return;

        for (Aranzman aranzman : activeArrangList){
            for (Rezervacija rezervacija : activeResList){
                if (aranzman.getNaziv_putovanja().equals(nameCol.getCellData(index)) && rezervacija.getAranzman_id().equals(aranzman.getId())){
                    if (!checkExpirationDate(aranzman.getDatum_polaska())){
                        String value = rezervacija.getPlacena_cijena();
                        Database.updateBankAccountBalance(getClientJmbg(), value, "add");
                        Database.updateBankAccountBalance("1102541293", value, "sub");
                        Database.clientCancelReservation(clientId, aranzman.getId());

                        transactionMessage();

                        // Azuriranje lista i tabela
                        canceledArrangList = getCanceledArrangements();
                        canceledResList = getCanceledReservations();
                        addCanceledResListData();
                        activeArrangList = getActiveArrangements();
                        activeResList = getActiveReservations();
                        addActiveResListData();
                        moneySpent.setText(getTotalMoneySpent()+"KM");
                    }else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText(null);
                        alert.setTitle("Error Message");
                        alert.setContentText("The cancellation time has expired! You cannot cancel the selected reservation.");
                        alert.showAndWait();
                    }
                }
            }
        }

    }

    /*--------------------------- Basic Functionalities ------------------------------*/

    public void switchForm(ActionEvent event){
        if(event.getSource() == btnHome) {
            homeView.visibleProperty().set(true);
            arrangementsView.visibleProperty().set(false);
            reservationsView.visibleProperty().set(false);
        } else if(event.getSource() == btnArrangements){
            homeView.visibleProperty().set(false);
            arrangementsView.visibleProperty().set(true);
            reservationsView.visibleProperty().set(false);
        } else if (event.getSource() == btnReservations) {
            rbActive.setSelected(true);
            moneySpent.setText(getTotalMoneySpent()+"KM");
            addActiveResListData();
            tfAmount.setStyle("");
            pfPassword.setStyle("");
            tfAmount.clear();
            pfPassword.clear();

            homeView.visibleProperty().set(false);
            arrangementsView.visibleProperty().set(false);
            reservationsView.visibleProperty().set(true);
        }else if (event.getSource() == btnBookTour){
            homeView.visibleProperty().set(false);
            arrangementsView.visibleProperty().set(true);
            reservationsView.visibleProperty().set(false);
        }

        if (event.getSource() == btnPassword) {
            passwordField.visibleProperty().set(true);
        }
    }

    public void changeTableData(ActionEvent event){
        if(rbActive.isSelected()){
            addActiveResListData();
        } else if (rbPast.isSelected()) {
            addPastResListData();
        } else if (rbCanceled.isSelected()) {
            addCanceledResListData();
        }
    }

    public void clientLogOut(ActionEvent event) throws IOException {
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

    public void btnMouseEntered(MouseEvent event){
        Node source = (Node) event.getSource();
        if(source == btnHome){
            mouseEnteredMenuBtn(btnHome);
        } else if (source == btnReservations) {
            mouseEnteredMenuBtn(btnReservations);
        } else if (source == btnArrangements) {
            mouseEnteredMenuBtn(btnArrangements);
        } else if (source == btnPassword) {
            mouseEnteredMenuBtn(btnPassword);
        } else if (source == btLogOut) {
            mouseEnteredMenuBtn(btLogOut);
        } else if (source == btnMinimize) {
            mouseEnteredMenuBtn(btnMinimize);
        } else if (source == btnExit) {
            mouseEnteredMenuBtn(btnExit);
        } else if (source == btnLowPriced) {
            mouseEnteredFuncBtn(btnLowPriced);
        } else if (source == btnDate) {
            mouseEnteredFuncBtn(btnDate);
        } else if (source == btnFilter) {
            mouseEnteredFuncBtn(btnFilter);
        }else if (source == btnPassFieldClose) {
            mouseEnteredWhiteBtn(btnPassFieldClose);
        } else if (source == btnCancelRes) {
            mouseEnteredWhiteBtn(btnCancelRes);
        } else if (source == btnPassFieldUpdate) {
            mouseEnteredGreenBtn(btnPassFieldUpdate);
        } else if (source == btnPay) {
            mouseEnteredGreenBtn(btnPay);
        } else if (source == btnBookTour) {
            mouseEnteredFuncBtn(btnBookTour);
        }else if(source == btnBack){
            mouseEnteredMenuBtn(btnBack);
        } else if (source == btnForward) {
            mouseEnteredMenuBtn(btnForward);
        }
    }

    public void btnMouseExited(MouseEvent event){
        Node source = (Node) event.getSource();
        if(source == btnHome){
            mouseExitedMenuBtn(btnHome);
        } else if (source == btnReservations) {
            mouseExitedMenuBtn(btnReservations);
        } else if (source == btnArrangements) {
            mouseExitedMenuBtn(btnArrangements);
        } else if (source == btnPassword) {
            mouseExitedMenuBtn(btnPassword);
        } else if (source == btLogOut) {
            mouseExitedMenuBtn(btLogOut);
        } else if (source == btnMinimize) {
            mouseExitedMenuBtn(btnMinimize);
        } else if (source == btnExit) {
            mouseExitedMenuBtn(btnExit);
        } else if (source == btnLowPriced) {
            mouseExitedFuncBtn(btnLowPriced);
        } else if (source == btnDate) {
            mouseExitedFuncBtn(btnDate);
        } else if (source == btnFilter) {
            mouseExitedFuncBtn(btnFilter);
        } else if (source == btnPassFieldClose) {
            mouseExitedWhiteBtn(btnPassFieldClose);
        } else if (source == btnCancelRes) {
            mouseExitedWhiteBtn(btnCancelRes);
        } else if (source == btnPassFieldUpdate) {
            mouseExitedGreenBtn(btnPassFieldUpdate);
        } else if (source == btnPay) {
            mouseExitedGreenBtn(btnPay);
        } else if (source == btnBookTour) {
            mouseExitedFuncBtn(btnBookTour);
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

    private void mouseEnteredFuncBtn(Button button){
        button.setStyle("-fx-background-color: #EBC8A3;");
    }

    private void mouseExitedFuncBtn(Button button){
        button.setStyle("-fx-background-color: #DAAD86");
    }

    private void mouseEnteredWhiteBtn(Button button){
        button.setTextFill(Color.valueOf("#fff"));
        button.setStyle("-fx-background-color: #9ED2BE;");
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
    /*--------------------------- Back/Forward navigation ------------------------------*/
    public void navigation(ActionEvent event){
        Node source = (Node) event.getSource();
        if (source == btnForward){
            if (homeView.isVisible()){
                homeView.setVisible(false);
                arrangementsView.setVisible(true);
            } else if (arrangementsView.isVisible()) {
                arrangementsView.setVisible(false);
                reservationsView.setVisible(true);
            }
        } else if (source == btnBack) {
            if (reservationsView.isVisible()){
                reservationsView.setVisible(false);
                arrangementsView.setVisible(true);
            } else if (arrangementsView.isVisible()) {
                arrangementsView.setVisible(false);
                homeView.setVisible(true);
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
