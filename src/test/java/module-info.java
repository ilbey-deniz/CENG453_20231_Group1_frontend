module Frontend {
    requires org.junit.jupiter.api;
    requires org.testfx.junit5;
    requires javafx.fxml;
    requires javafx.graphics;
    requires junit;
    requires org.testfx;

    opens com.group1.frontend to
            javafx.fxml,
            javafx.base,
            javafx.graphics,
            javafx.controls,
            org.junit.platform.commons,
            org.junit.jupiter.api;
    exports com.group1.frontend;


}