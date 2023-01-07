package com.example.partymanager_inzprog;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Statistics implements Initializable {

    // scene switching
    private Scene scene;
    private Stage stage;

    @FXML
    private Label totalEmployees;
    @FXML
    private Label totalSalaryCost;
    @FXML
    private Label totalParties;
    @FXML
    private Label totalEarnings;
    @FXML
    private Label selectedEarnings;
    @FXML
    private Label selectedDifference;
    @FXML
    private ChoiceBox<String> monthChoiceBox;
    @FXML
    private ChoiceBox<String> yearChoiceBox;
    @FXML
    private Label errorMessage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for(int i = 1; i < 13; i++) {
            monthChoiceBox.getItems().add(Integer.toString(i));
        }
        for(int i = 2005; i < 2024; i++) {
            yearChoiceBox.getItems().add(Integer.toString(i));
        }

        Connection connection = null;
        PreparedStatement psSelect = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/party_management_database", "root","root");
            psSelect = connection.prepareStatement("SELECT COUNT(*) as row_count, SUM(salary) as total_salary FROM salaries");
            resultSet = psSelect.executeQuery();
            resultSet.next();
            totalEmployees.setText(Integer.toString(resultSet.getInt("row_count")));
            totalSalaryCost.setText(Double.toString(resultSet.getDouble("total_salary")));

            psSelect = connection.prepareStatement("SELECT COUNT(*) as row_count, SUM(priceOfTheParty) as total_earnings FROM parties");
            resultSet = psSelect.executeQuery();
            resultSet.next();
            totalParties.setText(Integer.toString(resultSet.getInt("row_count")));
            totalEarnings.setText(Double.toString(resultSet.getDouble("total_earnings")));

        } catch(SQLException e) {
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
    }

    public void calculate() {
        if(monthChoiceBox.getValue() == null) {
            errorMessage.setText("Nie wybrano docelowego miesiąca");
            return;
        }
        if(yearChoiceBox.getValue() == null) {
            errorMessage.setText("Nie wybrano docelowego roku");
            return;
        }

        Connection connection = null;
        PreparedStatement psSelect = null;
        ResultSet resultSet = null;
        Double selectedEarningsTotal = null;
        Double totalSalary = null;
        Double difference = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/party_management_database", "root","root");
            psSelect = connection.prepareStatement("SELECT SUM(salary) as total_salary FROM salaries");
            resultSet = psSelect.executeQuery();
            resultSet.next();
            totalSalary = resultSet.getDouble("total_salary");
            psSelect = connection.prepareStatement("SELECT SUM(priceOfTheParty) as total_price FROM parties WHERE YEAR(partyOpeningDate) = ? AND MONTH(partyOpeningDate) = ?");
            psSelect.setString(1,yearChoiceBox.getValue());
            psSelect.setString(2,monthChoiceBox.getValue());
            resultSet = psSelect.executeQuery();
            resultSet.next();
            selectedEarningsTotal = resultSet.getDouble("total_price");
            selectedEarnings.setText(Double.toString(selectedEarningsTotal));
            difference = selectedEarningsTotal - totalSalary;
            selectedDifference.setText(Double.toString(difference));

        } catch(SQLException e) {
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

    public void goBackToBoss(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("boss.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
