package com.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class DictionaryApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DictionaryApplication.class.getResource("Application.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        //Set icon
        Image icon = new Image("file:src/main/resources/com/ui/zictionary.png");
        stage.getIcons().add(icon);
        //Stage settings
        stage.setTitle("Dictionary Application Demo");
        stage.setScene(scene);
        //stage.setResizable(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}