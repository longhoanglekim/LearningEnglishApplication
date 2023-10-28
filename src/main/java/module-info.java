module com.ui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.almasb.fxgl.all;
    requires de.jensd.fx.glyphs.commons;
    requires de.jensd.fx.glyphs.materialdesignicons;
    requires de.jensd.fx.glyphs.fontawesome;

    opens com.ui to javafx.fxml;
    exports com.ui;
    exports com.controller;
    opens com.controller to javafx.fxml;
}