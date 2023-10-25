package com.controller;

import com.ui.Model;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;

public class DashboardController implements Initializable {
    @FXML
    public Button home;
    public Button translate;
    public Button history;
    public Button bookmark;
    public Button game;
    public Button setting;

    public FontAwesomeIconView homeIcon;
    public FontAwesomeIconView historyIcon;
    public FontAwesomeIconView gameIcon;
    public FontAwesomeIconView translateIcon;


    @Override
    public void initialize(java.net.URL url, java.util.ResourceBundle resourceBundle) {
        setStyleButtonOnClick("home");
    }


    public void onHomeClicked() {
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

    public void onHistoryClicked() {
        Model.getInstance().getView().getSelectedMenuItem().setValue("History");
        setStyleButtonOnClick("history");
    }

    public void onGameClicked() {
        Model.getInstance().getView().getSelectedMenuItem().setValue("Game");
        setStyleButtonOnClick("game");
    }

    public void onSettingClicked() {
        Model.getInstance().getView().getSelectedMenuItem().setValue("Setting");
        setStyleButtonOnClick("setting");
    }

    public void setStyleButtonOnClick(String button) {
        if (button.equals("home")) {
            home.setId("button");
        } else {
            home.setId("");
        }
        if (button.equals("translate")) {
            translate.setId("button");
        } else {
            translate.setId("");
        }
        if (button.equals("history")) {
            history.setId("button");
        } else {
            history.setId("");
        }
        if (button.equals("bookmark")) {
            bookmark.setId("button");
        } else {
            bookmark.setId("");
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
