package com.controller;

import com.ui.Model;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.FadeTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class DashboardController implements Initializable {
    @FXML
    private Button dictionary;

    @FXML
    private Button translate;

    @FXML
    private Button game;

    @FXML
    private Button setting;

    @FXML
    private Button barButton;

    @FXML
    private VBox buttonVBox;

    public FontAwesomeIconView homeIcon;
    public FontAwesomeIconView historyIcon;
    public FontAwesomeIconView gameIcon;
    public FontAwesomeIconView translateIcon;

    boolean isBarOpen = false;

    @Override
    public void initialize(java.net.URL url, java.util.ResourceBundle resourceBundle) {
        setStyleButtonOnClick("home");
        barButton.setOnAction(actionEvent -> onBarClicked());
        dictionary.setOnAction(actionEvent -> onDictionaryClicked());
        translate.setOnAction(actionEvent -> onTranslateClicked());
        game.setOnAction(actionEvent -> onGameClicked());
        setting.setOnAction(actionEvent -> onSettingClicked());
    }

    public void onBarClicked() {
        if (isBarOpen) {
            isBarOpen = false;
            setTextVisible(false);
            setWidthBar(50);
            Model.getInstance().getView().getSelectedMenuItem().setValue("BarClose");
        } else {
            isBarOpen = true;
            setTextVisible(true);
            setWidthBar(130);
            Model.getInstance().getView().getSelectedMenuItem().setValue("BarOpen");
        };
    }

    public void onDictionaryClicked() {
        Model.getInstance().getView().getSelectedMenuItem().setValue("Home");
        setStyleButtonOnClick("home");
    }

    public void onTranslateClicked() {
        Model.getInstance().getView().getSelectedMenuItem().setValue("Translate");
        setStyleButtonOnClick("translate");
    }

    public void onBookmarkClicked() {
        Model.getInstance().getView().getSelectedMenuItem().setValue("Bookmark");
        setStyleButtonOnClick("bookmark");
    }


    public void onGameClicked() {
        Model.getInstance().getView().getSelectedMenuItem().setValue("Game");
        setStyleButtonOnClick("game");
    }

    public void onSettingClicked() {
        Model.getInstance().getView().getSelectedMenuItem().setValue("Setting");
        setStyleButtonOnClick("setting");
    }

    public void setTextVisible(boolean visible) {
        if (visible) {
            dictionary.setText("Dictionary");
            translate.setText("Translate");
            game.setText("Game");
            setting.setText("Setting");
        } else {
            dictionary.setText("");
            translate.setText("");
            game.setText("");
            setting.setText("");
        }
    }

    public void setWidthBar(int width) {
        dictionary.setPrefWidth(width);
        translate.setPrefWidth(width);
        game.setPrefWidth(width);
        setting.setPrefWidth(width);
    }

    public void setStyleButtonOnClick(String button) {
        if (button.equals("home")) {
            dictionary.setId("button");
        } else {
            dictionary.setId("");
        }
        if (button.equals("translate")) {
            translate.setId("button");
        } else {
            translate.setId("");
        }
        if (button.equals("game")) {
            game.setId("button");
        } else {
            game.setId("");
        }
        if (button.equals("setting")) {
            setting.setId("button");
        } else {
            setting.setId("");
        }
    }
}
