package com.ui;

import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {

    @Override
    public void init() throws Exception {

        Thread.sleep(2000);
    }

    public void start(Stage primaryStage) throws IOException {
        System.out.println("start");
        Model.getInstance().getView().showView();
    }

    public static void main(String[] args) {
        System.setProperty("javafx.preloader", Preload.class.getName());
        launch();
    }
}