package com.example.agencija;

import agencija.Admin;
import agencija.Klijent;
import database.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.*;

public class LogIn{
    public LogIn(){}

    @FXML
    private Label lblErrorLogIn;
    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private TextField unmaskPassword;
    @FXML
    private Button btLogIn;
    @FXML
    private Button btSignUp;
    @FXML
    private RadioButton radioButton;
    public static int clientId;
    public static String clientPass;
    private static int loggingAlertMessage = 0;

    public void userLogIn(ActionEvent event) throws IOException {
        checkLogIn();
    }

    public void userSignUp(ActionEvent event) throws IOException{
        Main m = new Main();
        m.changeScene("signUp.fxml");
    }

    private void checkLogIn() throws IOException{
        Main m = new Main();
        Database.main(null);

        String username = tfUsername.getText().trim();
        String password = tfPassword.getText().trim();

        // Lista koja sadrzi korisnicka imena i lozinke svih admina
        List<String> adminData = new ArrayList<>();
        for (Admin admin : Database.admini){
            adminData.add(admin.getKorisnicko_ime());
            adminData.add(admin.getLozinka());
        }

        // Lista koja sadrzi korisnicka imena i lozinke svih klijenata
        List<String> clientData = new ArrayList<>();
        for (Klijent klijent : Database.klijenti){
            clientData.add(klijent.getKorisnicko_ime());
            clientData.add(klijent.getLozinka());
        }

        // Lista koja sadrzi korisnicka imena svih admina koji su dodani od strane drugog admina
        List<String> newAdminList = new ArrayList<>();
        for (Admin admin : Database.admini){
            if (admin.getLozinka().equals("12345678")){
                newAdminList.add(admin.getKorisnicko_ime());
            }
        }

        // Mapa koja za kljuc i vrijednost sadrzi korisnicko ime i lozinku admina
        Map<String, String> adminMap = new HashMap<>();
        for (Admin admin : Database.admini){
            adminMap.put(admin.getKorisnicko_ime(), admin.getLozinka());
        }

        // Mapa koja za kljuc i vrijednost sadrzi korisnicko ime i lozinku klijenta
        Map<String, String> clientMap = new HashMap<>();
        for (Klijent klijent : Database.klijenti){
            clientMap.put(klijent.getKorisnicko_ime(), klijent.getLozinka());
        }

        // Provjeramo da li su sva polja popunjena
        if (tfUsername.getText().isEmpty() || tfPassword.getText().isEmpty()) {
            lblErrorLogIn.setText("Please fill out all required fields!");
            if (tfUsername.getText().isEmpty())
                textFieldError(tfUsername);

            if (tfPassword.getText().isEmpty())
                textFieldError(tfPassword);

            if (unmaskPassword.getText().isEmpty())
                textFieldError(unmaskPassword);
        }
        // Provjeravamo da li se novi admin prvi put prijavljuje u sistem
        else if(newAdminList.contains(tfUsername.getText().trim())){
            if(!password.equals("12345678") && loggingAlertMessage!=0){
                // promjenimo sifru
                Database.changeAdminPassword(username, password);
                newAdminList.remove(username);
                for (Admin a : Database.admini){
                    if(a.getKorisnicko_ime().equals(username)){
                        a.setLozinka(password);
                    }
                }
                m.changeScene("adminPage.fxml");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("You have successfully changed Your password.");
                alert.showAndWait();
            }else {
                loggingAlertMessage += 1;
                textFieldError(tfPassword);
                lblErrorLogIn.setText("First time logging in, please change Your password!");
            }
        } else if(adminMap.get(username)!=null && adminMap.get(username).equals(password)){
            // Uspjesna prijava za admina
            m.changeScene("adminPage.fxml");
            AdminPage.staticAdminUsername.setText(username);
            for (Admin admin : Database.admini){
                if(admin.getLozinka().equals(password) && admin.getKorisnicko_ime().equals(username)){
                    AdminPage.staticAdminFullName.setText(admin.getIme() + " " + admin.getPrezime());
                }
            }
        } else if (clientMap.get(username)!=null && clientMap.get(username).equals(password)){
            // Uspjesna prijava za klijenta
            for (Klijent klijent : Database.klijenti){
                if (klijent.getKorisnicko_ime().equals(username)){
                    clientId = klijent.getId();
                    clientPass = klijent.getLozinka();
                }
            }
            m.changeScene("clientPage.fxml");
            ClientPage.staticClientUsername.setText(username);
            for (Klijent klijent : Database.klijenti){
                if(klijent.getLozinka().equals(password) && klijent.getKorisnicko_ime().equals(username)){
                    ClientPage.staticClientFullName.setText(klijent.getIme() + " " + klijent.getPrezime());
                }
            }
        } else {
            // Pogresno uneseno korisnicko ime ili lozinka
            if(!adminData.contains(username) && !clientData.contains(username)){
                lblErrorLogIn.setText("Username is incorrect!");
                tfUsername.setText("");
                textFieldError(tfUsername);
            }else {
                lblErrorLogIn.setText("Password is incorrect!");
                tfPassword.setText("");
                unmaskPassword.setText("");
                textFieldError(tfPassword);
            }
        }
    }

    private void textFieldError(TextField tf){
        tf.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
    }

    public void txtFieldColorChange(KeyEvent event) throws IOException{
        List<TextField> textFields = new ArrayList<>(Arrays.asList(
                tfUsername,
                tfPassword
        ));

        for (TextField textField : textFields){
            if (!textField.getText().isEmpty()){
                textField.setStyle("");
            }
        }
    }

    public void maskPassword(ActionEvent event) throws IOException{
        if(radioButton.isSelected()){
            unmaskPassword.setVisible(true);
            tfPassword.setVisible(false);
        }else {
            unmaskPassword.setVisible(false);
            tfPassword.setVisible(true);
        }
    }

    public void setUnmaskedPass(KeyEvent event) throws IOException{
        unmaskPassword.setText(tfPassword.getText().trim());
    }

    public void setMaskedPass(KeyEvent event) throws IOException{
        tfPassword.setText(unmaskPassword.getText().trim());
    }

    public void setOnMouseEntered(MouseEvent event){
        btLogIn.setStyle("-fx-background-color: #C4E7D9");
    }
    public void setOnMouseExited(MouseEvent event){btLogIn.setStyle("-fx-background-color: #80BCBD;");}

    public void txtColorOnMouseEntered(MouseEvent event){
        btSignUp.setTextFill(Color.valueOf("#C4E7D9"));
    }
    public void txtColorOnMouseExited(MouseEvent event){
        btSignUp.setTextFill(Color.valueOf("#80BCBD"));
    }
}
