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
import java.net.URI;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.awt.Desktop;

public class ManageMessages implements Initializable {
    private Scene scene;
    private Stage stage;
    @FXML
    private TableColumn messageIDcolumn;
    @FXML
    private TableColumn topicColumn;
    @FXML
    private TableColumn contentColumn;
    @FXML
    private TableColumn userIDcolumn;
    @FXML
    private TableColumn firstNameColumn;
    @FXML
    private TableColumn lastNameColumn;
    @FXML
    private TableColumn emailColumn;
    @FXML
    private TableColumn statusColumn;
    @FXML
    private Label errorMessage;
    @FXML
    private TableView<MessagesData> messagesTableView;
    @FXML
    private TextArea contentTextArea;
    @FXML
    private ChoiceBox<String> statusChoiceBox;

    private static String clientEmail;
    private static String clientTopic;
    private static Integer messageID;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        messageIDcolumn.setCellValueFactory(new PropertyValueFactory<MessagesData, Integer>("id_wiadomosci"));
        topicColumn.setCellValueFactory(new PropertyValueFactory<MessagesData, String>("temat"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<MessagesData, String>("tresc_wiadomosci"));
        userIDcolumn.setCellValueFactory(new PropertyValueFactory<MessagesData, Integer>("user_id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<MessagesData, String>("firstname"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<MessagesData, String>("lastname"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<MessagesData, String>("e_mail"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<MessagesData, String>("status"));
        ObservableList<MessagesData> data = this.getData();
        messagesTableView.setItems(data);

        statusChoiceBox.getItems().add("Wysłano odpowiedź");
        statusChoiceBox.getItems().add("Zamknięte");
    }

    public boolean changeConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Uwaga");
        alert.setHeaderText("Za chwilę zmienisz status wiadomości.");
        alert.setContentText("Czy na pewno chcesz to zrobić?");

        if(alert.showAndWait().get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    private ObservableList<MessagesData> getData() {
        ObservableList<MessagesData> MessagesData = FXCollections.observableArrayList();
        Connection connection = null;
        PreparedStatement psSelect = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/party_management_database", "root","root");
            psSelect = connection.prepareStatement("SELECT messages.id_wiadomosci, messages.temat, messages.tresc_wiadomosci, messages.status, users.user_id, users.firstname, users.lastname, users.e_mail" +
                    " FROM users JOIN messages_users ON users.user_id = messages_users.id_uzytkownika JOIN messages ON messages.id_wiadomosci = messages_users.id_wiadomosci");
            resultSet = psSelect.executeQuery();
            while(resultSet.next()) {
                MessagesData.add(new MessagesData(resultSet.getInt("id_wiadomosci"), resultSet.getString("temat"), resultSet.getString("tresc_wiadomosci"),
                        resultSet.getInt("user_id"), resultSet.getString("firstname"), resultSet.getString("lastname"), resultSet.getString("e_mail"),
                        resultSet.getString("status")));
            }
        } catch (SQLException e) {
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
            if(resultSet!=null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return MessagesData;
    }

    public void changeStatus(javafx.event.ActionEvent actionEvent) {
        if(ManageMessages.messageID == null || ManageMessages.clientEmail == null || ManageMessages.clientTopic == null) {
            errorMessage.setText("Nie wybrano żadnej wiadomości!");
            return;
        }
        if(statusChoiceBox.getValue() == null) {
            errorMessage.setText("Wybierz status dla tej wiadomości");
            return;
        }

        if(!changeConfirmation()) {
            return;
        }

        Connection connection = null;
        PreparedStatement psUpdate = null;
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/party_management_database", "root","root");
            psUpdate = connection.prepareStatement("UPDATE messages SET status = ? WHERE id_wiadomosci = ?");
            if(statusChoiceBox.getValue().equals("Wysłano odpowiedź")) {
                psUpdate.setString(1, "wyslano_odpowiedz");
            } else {
                psUpdate.setString(1,"zamkniete");
            }
            psUpdate.setString(2,Integer.toString(ManageMessages.messageID));
            psUpdate.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(connection!=null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if(psUpdate!=null) {
                try {
                    psUpdate.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        try {
            this.switchToBoss(actionEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void respondToMail() {
        if(ManageMessages.messageID == null || ManageMessages.clientEmail == null || ManageMessages.clientTopic == null) {
            errorMessage.setText("Nie wybrano żadnej wiadomości!");
            return;
        }
        errorMessage.setText("");
        try {
            String uriString = "mailto:" + ManageMessages.clientEmail + "?subject=Re: " + ManageMessages.clientTopic;
            String replaceUriString = uriString.replaceAll("\\s+","%20");
            URI mailto = new URI(replaceUriString);
            Desktop.getDesktop().mail(mailto);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void displayContent() {
        MessagesData data = messagesTableView.getSelectionModel().getSelectedItem();
        String text = data.getTresc_wiadomosci();
        ManageMessages.messageID = data.getId_wiadomosci();
        ManageMessages.clientEmail = data.getE_mail();
        ManageMessages.clientTopic = data.getTemat();
        contentTextArea.setText(text);
    }

    public void switchToBoss(javafx.event.ActionEvent actionEvent) throws IOException {
        ManageMessages.messageID = null;
        ManageMessages.clientTopic = null;
        ManageMessages.clientEmail = null;

        Parent root = FXMLLoader.load(getClass().getResource("boss.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
