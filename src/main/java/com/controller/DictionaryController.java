package com.controller;

import com.dictionary.Database;
import com.dictionary.Dictionary;
import com.dictionary.Local;
import com.dictionary.Word;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller for main view.
 * Currently, this controller is for testing and can be refactored later.
 */
public class DictionaryController implements Initializable {
    @FXML
    private Label targetField;

    @FXML
    private Label pronounceField;

    @FXML
    private TextArea definitionField;

    @FXML
    private TextField searchField;

    @FXML
    private ListView<String> listOfWord;

    private Dictionary dictionary;

    private final ObservableList<String> wordData = FXCollections.observableArrayList();

    /**
     * Initialize the controller, updating the search view list.
     *
     * @param url            URL.
     * @param resourceBundle ResourceBundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*dictionary = new Database();
        if (dictionary.initialize()) {
            System.out.println("Database initialized.");
        } else {*/
            dictionary = new Local();
            if (dictionary.initialize()) {
                System.out.println("Local dictionary initialized.");
            } else {
                System.out.println("Cannot initialize dictionary.");
            }
        //}

        try {
            onActionSearchField();
            listOfWord.setFixedCellSize(30);
        } catch (Exception e) {
            e.printStackTrace();
        }
        targetField.styleProperty().bind(FontSizeManager.getInstance().fontSizeProperty().asString("-fx-font-size: %dpx;"));
        definitionField.styleProperty().bind(FontSizeManager.getInstance().fontSizeProperty().asString("-fx-font-size: %dpx;"));
        searchField.styleProperty().bind(FontSizeManager.getInstance().fontSizeProperty().asString("-fx-font-size: %dpx;"));
        listOfWord.styleProperty().bind(FontSizeManager.getInstance().fontSizeProperty().asString("-fx-font-size: %dpx;"));
        pronounceField.styleProperty().bind(FontSizeManager.getInstance().fontSizeProperty().asString("-fx-font-size: %dpx;"));
    }

    public void chooseWordClikced() {
        String selected = listOfWord.getSelectionModel().getSelectedItem();
        Word word = dictionary.lookUp(selected);
        targetField.setText(word.getTarget());
        pronounceField.setText("Phiên âm : " + word.getPronounce());
        definitionField.setText(word.getExplain());
    }

    public void onActionSearchField() {
        // Filter list view with search box data.
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                // Clear the list view if the search field is empty.
                listOfWord.getItems().clear();
            } else {
                // Filter the list view based on the search input.
                List<String> filteredWords = dictionary.getAllWordsTarget()
                        .stream()
                        .filter(word -> word.startsWith(newValue))
                        .collect(Collectors.toList());

                // Update the list view with filtered items.
                filteredWords.sort(Comparator.naturalOrder());
                listOfWord.getItems().setAll(filteredWords);

            }
        });
    }
}
/*=======
    public void onListViewCellClick() {
        String query = "SELECT detail FROM tbl_edict WHERE word = " + listOfWord.getSelectionModel().getSelectedItem();
        listOfWord.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {

            }
            return false;
        });
    }
}
>>>>>>> Stashed changes*/
