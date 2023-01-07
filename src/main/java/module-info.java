module com.example.partymanager_inzprog {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.example.partymanager_inzprog to javafx.fxml;
    exports com.example.partymanager_inzprog;
}