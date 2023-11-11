package com.ui;

import com.controller.WindowController;
import com.dictionary.Database;
import com.dictionary.Dictionary;
import com.dictionary.Local;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;


public class View {
    private final StringProperty selectedMenuItem;
    private AnchorPane dashboardPane;
    private AnchorPane dictionaryPane;
    private AnchorPane translatePane;
    private AnchorPane gamePane;
    private AnchorPane settingPane;

    public static Dictionary dictionary;

    public View() {
        selectedMenuItem = new SimpleStringProperty("");
//        dictionary = new Database();
//        if (!dictionary.initialize()) {
            dictionary = new Local();
            if (!dictionary.initialize()) {
                System.out.println("Cannot initialize dictionary");
                Platform.exit();
            }
//        }
    }

    public StringProperty getSelectedMenuItem() {
        return selectedMenuItem;
    }

    /**
     * Get dictionary pane
     * @return dictionary pane
     */
    public AnchorPane getDictionaryPane() {
        if (dictionaryPane == null) {
            try {
                dictionaryPane = new FXMLLoader(getClass().getResource("Dictionary.fxml")).load();
            } catch (IOException e) {
                Logger.getLogger(View.class.getName()).info("Cannot load dictionary data.");
            }
        }
        return dictionaryPane;
    }

    /**
     * Get dashboard pane
     * @return dashboard pane
     */
    public AnchorPane getDashboardPane() {
        if (dashboardPane == null) {
            try {
                dashboardPane = new FXMLLoader(getClass().getResource("Dashboard.fxml")).load();
            } catch (IOException e) {
                Logger.getLogger(View.class.getName()).info("Cannot load dashboard pane - FXML.");
            }
        }
        return dashboardPane;
    }

    /**
     * Get translate pane
     * @return translate pane
     */
    public AnchorPane getTranslatePane() {
        if (translatePane == null) {
            try {
                translatePane = new FXMLLoader(getClass().getResource("Translate.fxml")).load();
            } catch (IOException e) {
                Logger.getLogger(View.class.getName()).info("Cannot load translate pane - FXML.");
                e.printStackTrace();
            }
        }
        return translatePane;
    }

    /**
     * Get game pane
     * @return game pane
     */
    public AnchorPane getGamePane() {
        if (gamePane == null) {
            try {
                gamePane = new FXMLLoader(getClass().getResource("Game.fxml")).load();
            } catch (IOException e) {
                Logger.getLogger(View.class.getName()).info("Cannot load game pane - FXML.");
            }
        }
        return gamePane;
    }

    /**
     * Get setting pane
     * @return setting pane
     */
    public AnchorPane getSettingPane() {
        if (settingPane == null) {
            try {
                settingPane = new FXMLLoader(getClass().getResource("Setting.fxml")).load();
            } catch (IOException e) {
                Logger.getLogger(View.class.getName()).info("Cannot load setting pane - FXML.");
            }
        }
        return settingPane;
    }

    /**
     * Show window view with default: LEFT = Dashboard, CENTER = Dictionary.
     */
    public void showView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Window.fxml"));
        WindowController controller = new WindowController();
        loader.setController(controller);

        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            Logger.getLogger(View.class.getName()).info("Cannot load window view - FXML.");
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Dictionary beta");
        stage.getIcons().add(new Image("file:src/main/resources/com/ui/zictionary.png"));
        stage.setMinWidth(800);
        stage.setMinHeight(550);
        stage.show();
        stage.setOnCloseRequest(event -> {
            dictionary.close();
            System.out.println("Closing application");
            Platform.exit();
            System.exit(0);
        });
    }

}
