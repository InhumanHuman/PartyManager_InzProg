package com.example.partymanager_inzprog;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class Employee {
    // scene switching
    private Scene scene;
    private Stage stage;
    @FXML
    private VBox ScenePanelUser;

    @FXML
    private Label errorMessage;
    @FXML
    private TextArea userMessage;
    @FXML
    private TextField messageTopic;

    public void switchToLoginScreen(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void goBackToEmployeeScreen(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("employee.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void LogOut_WithQuestion(javafx.event.ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Za chwilę się wylogujesz!");
        alert.setContentText("Czy na pewno chcesz kontynuować?");

        if(alert.showAndWait().get() == ButtonType.OK) {
            stage = (Stage) ScenePanelUser.getScene().getWindow();
            this.switchToLoginScreen(actionEvent);
        }
    }
    public void switchToAddPartyScreen(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("employeeAddParty.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToRemovePartyScreen(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("employeeRemoveParty.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToChangePartyCredentialsScreen(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("employeeChangePartyCredentials.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToMessagesScreen(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("employeeMessages.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToMessageToBossScreen(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("employeeMessageToBoss.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public boolean askBeforeConfirmation(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Wysyłanie wiadomości");
        alert.setHeaderText("Za chwilę wyślesz wiadomość do szefa.");
        alert.setContentText("Czy na pewno chcesz kontynuować?");

        if(alert.showAndWait().get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    public void sendMailToBoss(javafx.event.ActionEvent actionEvent) {
        if(!askBeforeConfirmation()) {
            return;
        }
        if(userMessage.getText().equals("")) {
            errorMessage.setText("Nie wpisałeś treści wiadomości.");
            return;
        }
        if(messageTopic.getText().equals("")) {
            errorMessage.setText("Nie podano tematu wiadomości.");
            return;
        }
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psSelect = null;
        ResultSet resultSet = null;
        try {
            int mssgID;

            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/transport_company_database", "root","root");
            psInsert = connection.prepareStatement("INSERT INTO wiadomosci (temat,tresc_wiadomosci,status) VALUES(?,?,?)");
            psInsert.setString(1, messageTopic.getText());
            psInsert.setString(2, userMessage.getText());
            psInsert.setString(3, "nowa");
            psInsert.executeUpdate();

            psSelect = connection.prepareStatement("SELECT id_wiadomosci FROM wiadomosci WHERE temat = ? ORDER BY id_wiadomosci DESC");
            psSelect.setString(1,messageTopic.getText());
            resultSet = psSelect.executeQuery();
            resultSet.next();
            mssgID = resultSet.getInt(1);

            psInsert = connection.prepareStatement("INSERT INTO wiadomosci_uzytkownicy (id_wiadomosci, id_uzytkownika) VALUES(?,?)");
            psInsert.setString(1,Integer.toString(mssgID));
            psInsert.setString(2,Integer.toString(CurrentUser.currentUserID));
            psInsert.executeUpdate();

        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            if(connection!=null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if(psSelect!=null) {
                try {
                    psSelect.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if(psInsert!=null) {
                try {
                    psInsert.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if(resultSet!=null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        try {
            this.goBackToEmployeeScreen(actionEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
