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
import java.time.LocalDate;
import java.util.ResourceBundle;

public class BossRemoveParty implements Initializable {
    // scene switching
    private Scene scene;
    private Stage stage;

    @FXML
    private TableColumn idColumn;
    @FXML
    private TableColumn nameColumn;
    @FXML
    private TableColumn addressColumn;
    @FXML
    private TableColumn beginColumn;
    @FXML
    private TableColumn endColumn;
    @FXML
    private TableColumn priceColumn;
    @FXML
    private ChoiceBox idChoiceBox;
    @FXML
    private TableView contentTable;

    public void goBackToBossScreen(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("boss.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToBossRemovePartyScreen(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("bossRemoveParty.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public ObservableList<PartyData> getData() {
        ObservableList<PartyData> partyData = FXCollections.observableArrayList();

        // connecting to external database
        Connection connection = null;
        PreparedStatement psGetParties = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/party_management_database", "root", "root");
            psGetParties = connection.prepareStatement("SELECT party_id, partyName, partyAddress, partyOpeningDate, partyClosingDate, priceOfTheParty, availableEntries FROM parties");
            resultSet = psGetParties.executeQuery();
            while (resultSet.next()) {
                partyData.add(new PartyData(resultSet.getInt("party_id"), resultSet.getString("partyName"),
                        resultSet.getString("partyAddress"), LocalDate.parse(resultSet.getString("partyOpeningDate")),
                        LocalDate.parse(resultSet.getString("partyClosingDate")), resultSet.getDouble("priceOfTheParty"),
                        resultSet.getInt("availableEntries")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psGetParties != null) {
                try {
                    psGetParties.close();
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
        return partyData;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(new PropertyValueFactory<PartyData, Integer>("party_id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<PartyData, String>("partyName"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<PartyData, String>("partyAddress"));
        beginColumn.setCellValueFactory(new PropertyValueFactory<PartyData, LocalDate>("partyOpeningDate"));
        endColumn.setCellValueFactory(new PropertyValueFactory<PartyData, LocalDate>("partyClosingDate"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<PartyData, Integer>("priceOfTheParty"));

        ObservableList<PartyData> userData = this.getData();
        contentTable.setItems(userData);

        for(int i = 0; i < userData.size(); i++) {
            idChoiceBox.getItems().add(userData.get(i).getParty_id());
        }
    }
    public boolean deleteConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Uwaga");
        alert.setHeaderText("Za chwilę usuniesz imprezę");
        alert.setContentText("Czy na pewno chcesz usunąć imprezę?\n" +
                "Operacja jest nieodwracalna!!!");

        if(alert.showAndWait().get() == ButtonType.OK) {
            return true;
        }
        return false;
    }
    public void removeParty(javafx.event.ActionEvent actionEvent) throws IOException {
        if(!deleteConfirmation()) {
            return;
        }
        // connecting to external database
        Connection connection = null;
        PreparedStatement psDelete = null;
        Integer currentValue = (Integer)idChoiceBox.getValue();
        try {
            if(currentValue == null) {
                throw new Exception("Wybierz imprezę, którą chcesz usunąć.");
            }
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/party_management_database", "root", "root");
            psDelete = connection.prepareStatement("DELETE FROM parties WHERE party_id = ?");
            psDelete.setString(1, currentValue.toString());
            psDelete.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (psDelete != null) {
                try {
                    psDelete.close();
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
        try {
            this.switchToBossRemovePartyScreen(actionEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
