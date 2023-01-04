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

public class EmployeeChangePartyCredentials implements Initializable {
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
    private TableColumn availableEntries1;
    @FXML
    private ChoiceBox idChoiceBox;
    @FXML
    private Label errorMessage;
    @FXML
    private TableView contentTable;

    @FXML
    RadioButton partyNameButton;
    @FXML
    RadioButton addressPartyButton;
    @FXML
    RadioButton startPartyButton;
    @FXML
    RadioButton endPartyButton;
    @FXML
    RadioButton pricePartyButton;
    @FXML
    RadioButton availableEntriesPartyButton;

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
    private TextField availableEntries;

    public void goBackToEmployeeScreen(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("employee.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToEmployeeRemovePartyScreen(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("employeeChangePartyCredentials.fxml"));
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
        availableEntries1.setCellValueFactory(new PropertyValueFactory<PartyData, Integer>("availableEntries"));

        ObservableList<PartyData> PartyData = this.getData();
        contentTable.setItems(PartyData);

        for(int i = 0; i < PartyData.size(); i++) {
            idChoiceBox.getItems().add(PartyData.get(i).getParty_id());
        }
    }
    public boolean changeCredentialcConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Uwaga");
        alert.setHeaderText("Za chwilę zmienisz dane imprezy");
        alert.setContentText("Czy na pewno chcesz to zrobić?");

        if(alert.showAndWait().get() == ButtonType.OK) {
            return true;
        }
        return false;
    }
    public void changePartyCredentials(javafx.event.ActionEvent actionEvent) throws IOException {
        if(!changeCredentialcConfirmation()) {
            return;
        }

        // connecting to external database
        boolean isFirst = false;
        Connection connection = null;
        PreparedStatement psChange = null;
        Integer currentValue = (Integer)idChoiceBox.getValue();

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/party_management_database", "root","root");
            String updateStatement = "UPDATE parties SET";
            //check all fields
            if(partyNameButton.isSelected()) {
                if(!partyName.getText().equals("")) {
                    if(!isFirst) {
                        isFirst = true;
                        updateStatement = updateStatement + " partyName = '" + partyName.getText() + "'";
                    } else {
                        updateStatement = updateStatement + " , partyName = '" + partyName.getText() + "'";
                    }
                } else {
                    throw new Exception("Zaznaczone pole 'nazwa imprezy' jest puste.");
                }
            }
            if(addressPartyButton.isSelected()) {
                if(!partyAddress.getText().equals("")) {
                    if(!isFirst) {
                        isFirst = true;
                        updateStatement = updateStatement + " partyAddress = '" + partyAddress.getText() + "'";
                    } else {
                        updateStatement = updateStatement + " , partyAddress = '" + partyAddress.getText() + "'";
                    }
                } else {
                    throw new Exception("Zaznaczone pole 'adres' jest puste.");
                }
            }
            if(startPartyButton.isSelected()) {
                if(!partyOpeningDate.toString().equals("")) {
                    // if ((partyOpeningDate.getValue()).isAfter((partyClosingDate).getValue())) {
                    //    throw new Exception("Data rozpoczęcia nie może być po dacie zakończenia");
                    //}
                    if(!isFirst) {
                        isFirst = true;
                        updateStatement = updateStatement + " partyOpeningDate = '" + partyOpeningDate.getValue() + "'";
                    } else {
                        updateStatement = updateStatement + " , partyOpeningDate = '" + partyOpeningDate.getValue() + "'";
                    }
                } else {
                    throw new Exception("Zaznaczone pole 'data rozpoczęcia' jest puste.");
                }
            }
            if(endPartyButton.isSelected()) {
                if(!partyClosingDate.toString().equals("")) {
                    //if ((partyClosingDate.getValue()).isBefore((partyOpeningDate).getValue())) {
                    //    throw new Exception("Data zakończenia nie może być przed datą rozpoczęcia");
                    //}
                    if(!isFirst) {
                        isFirst = true;
                        updateStatement = updateStatement + " partyClosingDate = '" + partyClosingDate.getValue() + "'";
                    } else {
                        updateStatement = updateStatement + " , partyClosingDate = '" + partyClosingDate.getValue() + "'";
                    }
                } else {
                    throw new Exception("Zaznaczone pole 'data zakończenia' jest puste.");
                }
            }
            if(pricePartyButton.isSelected()) {
                if(!priceOfTheParty.getText().equals("")) {
                    if(!isFirst) {
                        isFirst = true;
                        updateStatement = updateStatement + " priceOfTheParty = '" + priceOfTheParty.getText() + "'";
                    } else {
                        updateStatement = updateStatement + " , priceOfTheParty = '" + priceOfTheParty.getText() + "'";
                    }
                } else {
                    throw new Exception("Zaznaczone pole 'cena' jest puste.");
                }
            }
            if(availableEntriesPartyButton.isSelected()) {
                if(!availableEntries.getText().equals("")) {
                    if(!isFirst) {
                        isFirst = true;
                        updateStatement = updateStatement + " availableEntries = '" + availableEntries.getText() + "'";
                    } else {
                        updateStatement = updateStatement + " , availableEntries = '" + availableEntries.getText() + "'";
                    }
                } else {
                    throw new Exception("Zaznaczone pole 'liczba dostępnych miejsc' jest puste.");
                }
            }

            updateStatement = updateStatement + " WHERE party_id = " + currentValue;
            psChange = connection.prepareStatement(updateStatement);
            psChange.executeUpdate();
            throw new Exception("Zmieniono dane imprezy.");

        } catch(Exception e) {
            errorMessage.setText(e.getMessage());
        } finally {
            if(psChange != null) {
                try {
                    psChange.close();
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
            this.switchToEmployeeRemovePartyScreen(actionEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
