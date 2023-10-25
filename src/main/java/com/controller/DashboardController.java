package com.controller;

import com.ui.Model;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;

public class DashboardController implements Initializable {
    @FXML
    public Button home;
    public Button history;
    public Button game;
    public Button translate;
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

    public void onHistoryClicked() {
        Model.getInstance().getView().getSelectedMenuItem().setValue("History");
        setStyleButtonOnClick("history");
    }

    public void onGameClicked() {
        Model.getInstance().getView().getSelectedMenuItem().setValue("Game");
        setStyleButtonOnClick("game");
    }

    public void onTranslateClicked() {
        Model.getInstance().getView().getSelectedMenuItem().setValue("Translate");
        setStyleButtonOnClick("translate");
    }

    public void setStyleButtonOnClick(String button) {
        switch (button) {
            case "home":
                home.setId("button");
                history.setId("");
                game.setId("");
                translate.setId("");
                break;
            case "history":
                home.setId("");
                history.setId("button");
                game.setId("");
                translate.setId("");
                break;
            case "game":
                home.setId("");
                history.setId("");
                game.setId("button");
                translate.setId("");
                break;
            case "translate":
                home.setId("");
                history.setId("");
                game.setId("");
                translate.setId("button");
                break;
        }
    }
}
