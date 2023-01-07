package com.example.partymanager_inzprog;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.sql.*;

public class EmployeeHelpDesk {
    // scene switching
    private Scene scene;
    private Stage stage;

    @FXML
    private TextField clientsEmail;
    @FXML
    private TextField messageTopic;
    @FXML
    private Label errorMessage;

    public void goBackToEmployee(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("employee.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void sendMessage() {
        if(clientsEmail.getText().equals("")) {
            errorMessage.setText("E-mail klienta nie może być pusty");
            return;
        }
        if(messageTopic.getText().equals("")) {
            errorMessage.setText("Temat wiadomości nie może być pusty");
            return;
        }
        Connection connection = null;
        PreparedStatement psSelect = null;
        ResultSet resultSet = null;
        String imie = null;
        String nazwisko = null;

        try {
            connection = connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/party_management_database", "root","root");
            psSelect = connection.prepareStatement("SELECT firstname, lastname FROM users WHERE user_id = ?");
            psSelect.setString(1,Integer.toString(CurrentUser.currentUserID));
            resultSet = psSelect.executeQuery();
            resultSet.next();

            imie = resultSet.getString("firstname");
            nazwisko = resultSet.getString("lastname");

            try {
                String uriString = "mailto:" + clientsEmail.getText() + "?subject=" + messageTopic.getText() + "&body=%0APracownik firmy,%0A"
                        + imie + " " + nazwisko;
                String replaceUriString = uriString.replaceAll("\\s+","%20");
                URI mailto = new URI(replaceUriString);
                Desktop.getDesktop().mail(mailto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(connection!=null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(psSelect!=null) {
                try {
                    psSelect.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(resultSet!=null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        errorMessage.setText("");
    }
}
