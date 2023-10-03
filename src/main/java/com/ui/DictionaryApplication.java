package com.ui;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class DictionaryApplication extends Application {

    @FXML
    private Button StartButton;

    @FXML
    FXMLLoader runningApp = new FXMLLoader(DictionaryApplication.class.getResource("Application.fxml"));


    @Override
    public void start(Stage stage) throws IOException {
        try {
            Scene scene  = new Scene(runningApp.load());

            //Set icon
            Image icon = new Image("file:src/main/resources/com/ui/zictionary.png");
            stage.getIcons().add(icon);
            //Stage settings
            stage.setTitle("Dictionary Application Demo");
            stage.setScene(scene);
            //stage.setResizable(true);
            stage.show();
            // System.out.println("Width :" + stage.getWidth()); 916.0
            // System.out.println("Height :" + stage.getHeight()); 539.0

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}