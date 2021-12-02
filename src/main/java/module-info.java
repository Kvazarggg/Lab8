module com.example.lab8 {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    requires java.sql;
    requires mysql.connector.java;

    opens com.example.lab8 to javafx.fxml;
    exports com.example.lab8;
}