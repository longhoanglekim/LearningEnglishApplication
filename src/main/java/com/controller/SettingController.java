package com.controller;

import com.ui.View;
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

    @FXML
    ChoiceBox<Integer> fontSize;

    @FXML
    Label labelFontSize;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fontSize.getItems().addAll(12, 14, 16, 18);
        fontSize.setValue(12);
    }

    public void onFontSizeSelected(ActionEvent event) {
        Integer selectedSize = fontSize.getValue();
        fontSize.getStyleClass().removeAll("my-button12", "my-button14");
        labelFontSize.getStyleClass().removeAll("my-label12", "my-label14");
        switch (selectedSize) {
            case 12:
                labelFontSize.getStyleClass().add("my-label12");
                fontSize.getStyleClass().add("my-button12");
                break;
            case 14:
                labelFontSize.getStyleClass().add("my-label14");
                fontSize.getStyleClass().add("my-button14");
                break;
            case 16:
                break;
            default:
                break;
        }

    }

}
