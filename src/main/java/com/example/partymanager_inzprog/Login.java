package com.example.partymanager_inzprog;

import javafx.event.ActionEvent;
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

public class Login {
    // scene switching
    private Scene scene;
    private Stage stage;

    @FXML
    private TextField login;
    @FXML
    private PasswordField password;
    @FXML
    private Label errorMessage;

    public void switchToEmployeeScreen(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("employee.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToBossScreen(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("boss.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToRegisterEmployeeScreen(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("register.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public int checkCredentials(Label errorMessage, String login, String password, ActionEvent actionEvent){
        try {
            if(login.equals("")) {
                throw new Exception("Pole 'login' nie może być puste");
            }
            if(password.equals("")) {
                throw new Exception("Pole 'hasło' nie może być puste");
            }

            // connecting to external database
            Connection connection = null;
            PreparedStatement psInsert = null;
            PreparedStatement psCheckUserExists = null;
            ResultSet resultSet = null;

            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/party_management_database", "root","root");
                psCheckUserExists = connection.prepareStatement("SELECT * FROM users WHERE login = ? AND password = ?");
                psCheckUserExists.setString(1, login);
                psCheckUserExists.setString(2, password);
                resultSet = psCheckUserExists.executeQuery();

                if(resultSet.isBeforeFirst()) {
                    resultSet.next();

                    if(resultSet.getString(8).equals("employee")) {
                        CurrentUser.currentUserID = resultSet.getInt(1);
                        System.out.println(CurrentUser.currentUserID);
                        return 1;
                    } else if (resultSet.getString(8).equals("boss")) {
                        CurrentUser.currentUserID = resultSet.getInt(1);
                        System.out.println(CurrentUser.currentUserID);
                        return 2;
                    }
                } else {
                    throw new Exception("Użytkownik o podanych danych nie istnieje");
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
        }  catch(Exception e) {
            errorMessage.setText(e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    public void confirmLogin(javafx.event.ActionEvent actionEvent) throws Exception {
        String loginText = login.getText();
        String passwordText = password.getText();
        int userType = checkCredentials(errorMessage, loginText, passwordText, actionEvent);
        switch(userType) {
            case 1:
                this.switchToEmployeeScreen(actionEvent);
                break;
            case 2:
                this.switchToBossScreen(actionEvent);
                break;
        }
    }
}
