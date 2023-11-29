package com.ui;

import com.controller.WindowController;
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

import static com.ui.Model.dictionary;


public class View {
    private final StringProperty selectedMenuItem;
    private AnchorPane dashboardPane;
    private AnchorPane dictionaryPane;
    private AnchorPane translatePane;
    private AnchorPane gamePane;
    private AnchorPane flashcardPane;
    private AnchorPane loginSQLPane;

    public View() {
        selectedMenuItem = new SimpleStringProperty("");
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
                Logger.getLogger(View.class.getName()).info("Cannot load dictionary data.\n" + e.getCause());
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
                Logger.getLogger(View.class.getName()).info("Cannot load dashboard pane - FXML.\n" + e.getCause());
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
                Logger.getLogger(View.class.getName()).info("Cannot load translate pane - FXML.\n" + e.getCause());
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
                Logger.getLogger(View.class.getName()).info("Cannot load game pane - FXML.\n" + e.getCause());
            }
        }
        return gamePane;
    }

    public AnchorPane getFlashcardPane() {
        if (flashcardPane == null) {
            try {
                flashcardPane = new FXMLLoader(getClass().getResource("Flashcard.fxml")).load();
            } catch (IOException e) {
                Logger.getLogger(View.class.getName()).info("Cannot load Flashcard pane - FXML.\n" + e.getCause());
            }
        }
        return flashcardPane;
    }
    
    public AnchorPane getLoginSQLPane() {
        if (loginSQLPane == null) {
            try {
                loginSQLPane = new FXMLLoader(getClass().getResource("LoginSQLController.fxml")).load();
            } catch (IOException e) {
                Logger.getLogger(View.class.getName()).info("Cannot load LoginSQLController pane - FXML.");
            }
        }
        return loginSQLPane;
    }

    /**
     * Create a stage 'window' with default borderPane: LEFT = Dashboard, CENTER = Dictionary.
     */
    public void createView() {
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
        stage.getIcons().add(new Image("file:src/main/resources/com/icon/Dictionary.png"));
        stage.setMinWidth(800);
        stage.setMinHeight(550);
        stage.setOnCloseRequest(event -> {
            dictionary.close();
            System.out.println("Application closed!");
            Platform.exit();
            System.exit(0);
        });
    }

    /**
     * Show window view.
     */
    public void showView() {
        Stage stage = (Stage) dashboardPane.getScene().getWindow();
        stage.show();
    }

    /**
     * Hide window view.
     */
    public void hideView() {
        Stage stage = (Stage) dashboardPane.getScene().getWindow();
        stage.hide();
    }

    public void showLoginSQLView() {
        Scene scene = getLoginSQLPane().getScene();
        if (scene == null) {
            scene = new Scene(getLoginSQLPane());
        }
        Stage stage = new Stage();
        stage.initOwner(dashboardPane.getScene().getWindow());
        stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void init() throws IOException {
        dictionaryPane = new FXMLLoader(getClass().getResource("Dictionary.fxml")).load();
        gamePane = new FXMLLoader(getClass().getResource("Game.fxml")).load();
        translatePane = new FXMLLoader(getClass().getResource("Translate.fxml")).load();
    }
}
