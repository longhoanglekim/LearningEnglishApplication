package com.controller;

import com.ui.Model;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import static com.ui.Model.dictionary;

public class DashboardController implements Initializable {
    @FXML
    private Button dictionaryButton;

    @FXML
    private Button translateButton;

    @FXML
    private Button gameButton;

    @FXML
    private Button settingButton;

    @FXML
    private Button exportButton;

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
        dictionaryButton.setOnAction(actionEvent -> onDictionaryClicked());
        translateButton.setOnAction(actionEvent -> onTranslateClicked());
        gameButton.setOnAction(actionEvent -> onGameClicked());
        settingButton.setOnAction(actionEvent -> onSettingClicked());
        exportButton.setOnAction(actionEvent -> onExportClicked());
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

    /**
     * Set text for all button in bar (When resize the HBox)
     * @param visible true if visible, false if not
     */
    public void setTextVisible(boolean visible) {
        if (visible) {
            dictionaryButton.setText("Dictionary");
            translateButton.setText("Translate");
            gameButton.setText("Game");
            settingButton.setText("Setting");
            exportButton.setText("Export");
        } else {
            dictionaryButton.setText("");
            translateButton.setText("");
            gameButton.setText("");
            settingButton.setText("");
            exportButton.setText("");
        }
    }

    /**
     * Set width for all button in bar (Resizing the HBox)
     * @param width width of button
     */
    public void setWidthBar(int width) {
        dictionaryButton.setPrefWidth(width);
        translateButton.setPrefWidth(width);
        gameButton.setPrefWidth(width);
        settingButton.setPrefWidth(width);
        exportButton.setPrefWidth(width);
    }

    /**
     * Set style for button when clicked
     * @param button button clicked
     */
    public void setStyleButtonOnClick(String button) {
        if (button.equals("home")) {
            dictionaryButton.setId("button");
        } else {
            dictionaryButton.setId("");
        }
        if (button.equals("translate")) {
            translateButton.setId("button");
        } else {
            translateButton.setId("");
        }
        if (button.equals("game")) {
            gameButton.setId("button");
        } else {
            gameButton.setId("");
        }
        if (button.equals("setting")) {
            settingButton.setId("button");
        } else {
            settingButton.setId("");
        }
    }

    public void onExportClicked() {
        dictionary.export();
    }
}
