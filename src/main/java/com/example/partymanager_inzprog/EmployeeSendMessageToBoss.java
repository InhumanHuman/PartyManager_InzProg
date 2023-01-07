package com.example.partymanager_inzprog;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class EmployeeSendMessageToBoss {
    // scene switching
    private Scene scene;
    private Stage stage;

    @FXML
    private TextArea employeeMessage;
    @FXML
    private TextField messageTopic;
    @FXML
    private Label errorMessage;

    public void goBackToEmployeeScreen(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("employee.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public boolean askBeforeConfirmation(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Wysyłanie wiadomości");
        alert.setHeaderText("Za chwilę wyślesz wiadomość do szefa firmy.");
        alert.setContentText("Czy na pewno chcesz kontynuować?");

        if(alert.showAndWait().get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    public void sendMail(javafx.event.ActionEvent actionEvent) {
        if(!askBeforeConfirmation()) {
            return;
        }
        if(employeeMessage.getText().equals("")) {
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

            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/party_management_database", "root","root");
            psInsert = connection.prepareStatement("INSERT INTO messages (temat,tresc_wiadomosci,status) VALUES(?,?,?)");
            psInsert.setString(1, messageTopic.getText());
            psInsert.setString(2, employeeMessage.getText());
            psInsert.setString(3, "nowa");
            psInsert.executeUpdate();

            psSelect = connection.prepareStatement("SELECT id_wiadomosci FROM messages WHERE temat = ? ORDER BY id_wiadomosci DESC");
            psSelect.setString(1,messageTopic.getText());
            resultSet = psSelect.executeQuery();
            resultSet.next();
            mssgID = resultSet.getInt(1);

            psInsert = connection.prepareStatement("INSERT INTO messages_users (id_wiadomosci, id_uzytkownika) VALUES(?,?)");
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
