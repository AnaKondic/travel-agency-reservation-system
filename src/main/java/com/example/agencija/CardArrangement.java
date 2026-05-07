package com.example.agencija;

import agencija.*;
import database.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

public class CardArrangement implements Initializable {
    @FXML
    Label arrivalDate;
    @FXML
    Label departureDate;
    @FXML
    Label destination;
    @FXML
    Label lodging;
    @FXML
    Label name;
    @FXML
    Label price;
    @FXML
    Label pricePerNight;
    @FXML
    Label rating;
    @FXML
    Label roomType;
    @FXML
    Label transport;
    @FXML
    Label lblRating;
    @FXML
    Label lblPerNight;
    @FXML
    Button btnBook;
    @FXML
    Button btnConfirm;
    @FXML
    PasswordField passConfirm;
    Aranzman aranzman;
    public void setData(Aranzman aranzman){
        this.aranzman = aranzman;

        name.setText(aranzman.getNaziv_putovanja());
        price.setText(aranzman.getCijena_aranzmana());
        destination.setText(aranzman.getDestinacija());
        departureDate.setText(aranzman.getDatum_polaska()+"");
        arrivalDate.setText(aranzman.getDatum_dolaska()+"");
        transport.setText(aranzman.getPrevoz());
        if (aranzman.getSmjestaj_id()!=null){
            for (Smjestaj smjestaj : Database.smjestaji){
                if(smjestaj.getId()==aranzman.getSmjestaj_id()){
                    lodging.setText(smjestaj.getNaziv());
                    pricePerNight.setText(smjestaj.getCjena_po_nocenju());
                    roomType.setText(smjestaj.getVrsta_sobe());
                    rating.setText(smjestaj.getBroj_zvjezdica());
                }
            }
        }else {
            lblRating.setVisible(false);
            lblPerNight.setVisible(false);
            lodging.setVisible(false);
            pricePerNight.setVisible(false);
            roomType.setVisible(false);
            rating.setVisible(false);
        }
    }

    public void bookArrangement(ActionEvent event){
        Alert alert;
        if (ClientPage.canceledArrangList.contains(aranzman)){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error Message");
            alert.setContentText("Booking error! This arrangement is in your canceled arrangements.");
            alert.showAndWait();
        } else if (ClientPage.activeArrangList.contains(aranzman)) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error Message");
            alert.setContentText("Booking error! You have already booked this arrangement.");
            alert.showAndWait();
        }else {
            btnBook.setVisible(false);
            btnConfirm.setVisible(true);
            passConfirm.setVisible(true);
        }
    }

    public void confirmPass(ActionEvent event){
        Alert alert;
        //Provjeravamo ispravnost lozinke
        if (passConfirm.getText().isEmpty()){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error Message");
            alert.setContentText("Please enter Your password!");
            passwordFieldError(passConfirm);
            alert.showAndWait();
            return;
        } else if (!passConfirm.getText().trim().equals(ClientPage.clientPass)) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error Message");
            alert.setContentText("Incorrect password!");
            alert.showAndWait();
            passwordFieldError(passConfirm);
            passConfirm.clear();
            return;
        }

        String clientJmbg = "";
        int clientId = ClientPage.clientId;
        for (Klijent klijent : Database.klijenti){
            if (klijent.getId() == clientId){
                clientJmbg = klijent.getJmbg();
            }
        }
        double balance = Objects.requireNonNull(getClientAccountBalance(clientJmbg)).doubleValue();
        double totalPrice = totalReservationPrice();
        double firstPayment = totalPrice/2;

        // Provjeravamo da li klijent ima dovoljno sredstava na racunu
        if (firstPayment <= balance){
            Database.updateBankAccountBalance(clientJmbg, firstPayment+"", "sub");
            Database.updateBankAccountBalance("1102541293", firstPayment+"", "add");
            Database.addReservationToDataBase(clientId, aranzman.getId(), totalPrice+"", firstPayment+"");
            ClientPage.availableArrangList = ClientPage.getAvailableArrangements();
            ClientPage.activeArrangList = ClientPage.getActiveArrangements();
            ClientPage.activeResList = ClientPage.getActiveReservations();
            AdminPage.updatedResList = AdminPage.updateReservationList();

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Information Message");
            alert.setContentText("You have successfully booked arrangement \"" + aranzman.getNaziv_putovanja() + "\"! Your bank Account balance is " + getClientAccountBalance(clientJmbg) + ".");
            alert.showAndWait();

            ClientPage.paneList.get(1).setVisible(false);
            ClientPage.paneList.get(0).setVisible(true);
            btnBook.setVisible(true);
            btnConfirm.setVisible(false);
            passConfirm.setVisible(false);
            passConfirm.setStyle("");
        }else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error Message");
            alert.setContentText("Not enough funds in Your bank account!");
            alert.showAndWait();

            btnBook.setVisible(true);
            btnConfirm.setVisible(false);
            passConfirm.setVisible(false);
            passConfirm.setStyle("");
        }

    }

    private double totalReservationPrice(){
        double arrPrice = Double.parseDouble(aranzman.getCijena_aranzmana());
        if (aranzman.getSmjestaj_id() != null){
            int nights = (int) getDayDifference(aranzman.getDatum_polaska(), aranzman.getDatum_dolaska());
            double pricePerNight = 0;
            for (Smjestaj smjestaj : Database.smjestaji){
                if (aranzman.getSmjestaj_id().equals(smjestaj.getId())){
                    pricePerNight = Double.parseDouble(smjestaj.getCjena_po_nocenju());
                }
            }
            arrPrice += nights*pricePerNight;
        }
        return  arrPrice;
    }

    private static long getDayDifference(Date startDate, Date endDate){
        long diffInMillies = endDate.getTime() - startDate.getTime();
        return diffInMillies / (1000 * 60 * 60 * 24);
    }

    private BigDecimal getClientAccountBalance(String jmbg){
        for (BankovniRacun bankovniRacun : Database.racuni){
            if (bankovniRacun.getJmbg().equals(jmbg)){
                return bankovniRacun.getStanje();
            }
        }
        return null;
    }

    private void passwordFieldError(PasswordField pf){
        pf.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
    }

    public void btnMouseEntered(MouseEvent event){
        Node source = (Node) event.getSource();
        if(source == btnBook){
            mouseEnteredStyle(btnBook);
        } else if (source == btnConfirm) {
            mouseEnteredStyle(btnConfirm);
        }
    }

    public void btnMouseExited(MouseEvent event){
        Node source = (Node) event.getSource();
        if(source == btnBook){
            mouseExitedStyle(btnBook);
        } else if (source == btnConfirm) {
            mouseExitedStyle(btnConfirm);
        }
    }

    private void mouseEnteredStyle(Button button){
        button.setStyle("-fx-background-color: #EBC8A3;");
    }

    private void mouseExitedStyle(Button button){
        button.setStyle("-fx-background-color: #DAAD86");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
