package com.ui;

import com.controller.WindowController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class View {
    private final StringProperty selectedMenuItem;
    private AnchorPane dictionaryPane;
    private AnchorPane dashboardPane;
    private AnchorPane historyPane;
    private AnchorPane translatePane;
    private AnchorPane gamePane;

    public View() {
        selectedMenuItem = new SimpleStringProperty("");
        try {
            dictionaryPane = new FXMLLoader(getClass().getResource("Dictionary.fxml")).load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //dictionaryPane = new FXMLLoader(getClass().getResource("Dictionary.fxml")).load();
    }

    public StringProperty getSelectedMenuItem() {
        return selectedMenuItem;
    }

    public AnchorPane getDictionaryPane() {
        if (dictionaryPane == null) {
            try {
                dictionaryPane = new FXMLLoader(getClass().getResource("Dictionary.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dictionaryPane;
    }

    public AnchorPane getDashboardPane() {
        if (dashboardPane == null) {
            try {
                dashboardPane = new FXMLLoader(getClass().getResource("Dashboard.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dashboardPane;
    }

    public AnchorPane getHistoryPane() {
        if (historyPane == null) {
            try {
                historyPane = new FXMLLoader(getClass().getResource("History.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return historyPane;
    }

    public AnchorPane getTranslatePane() {
        if (translatePane == null) {
            try {
                translatePane = new FXMLLoader(getClass().getResource("Translate.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return translatePane;
    }

    public AnchorPane getGamePane() {
        if (gamePane == null) {
            try {
                gamePane = new FXMLLoader(getClass().getResource("Game.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return gamePane;
    }

    public void showView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Window.fxml"));
        WindowController controller = new WindowController();
        loader.setController(controller);

        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Dictionary beta");
        stage.getIcons().add(new Image("file:src/main/resources/com/ui/zictionary.png"));
        stage.show();
    }
}
