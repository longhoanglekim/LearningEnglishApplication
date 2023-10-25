package com.ui;

public class Model {
    private final View view;
    private static Model instance;

    public Model() {
        view = new View();
    }

    public View getView() {
        return view;
    }

    public static Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }
}
