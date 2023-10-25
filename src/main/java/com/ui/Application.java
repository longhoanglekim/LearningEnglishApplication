package com.ui;

import javafx.stage.Stage;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage)  {
        Model model = new Model();
        model.getView().showView();
    }

    public static void main(String[] args) {
        launch();
    }
}