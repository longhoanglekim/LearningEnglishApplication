package com.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static com.dictionary.Translator.translateEnToVi;
import static com.dictionary.Translator.translateViToEn;


public class TranslateController implements Initializable {
    @FXML
    public TextArea fromField;
    @FXML
    public TextArea toField;
    /*    @FXML
        public ImageView imageFromLanguage;
        @FXML
        public ImageView imageToLanguage;*/
    @FXML
    public Label labelFromLanguage;
    @FXML
    public Label labelToLanguage;
    @FXML
    public ListView<String> historyTranslate;
    Image imageVi, imageEn;
    boolean enToVi = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imageVi = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/icon/Vietnam.png")));
        imageEn = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/icon/UK.png")));
        historyTranslate.setVisible(false);
        fromField.styleProperty().bind(FontSizeManager.getInstance().fontSizeProperty().asString("-fx-font-size: %dpx;"));
        toField.styleProperty().bind(FontSizeManager.getInstance().fontSizeProperty().asString("-fx-font-size: %dpx;"));
        labelFromLanguage.styleProperty().bind(FontSizeManager.getInstance().fontSizeProperty().asString("-fx-font-size: %dpx;"));;
        labelToLanguage.styleProperty().bind(FontSizeManager.getInstance().fontSizeProperty().asString("-fx-font-size: %dpx;"));

        fromField.addEventFilter(KeyEvent.KEY_RELEASED, keyEvent -> {
            String res = fromField.getText();
            if (keyEvent.getCode() == KeyCode.ENTER) {
                res += "";
                // Xử lý Enter ở đây nếu cần
                keyEvent.consume(); // Ngăn Enter lan ra

            }
            try {
                res = fromField.getText();
                String[] tmp = res.split("\n");
//                for (String str : tmp) {
//                    System.out.println(str);
//                }
                System.out.println("-----------");
                //res = res.replace("\r\n", "<code>0</code>");
                String toTrans = "";
                if (enToVi && !res.isEmpty()) {
                    for (String str : tmp) {
                        toTrans += translateEnToVi(str) + "\n";
                    }

                } else if (!enToVi && !res.isEmpty()) {
                    toTrans = translateViToEn(res);
                }
                toField.setText(toTrans);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

    }

    public void onSwichClicked() {
        if(enToVi) {
            labelFromLanguage.setText("Vietnamese");
            labelToLanguage.setText("English");
/*            imageFromLanguage.setImage(imageVi);
            imageToLanguage.setImage(imageEn);*/
            enToVi = false;
        } else {
            labelFromLanguage.setText("English");
            labelToLanguage.setText("Vietnamese");
/*            imageFromLanguage.setImage(imageEn);
            imageToLanguage.setImage(imageVi);*/
            enToVi = true;
        }
    }
    public void onHistoryClicked() {
        historyTranslate.setVisible(true);
    }

}