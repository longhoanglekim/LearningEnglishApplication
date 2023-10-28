package com.controller;

import com.ui.Model;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;

public class DashboardController implements Initializable {
    @FXML
    public Button dictionary;
    public Button translate;
    public Button history;
    public Button bookmark;
    public Button game;
    public Button setting;

    public Button barButton;

    public FontAwesomeIconView homeIcon;
    public FontAwesomeIconView historyIcon;
    public FontAwesomeIconView gameIcon;
    public FontAwesomeIconView translateIcon;

    boolean isBarOpen = false;

    @Override
    public void initialize(java.net.URL url, java.util.ResourceBundle resourceBundle) {
        setStyleButtonOnClick("home");
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

    public void setTextVisible(boolean visible) {
        if (visible) {
            dictionary.setText("Dictionary");
            translate.setText("Translate");
            history.setText("History");
            bookmark.setText("Bookmark");
            game.setText("Game");
            setting.setText("Setting");
        } else {
            dictionary.setText("");
            translate.setText("");
            history.setText("");
            bookmark.setText("");
            game.setText("");
            setting.setText("");
        }
    }

    public void setWidthBar(int width) {
        dictionary.setPrefWidth(width);
        translate.setPrefWidth(width);
        history.setPrefWidth(width);
        bookmark.setPrefWidth(width);
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
