package com.example.partymanager_inzprog;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class RegisterEmployee {
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
    private Label errorMessage;

    public void switchToLogin(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
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

            // testing if inputted passwords are the same
            if(!password.getText().equals(confirmPassword.getText())) {
                throw new Exception("Potwierdzenie hasła zakończyło się niepowodzeniem.\n" +
                        "Sprawdź czy nie masz wciśniętego CAPS LOCK'a");
            }

            // connecting to external database
            Connection connection = null;
            PreparedStatement psInsert = null;
            PreparedStatement psCheckUserExists = null;
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
            this.switchToLogin(actionEvent);
        } catch(Exception e) {
            errorMessage.setText(e.getMessage());
            e.printStackTrace();
        }
    }
}
