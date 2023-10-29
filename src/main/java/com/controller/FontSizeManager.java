package com.controller;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class FontSizeManager {
    private static FontSizeManager instance = new FontSizeManager();
    private IntegerProperty fontSizeProperty = new SimpleIntegerProperty(12);

    private FontSizeManager() {
    }

    public static FontSizeManager getInstance() {
        return instance;
    }

    public int getFontSize() {
        return fontSizeProperty.get();
    }

    public IntegerProperty fontSizeProperty() {
        return fontSizeProperty;
    }

    public void setFontSize(int fontSize) {
        fontSizeProperty.set(fontSize);
    }
}