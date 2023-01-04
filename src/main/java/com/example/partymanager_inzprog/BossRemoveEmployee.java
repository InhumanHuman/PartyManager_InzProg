package com.example.partymanager_inzprog;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class BossRemoveEmployee implements Initializable {
    // scene switching
    private Scene scene;
    private Stage stage;

    @FXML
    private TableColumn idColumn;
    @FXML
    private TableColumn firstnameColumn;
    @FXML
    private TableColumn lastnameColumn;
    @FXML
    private TableColumn emailColumn;
    @FXML
    private TableColumn idNumberColumn;
    @FXML
    private TableColumn loginColumn;
    @FXML
    private TableColumn statusColumn;
    @FXML
    private TableView contentTable;

    @FXML
    private ChoiceBox idChoiceBox;
    @FXML
    private Label errorMessage;

    public void goBackToBossScreen(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("boss.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void goBackToDeleteEmployeeScreen(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("bossRemoveEmployee.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @Override
    public void initialize(URL url, ResourceBundle resurceBundle) {
        idColumn.setCellValueFactory(new PropertyValueFactory<UserData, Integer>("user_id"));
        idNumberColumn.setCellValueFactory(new PropertyValueFactory<UserData, String>("id_number"));
        firstnameColumn.setCellValueFactory(new PropertyValueFactory<UserData, String>("firstname"));
        lastnameColumn.setCellValueFactory(new PropertyValueFactory<UserData, String>("lastname"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<UserData, String>("e_mail"));
        loginColumn.setCellValueFactory(new PropertyValueFactory<UserData, String>("login"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<UserData, String>("status"));
        ObservableList<UserData> userData = this.getData();
        contentTable.setItems(userData);

        for(int i = 0; i < userData.size(); i++) {
            idChoiceBox.getItems().add(userData.get(i).getUser_id());
        }
    }
    public boolean deleteConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Uwaga");
        alert.setHeaderText("Za chwilę usuniesz pracownika");
        alert.setContentText("Czy na pewno chcesz usunąć pracownika?\n" +
                "Operacja jest nieodwracalna.");

        if(alert.showAndWait().get() == ButtonType.OK) {
            return true;
        }
        return false;
    }
    public ObservableList<UserData> getData() {
        ObservableList<UserData> userData = FXCollections.observableArrayList();

        // connecting to external database
        Connection connection = null;
        PreparedStatement psGetEmployee = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/party_management_database", "root","root");
            psGetEmployee = connection.prepareStatement("SELECT user_id, id_number, firstname, lastname, e_mail, login, password, status FROM users WHERE status = 'employee'");
            resultSet = psGetEmployee.executeQuery();
            while(resultSet.next()) {
                userData.add(new UserData(resultSet.getInt("user_id"), resultSet.getString("id_number"),
                         resultSet.getString("firstname"), resultSet.getString("lastname"),
                        resultSet.getString("e_mail"), resultSet.getString("login"), resultSet.getString("password"),
                        resultSet.getString("status")));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psGetEmployee != null) {
                try {
                    psGetEmployee.close();
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
        return userData;
    }
    public void deleteEmployee(javafx.event.ActionEvent actionEvent) {
        if(!deleteConfirmation()) {
            return;
        }
        // connecting to external database
        Connection connection = null;
        PreparedStatement psDelete = null;
        Integer currentValue = (Integer)idChoiceBox.getValue();
        try {
            if(currentValue == null) {
                throw new Exception("Najpierw wybierz pracownika, którego chcesz zwolnić.");
            }
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/party_management_database", "root","root");
            psDelete = connection.prepareStatement("DELETE FROM users WHERE user_id = ?");
            psDelete.setString(1,currentValue.toString());
            psDelete.executeUpdate();
        } catch(Exception e) {
            errorMessage.setText(e.getMessage());
        } finally {
            if(psDelete != null) {
                try {
                    psDelete.close();
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
        try {
            this.goBackToDeleteEmployeeScreen(actionEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
