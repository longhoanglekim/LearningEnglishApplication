package com.controller;

import com.dictionary.Database;
import com.ui.Model;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.Optional;

import static com.ui.Model.dictionary;

public class DashboardController implements Initializable {
    @FXML
    public Button flashcardButton;

    @FXML
    private Button dictionaryButton;

    @FXML
    private Button translateButton;

    @FXML
    private Button gameButton;

    @FXML
    private Button exportButton;

    @FXML
    private Button barButton;

    @FXML
    private ToggleButton switchModeButton;

    @FXML
    private TextFlow statusText;

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
        exportButton.setOnAction(actionEvent -> onExportClicked());
        flashcardButton.setOnAction(actionEvent -> onFlashcardClicked());

        switchModeButtonListener();
    }

    private void switchModeButtonListener() {
        switchModeButton.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!switchModeButton.isSelected()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Switch to local mode?");
                Optional<ButtonType> result = alert.showAndWait();
                if (!result.isPresent() || result.get() == ButtonType.CANCEL) {
                    switchModeButton.setSelected(true);
                    return;
                }
                Model.getInstance().getView().getSelectedMenuItem().setValue("toLocal");
                switchModeButton.setText("Connect\nMySQL");
            } else {
                Model.getInstance().getView().getSelectedMenuItem().setValue("toSQL");
            }
        });
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
        }
    }

    public void onDictionaryClicked() {
        Model.getInstance().getView().getSelectedMenuItem().setValue("Home");
        setStyleButtonOnClick("home");
    }

    public void onTranslateClicked() {
        Model.getInstance().getView().getSelectedMenuItem().setValue("Translate");
        setStyleButtonOnClick("translate");
    }

    public void onGameClicked() {
        Model.getInstance().getView().getSelectedMenuItem().setValue("Game");
        setStyleButtonOnClick("game");
    }

    public void onFlashcardClicked() {
        Model.getInstance().getView().getSelectedMenuItem().setValue("Flashcard");
        setStyleButtonOnClick("Flashcard");
    }

    public void onSwitchModeClicked() {
        Model.getInstance().getView().getSelectedMenuItem().setValue("SwitchMode");
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
            exportButton.setText("Export");
            flashcardButton.setText("Flashcard");
        } else {
            dictionaryButton.setText("");
            translateButton.setText("");
            gameButton.setText("");
            exportButton.setText("");
            flashcardButton.setText("");
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
        exportButton.setPrefWidth(width);
        flashcardButton.setPrefWidth(width);
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
        if (button.equals("Flashcard")) {
            flashcardButton.setId("button");
        } else {
            flashcardButton.setId("");
        }
    }

    public void onExportClicked() {
        dictionary.export(false);
    }
}
