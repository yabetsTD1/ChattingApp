module com.example.lastchatapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.rmi;
    requires java.xml;

    opens com.example.lastchatapp to javafx.fxml;
    exports com.example.lastchatapp;
}