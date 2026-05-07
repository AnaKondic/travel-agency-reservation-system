package com.example.agencija;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static Stage stage;
    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main.fxml"));
        primaryStage.setScene(new Scene(fxmlLoader.load()));
        primaryStage.setTitle("Travel and Tourism Agency");
        primaryStage.show();
    }

    public void changeScene(String fxml) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource(fxml));
        stage.getScene().setRoot(parent);
    }

    public static void main(String[] args) {
        launch();
    }
}