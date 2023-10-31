package com.ui;

import javafx.scene.text.Text;

public class DefinitionBeautify {
    public static Text beautifyDef(Text text) {
        String temp = text.getText();
        switch (temp.charAt(0)) {
            case '*':
                text.setId("wordtype");
                break;
            case '-':
                text.setId("wordmean");
                break;
            case '=':
                text.setId("wordexample");
                break;
            default:
                break;
        }
        return text;
    }
}
