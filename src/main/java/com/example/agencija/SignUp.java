package com.example.agencija;

import agencija.BankovniRacun;
import agencija.Klijent;
import database.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.*;

public class SignUp {
    public SignUp(){}

    @FXML
    TextField tfName;
    @FXML
    TextField tfLastName;
    @FXML
    TextField tfPhoneNumber;
    @FXML
    TextField tfJmbg;
    @FXML
    TextField tfBankAccount;
    @FXML
    TextField tfClientUsername;
    @FXML
    TextField tfClientPassword;
    @FXML
    TextField tfRepeatPassword;
    @FXML
    Label lblErrorSignUp;
    @FXML
    Button btClientSignUp;
    @FXML
    Button btLogInGoBack;

    public void userGoBack() throws IOException{
        Main m = new Main();
        m.changeScene("main.fxml");
    }

    public void clientSignUp(ActionEvent event) throws IOException{
        checkSignUp();
    }

    public void checkSignUp() throws IOException{
        Main m = new Main();
        Database.main(null);

        List<TextField> textFields = new ArrayList<>(Arrays.asList(
                tfName,
                tfLastName,
                tfPhoneNumber,
                tfJmbg,
                tfBankAccount,
                tfClientUsername,
                tfClientPassword,
                tfRepeatPassword
        ));

        // Kreiramo listu koja sadrzi korisnicka imena klijenata
        List<String> clientUsernames = new ArrayList<>();
        for (Klijent klijent : Database.klijenti){
            clientUsernames.add(klijent.getKorisnicko_ime());
        }

        // Kreiramo listu koja sadrzi brojeve bankovnih racuna klijenata
        List<String> clientBankAccNums = new ArrayList<>();
        for (Klijent klijent : Database.klijenti){
            clientBankAccNums.add(klijent.getBroj_racuna());
        }

        // Kreiramo listu koja sadrzi jmbg klijenata
        List<String> clientJmbg = new ArrayList<>();
        for (BankovniRacun racun : Database.racuni){
            clientJmbg.add(racun.getJmbg());
        }

        // Kreiramo mapu sa jmbg klijenta kao kljuc i broj bankovnog racuna kao vrijednost
        Map<String, String> clientBankData = new HashMap<>();
        for (BankovniRacun bankovniRacun : Database.racuni){
            clientBankData.put(bankovniRacun.getJmbg(), bankovniRacun.getBroj_racuna());
        }

        for (TextField textField : textFields){
            if (textField.getText().isEmpty()){
                textField.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
            }
        }

        // Provjeravamo da li su sva polja popunjena
        for (TextField textField : textFields){
            if (textField.getText().isEmpty()){
                lblErrorSignUp.setText("Please fill out all required fields!");
                return;
            }
        }

        // Provjeravamo ispravnost unesenih informacija
        if (!clientJmbg.contains(tfJmbg.getText().trim())) {
            lblErrorSignUp.setText("No client in the bank with the entered jmbg!");
            tfJmbg.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
            tfJmbg.setText("");
        } else if (clientBankAccNums.contains(tfBankAccount.getText().trim())){
            lblErrorSignUp.setText("Client with the same bank account number already exist!");
            tfBankAccount.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
            tfBankAccount.setText("");
        } else if (!clientBankData.get(tfJmbg.getText().trim()).equals(tfBankAccount.getText().trim())) {
            lblErrorSignUp.setText("Bank account number does not match for entered jmbg!");
            tfBankAccount.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
            tfBankAccount.setText("");
        } else if(clientUsernames.contains(tfClientUsername.getText().trim())){
            lblErrorSignUp.setText("Username " + tfClientUsername.getText() + " already exists!");
            tfClientUsername.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
            tfClientUsername.setText("");
        } else if (!tfClientPassword.getText().trim().equals(tfRepeatPassword.getText().trim())) {
            lblErrorSignUp.setText("Repeated password is incorrect!");
            tfRepeatPassword.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
            tfRepeatPassword.setText("");
        } else {
            // Svi uslovi su ispunjeni i unosimo podatke novog klijenta u bazu
            String name = tfName.getText().trim();
            String lastName = tfLastName.getText().trim();
            String phoneNumber = tfPhoneNumber.getText().trim();
            String jmbg = tfJmbg.getText().trim();
            String bankAccNumber = tfBankAccount.getText().trim();
            String username = tfClientUsername.getText().trim();
            String password = tfClientPassword.getText().trim();
            int id = Database.klijenti.size() + 1;

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText("Account has been successfully created!");
            alert.showAndWait();

            Database.addClientToDatabase(id, name, lastName, phoneNumber, jmbg, bankAccNumber, username, password);
            m.changeScene("main.fxml");
        }
    }

    public void textFieldColorChange(KeyEvent event) throws IOException{
        List<TextField> textFields = new ArrayList<>(Arrays.asList(
                tfName,
                tfLastName,
                tfPhoneNumber,
                tfJmbg,
                tfBankAccount,
                tfClientUsername,
                tfClientPassword,
                tfRepeatPassword
        ));

        for (TextField textField : textFields){
            if (!textField.getText().isEmpty()){
                textField.setStyle("");
            }
        }
    }
    public void setOnMouseEntered(MouseEvent event){
        btClientSignUp.setStyle("-fx-background-color: #C4E7D9;");
    }
    public void setOnMouseExited(MouseEvent event){
        btClientSignUp.setStyle("-fx-background-color: #80BCBD;");
    }
    public void txtColorOnMouseEntered(MouseEvent event){
        btLogInGoBack.setTextFill(Color.valueOf("#C4E7D9"));
    }
    public void txtColorOnMouseExited(MouseEvent event){
        btLogInGoBack.setTextFill(Color.valueOf("#80BCBD"));
    }
}
