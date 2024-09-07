module com.bdc.dannyboymodding {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires java.desktop;

    opens com.bdc.dannyboymodding to javafx.fxml;
    exports com.bdc.dannyboymodding;
    exports com.bdc.dannyboymodding.utils;
    opens com.bdc.dannyboymodding.utils to javafx.fxml;
}