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
    private TextField partyName;
    @FXML
    private TextField partyAddress;
    @FXML
    private DatePicker partyOpeningDate;
    @FXML
    private DatePicker partyClosingDate;
    @FXML
    private TextField priceOfTheParty;
    @FXML
    private  TextField availableEntries;

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
    public void switchToHelpDesk(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("employeeHelpDesk.fxml"));
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
    public void switchToMessageToBossScreen(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("employeeMessageToBoss.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public boolean askBeforeAddParty(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Dodanie nowej imprezy");
        alert.setHeaderText("Za chwilę dodasz nową imprezę do systemu.");
        alert.setContentText("Czy na pewno chcesz kontynuować?");

        if(alert.showAndWait().get() == ButtonType.OK) {
            return true;
        }
        return false;
    }
    public void registerNewParty(javafx.event.ActionEvent actionEvent){
        try {
            if(!askBeforeAddParty()) {
                return;
            }
            // testing if input fields are not empty
            if (partyName.equals("")) {
                throw new Exception("Pole 'nazwa imprezy' nie może być puste");
            }
            if (partyAddress.equals("")) {
                throw new Exception("Pole 'adres' nie może być puste");
            }
            if (partyOpeningDate.getValue() == null) {
                throw new Exception("Pole 'data rozpoczęcia' nie może być puste");
            }
            if (partyClosingDate.getValue() == null) {
                throw new Exception("Pole 'data zakończenia' nie może być puste");
            }
            if (priceOfTheParty.equals("")) {
                throw new Exception("Pole 'cena' nie może być puste");
            }
            if (availableEntries.equals("")) {
                throw new Exception("Pole 'liczba dostępnych miejsc' nie może być puste");
            }
            if ((partyClosingDate.getValue()).isBefore((partyOpeningDate).getValue())) {
                throw new Exception("Data zakończenia nie może być przed datą rozpoczęcia");
            }

            // connecting to external database
            Connection connection = null;
            PreparedStatement psInsert = null;
            PreparedStatement psCheckPartyExists = null;
            ResultSet resultSet = null;

            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/party_management_database", "root", "root");
                psCheckPartyExists = connection.prepareStatement("SELECT * FROM parties WHERE partyName = ?");
                psCheckPartyExists.setString(1, partyName.getText());
                resultSet = psCheckPartyExists.executeQuery();

                if (resultSet.isBeforeFirst()) {
                    throw new Exception("Impreza o podanych danych juz istnieje");
                } else {
                    psInsert = connection.prepareStatement("INSERT INTO parties (partyName,partyAddress,partyOpeningDate,partyClosingDate,priceOfTheParty,availableEntries) VALUES(?,?,?,?,?,?)");
                    psInsert.setString(1, partyName.getText());
                    psInsert.setString(2, partyAddress.getText());
                    psInsert.setString(3, String.valueOf(partyOpeningDate.getValue()));
                    psInsert.setString(4, String.valueOf(partyClosingDate.getValue()));
                    psInsert.setString(5, priceOfTheParty.getText());
                    psInsert.setString(6, availableEntries.getText());
                    psInsert.executeUpdate();
                }
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            } finally {
                if (resultSet != null) {
                    try {
                        resultSet.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (psCheckPartyExists != null) {
                    try {
                        psCheckPartyExists.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (psInsert != null) {
                    try {
                        psInsert.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            // switching back to employee screen
            this.goBackToEmployeeScreen(actionEvent);
        } catch(Exception e) {
            errorMessage.setText(e.getMessage());
            e.printStackTrace();
        }
    }
}
