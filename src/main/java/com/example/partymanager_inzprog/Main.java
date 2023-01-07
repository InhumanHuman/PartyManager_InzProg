package com.example.partymanager_inzprog;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Scene scene = new Scene(root, 400, 525);
            stage.setScene(scene);
            Image icon = new Image("file:party_popper.png");
            stage.getIcons().add(icon);
            stage.setTitle("Party manager");
            stage.show();

            stage.setOnCloseRequest(windowEvent -> {
                windowEvent.consume();
                Close_Aplication(stage);
            });
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void Close_Aplication(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("Za chwilę wyjdziesz z aplikacji!");
        alert.setContentText("Czy chcesz kontynuwać?");

        if(alert.showAndWait().get() == ButtonType.OK) {
            stage.close();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
