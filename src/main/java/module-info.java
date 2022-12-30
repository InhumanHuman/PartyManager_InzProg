module com.example.partymanager_inzprog {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.example.partymanager_inzprog to javafx.fxml;
    exports com.example.partymanager_inzprog;
}