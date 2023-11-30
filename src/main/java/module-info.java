module com.group1.frontend {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.group1.frontend to javafx.fxml;
    exports com.group1.frontend;
}