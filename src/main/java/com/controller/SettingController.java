package com.controller;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingController implements Initializable {
    private IntegerProperty fontSizeProperty = new SimpleIntegerProperty(12);

    @FXML
    ChoiceBox<Integer> fontSize;

    @FXML
    Label labelFontSize;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fontSize.getItems().addAll(12, 14, 16, 18);
        fontSize.setValue(12);

        fontSize.valueProperty().addListener((observable, oldValue, newValue) -> {
            int selectedSize = fontSize.getValue();

            // Cập nhật IntegerProperty chung
            FontSizeManager.getInstance().setFontSize(selectedSize);

            // Cập nhật Label trong Setting.fxml
            labelFontSize.setStyle("-fx-font-size: " + selectedSize + "px;");
            fontSize.setStyle("-fx-font-size: " + selectedSize + "px;");
        });
    }

    public void onFontSizeSelected(ActionEvent event) {
        int selectedSize = fontSize.getValue();

        // Cập nhật Label trong Setting.fxml
        labelFontSize.setStyle("-fx-font-size: " + selectedSize + "px;");

        // Cập nhật TranslateX cho Label
        double translateX = (12 - selectedSize) * 6; // Điều chỉnh giá trị 5 theo mong muốn của bạn
        labelFontSize.setTranslateX(translateX);
    }

}
