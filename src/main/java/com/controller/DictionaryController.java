package com.controller;

import com.dictionary.Database;
import com.dictionary.Dictionary;
import com.dictionary.Local;
import com.dictionary.Word;
import com.ui.DefinitionBeautify;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
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

    @FXML
    private Button searchList;

    @FXML
    private Button historyList;

    @FXML
    private Button bookmarkList;

    private Dictionary dictionary;

    /**
     * Initialize the controller, updating the search view list.
     *
     * @param url            URL.
     * @param resourceBundle ResourceBundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*ictionary = new Database();
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
            setStyleProperty();
            searchList.setOnAction(event -> {
                dictionary.export();
            });

        try {
            onActionSearchField();
            listOfWord.setFixedCellSize(30);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chooseWordClikced() {
        String selected = listOfWord.getSelectionModel().getSelectedItem();
        Word word = dictionary.lookUp(selected);
        if (word == null) {
            return;
        }
        targetField.setText(word.getTarget());
        pronounceField.setText(word.getPronounce());
        String explain = word.getExplain();
        Scanner sc = new Scanner(explain);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Text newtext = new Text(line);
            newtext = DefinitionBeautify.beautifyDef(newtext);
            definitionField.appendText(newtext.getText() + "\n");
        }
    }

    public void onActionSearchField() {
        // Filter list view with search box data.
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                // Clear the list view if the search field is empty.
                listOfWord.getItems().clear();
            } else {
                // Filter the list view based on the search input.
                /*List<String> filteredWords = dictionary.getAllWordsTarget()
                        .stream()
                        .filter(word -> word.startsWith(newValue))
                        .collect(Collectors.toList());

                // Update the list view with filtered items.
                filteredWords.sort(Comparator.naturalOrder());*/
                if (!newValue.equals(oldValue)) {
                    listOfWord.getItems().clear();
                    List<String> target = dictionary.search(newValue);
                    if (target != null) {
                        listOfWord.getItems().addAll(target);
                    }
                }

            }
        });
    }

    public void setStyleProperty() {
        searchField.styleProperty().bind(FontSizeManager.getInstance().fontSizeProperty().asString("-fx-font-size: %dpx;"));
        listOfWord.styleProperty().bind(FontSizeManager.getInstance().fontSizeProperty().asString("-fx-font-size: %dpx;"));
        definitionField.styleProperty().bind(FontSizeManager.getInstance().fontSizeProperty().asString("-fx-font-size: %dpx;"));
        definitionField.setWrapText(true);

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
