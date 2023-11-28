package com.ui;

import com.controller.GameController;
import com.controller.PreloadController;
import com.controller.WindowController;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;


public class Preload extends Preloader {
    AnchorPane preloadpane;
    Scene scene;
    public Stage preloaderStage;

    @Override
    public void init() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Thread t2 = new Thread(() -> {
            Platform.runLater(() -> {
                Model.getInstance().getView().createView();
                try {
                    Model.getInstance().getView().init();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                latch.countDown();
            });
        });
        t2.start();
        latch.await();
        preloadpane = new FXMLLoader(getClass().getResource("Preload.fxml")).load();
        scene = new Scene(preloadpane);
    }

    @Override
    public void start(Stage primaryStage) throws IOException, InterruptedException {
        preloaderStage = primaryStage;
        preloaderStage.setScene(scene);
        preloaderStage.show();
        preloaderStage.centerOnScreen();

    }

    @Override
    public void handleStateChangeNotification(Preloader.StateChangeNotification info) {
        switch (info.getType()) {
            case BEFORE_START:
                preloaderStage.hide();
                break;
        }
    }

    @Override
    public void handleProgressNotification(ProgressNotification progressNotification) {
    }
}

