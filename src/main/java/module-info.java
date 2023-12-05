module com.group1.frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires org.json;


    opens com.group1.frontend to javafx.fxml;
    exports com.group1.frontend;
    exports com.group1.frontend.controllers;
    exports com.group1.frontend.utils;
    opens com.group1.frontend.controllers to javafx.fxml;
}