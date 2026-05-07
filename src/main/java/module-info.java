module com.example.agencija {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;

    opens com.example.agencija to javafx.fxml;
    exports com.example.agencija;
    exports agencija;
    exports database;
}