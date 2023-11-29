package com.ui;

import com.dictionary.Database;
import com.dictionary.Dictionary;
import com.dictionary.Local;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Model {
    private final View view;
    private static Model instance;
    private final BooleanProperty changeDictionary;
    public static Dictionary dictionary;

    private Model() {
        dictionary = new Local();
        if (!dictionary.initialize()) {
            System.out.println("Cannot initialize dictionary");
            Platform.exit();
        }
        changeDictionary = new SimpleBooleanProperty(false);
        view = new View();
    }

    public static void setDictionary(Dictionary dictionary) {
        Model.dictionary = dictionary;
    }

    public BooleanProperty getChangeDictionary() {
        if (dictionary instanceof Local) {
            changeDictionary.setValue(true);
        } else if (dictionary instanceof Database) {
            changeDictionary.setValue(false);
        }
        return changeDictionary;
    }

    public View getView() {
        return view;
    }

    /**
     * Get instance of model
     * @return instance of model
     */
    public static Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }
}
