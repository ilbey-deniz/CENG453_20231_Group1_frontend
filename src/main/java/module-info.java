module com.group1.frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires org.json;
    requires com.fasterxml.jackson.databind;
    requires org.java_websocket;
    requires static lombok;


    opens com.group1.frontend to javafx.fxml;
    exports com.group1.frontend;
    exports com.group1.frontend.controllers;
    exports com.group1.frontend.utils;
    exports com.group1.frontend.events;
    exports com.group1.frontend.enums;
    exports com.group1.frontend.components;
    opens com.group1.frontend.controllers to javafx.fxml;
    exports com.group1.frontend.dto.websocketDto;
    exports com.group1.frontend.dto.httpDto;
}