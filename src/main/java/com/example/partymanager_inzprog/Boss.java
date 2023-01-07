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

public class Boss {
    // scene switching
    private Scene scene;
    private Stage stage;

    @FXML
    private TextField firstname;
    @FXML
    private TextField lastname;
    @FXML
    private TextField e_mail;
    @FXML
    private TextField id_number;
    @FXML
    private TextField login;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField confirmPassword;
    @FXML
    private TextField salaryTextField;

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
    private VBox ScenePanelBoss;

    @FXML
    private Label errorMessage;

    public void switchToLoginScreen(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void goBackToBossScreen(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("boss.fxml"));
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
            stage = (Stage) ScenePanelBoss.getScene().getWindow();
            this.switchToLoginScreen(actionEvent);
        }
    }
    public void switchToAddEmployee(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("bossAddEmployee.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToFireEmployee(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("bossRemoveEmployee.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToAddPartyScreen(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("bossAddParty.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToRemovePartyScreen(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("bossRemoveParty.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToMessagesScreen(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("bossAnswerMessages.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToStatistics(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("statistics.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void registerNewEmployee(javafx.event.ActionEvent actionEvent){

        try {
            // testing if input fields are not empty
            if(firstname.getText().equals("")) {
                throw new Exception("Pole 'imię' nie może być puste");
            }
            if(lastname.getText().equals("")) {
                throw new Exception("Pole 'nazwisko' nie może być puste");
            }
            if(e_mail.getText().equals("")) {
                throw new Exception("Pole 'e-mail' nie może być puste");
            }
            if(id_number.getText().equals("")) {
                throw new Exception("Pole 'numer dowodu' nie może być puste");
            }
            if(login.getText().equals("")) {
                throw new Exception("Pole 'login' nie może być puste");
            }
            if(password.getText().equals("")) {
                throw new Exception("Pole 'hasło' nie może być puste");
            }
            if(salaryTextField.getText().equals("")) {
                throw new Exception("Pole 'pensja' nie może być puste");
            }

            try {
                double val = Double.parseDouble(salaryTextField.getText());
            } catch (NumberFormatException e) {
                throw new Exception("Pole 'pensja' musi być wartością typu double");
            }

            // testing if inputted passwords are the same
            if(!password.getText().equals(confirmPassword.getText())) {
                throw new Exception("Potwierdzenie hasła zakończyło się niepowodzeniem.\n" +
                        "Sprawdź czy nie masz wciśniętego CAPS LOCK'a");
            }

            // connecting to external database
            Connection connection = null;
            PreparedStatement psInsert = null;
            PreparedStatement psCheckUserExists = null;
            PreparedStatement psSelect = null;
            ResultSet resultSet = null;

            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/party_management_database", "root","root");
                psCheckUserExists = connection.prepareStatement("SELECT * FROM users WHERE login = ? OR e_mail = ? OR id_number = ?");
                psCheckUserExists.setString(1, login.getText());
                psCheckUserExists.setString(2, e_mail.getText());
                psCheckUserExists.setString(3, id_number.getText());
                resultSet = psCheckUserExists.executeQuery();

                if(resultSet.isBeforeFirst()) {
                    throw new Exception("Uzytkownik o podanych danych juz istnieje");
                }
                else {
                    psInsert = connection.prepareStatement("INSERT INTO users (id_number,firstname,lastname,e_mail,login,password,status) VALUES(?,?,?,?,?,?,?)");
                    psInsert.setString(1, id_number.getText());
                    psInsert.setString(2, firstname.getText());
                    psInsert.setString(3, lastname.getText());
                    psInsert.setString(4, e_mail.getText());
                    psInsert.setString(5, login.getText());
                    psInsert.setString(6, password.getText());
                    psInsert.setString(7,"employee");
                    psInsert.executeUpdate();

                    psSelect = connection.prepareStatement("SELECT user_id FROM users WHERE id_number = ?");
                    psSelect.setString(1, id_number.getText());
                    resultSet = psSelect.executeQuery();

                    psInsert = connection.prepareStatement("INSERT INTO salaries (user_id, salary) VALUES(?,?)");
                    resultSet.next();
                    psInsert.setString(1, Integer.toString(resultSet.getInt(1)));
                    psInsert.setString(2,salaryTextField.getText());
                    psInsert.executeUpdate();
                }
            } catch(Exception e) {
                throw new Exception(e.getMessage());
            } finally {
                if(resultSet != null) {
                    try {
                        resultSet.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if(psCheckUserExists != null) {
                    try {
                        psCheckUserExists.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if(psInsert != null) {
                    try {
                        psInsert.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if(connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            this.goBackToBossScreen(actionEvent);
        } catch(Exception e) {
            errorMessage.setText(e.getMessage());
            e.printStackTrace();
        }
    }
    public void registerNewParty(javafx.event.ActionEvent actionEvent) throws IOException {
        try {
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
            this.goBackToBossScreen(actionEvent);
        } catch(Exception e) {
            errorMessage.setText(e.getMessage());
            e.printStackTrace();
        }
    }
}
