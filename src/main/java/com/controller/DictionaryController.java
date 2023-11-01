package com.controller;

import com.dictionary.Dictionary;
import com.dictionary.Local;
import com.dictionary.Word;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;

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
    private VBox definitionField;

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

    @FXML
    private Button deleteButton;

    @FXML
    private Button copyButton;

    @FXML
    private Button bookmarkButton;

    private Dictionary dictionary;
    private String currentWord;
    private ListViewType listViewType;

    /**
     * Initialize the controller, updating the search view list.
     *
     * @param url            URL.
     * @param resourceBundle ResourceBundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*Dictionary = new Database();
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
        deleteButton.setVisible(false);
        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onEnterPress();
            }
        });
        listOfWord.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                chooseWordClicked();
            }
        });
        bookmarkButton.setOnAction(event -> configBookmark());
        // Set action for list view.
        listViewType = ListViewType.SEARCH;
        searchList.setOnAction(event -> searchListView());
        historyList.setOnAction(event -> historyListView());
        bookmarkList.setOnAction(event -> bookmarkListView());
        try {
            onActionSearchField();
            listOfWord.setFixedCellSize(30);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chooseWordClicked() {
        currentWord = listOfWord.getSelectionModel().getSelectedItem();
        Word word = dictionary.lookUp(currentWord);
        if (word == null) return;
        definitionField.getChildren().clear();
        showWord(word);
        dictionary.addHistoryWord(currentWord);
    }

    public void onEnterPress() {
        currentWord = searchField.getText();
        Word word = dictionary.lookUp(currentWord);
        if (word == null) return;
        definitionField.getChildren().clear();
        showWord(word);
        dictionary.addHistoryWord(currentWord);
    }

    public void showWord(Word word) {
        targetField.setText(word.getTarget());
        pronounceField.setText(word.getPronounce());
        String explain = word.getExplain();
        Scanner sc = new Scanner(explain);
        int indexBlock = 1;
        while (sc.hasNextLine()) {
            Text result = new Text();
            String line = sc.nextLine();
            char firstChar = line.charAt(0);
            if (firstChar == '*') {
                result.setText(line.substring(1));
                result.setId("wordtype");
            } else if (firstChar == '-') {
                result.setText("\t" + indexBlock++ + ". " + line.substring(1));
                result.setId("wordmean");
            } else if (firstChar == '=') {
                String[] tmp = line.split("\\+");
                result.setText("\t\t٠ " + tmp[0].substring(1) + ":" + tmp[1]);
                result.setId("wordexample");
            } else if (firstChar == '!') {
                result.setText("\t\t٠ " + line.substring(1));
                result.setId("wordexample");
            }
            definitionField.getChildren().add(result);
        }
    }

    public void configBookmark() {
        System.out.println(currentWord);
        if (dictionary.getBookmarkList().isEmpty())  {
            dictionary.addBookmarkWord(currentWord);
            System.out.println("Added");
        } else {
            if (!dictionary.getBookmarkList().contains(currentWord)) {
                dictionary.addBookmarkWord(currentWord);
                System.out.println("Added");
            } else {
                dictionary.removeBookmarkWord(currentWord);
                System.out.println("Removed");
            }
        }
        listOfWord.getItems().clear();
        listOfWord.getItems().addAll(dictionary.getBookmarkList());
    }

    public void onActionSearchField() {
        // Filter list view with search box data.
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Clear the list view if the search field is empty.
            if (newValue == null || newValue.isEmpty()) {
                deleteButton.setVisible(false);
                listOfWord.getItems().clear();
                if (listViewType == ListViewType.HISTORY) {
                    listOfWord.getItems().addAll(dictionary.getHistoryList());
                } else {
                    listOfWord.getItems().addAll(dictionary.getBookmarkList());
                }
            // Otherwise, search the dictionary and update the list view.
            } else {
                if (!deleteButton.isVisible()) {
                    deleteButton.setVisible(true);
                }
                if (!newValue.equals(oldValue)) {
                    listOfWord.getItems().clear();
                    List<String> target = null;
                    if (listViewType == ListViewType.SEARCH) {
                        target = dictionary.search(newValue);
                    } else if (listViewType == ListViewType.HISTORY) {
                        target = dictionary.searchHistory(newValue);
                    } else if (listViewType == ListViewType.BOOKMARK) {
                        target = dictionary.searchBookmark(newValue);
                    }
                    if (target != null) {
                        listOfWord.getItems().addAll(target);
                    }
                }

            }
        });
    }

    public void searchListView() {
        searchField.clear();
        setStyleListButton("searchList");
        listOfWord.getItems().clear();
        listViewType = ListViewType.SEARCH;
    }

    public void historyListView() {
        searchField.clear();
        setStyleListButton("historyList");
        listOfWord.getItems().clear();
        if (!dictionary.getHistoryList().isEmpty()) {
            listOfWord.getItems().addAll(dictionary.getHistoryList());
        }
        listViewType = ListViewType.HISTORY;
    }

    public void bookmarkListView() {
        searchField.clear();
        setStyleListButton("bookmarkList");
        listOfWord.getItems().clear();
        if (!dictionary.getBookmarkList().isEmpty()) {
            listOfWord.getItems().addAll(dictionary.getBookmarkList());
        }
        listViewType = ListViewType.BOOKMARK;
    }

    public void onDeleteClick() {
        searchField.clear();
    }

    public void onCopyClick() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(searchField.getText());
        clipboard.setContent(content);
    }

    public void setStyleListButton(String button) {
        if (button == null) {
            return;
        }
        if (button.equals("searchList")) {
            searchList.setText("Search");
            searchList.setId("fontawesome-icon");
        } else {
            searchList.setText("");
            searchList.setId("listbutton-fontawesome-icon");
        }
        if (button.equals("historyList")) {
            historyList.setText("History");
            searchList.setId("fontawesome-icon");
        } else {
            historyList.setText("");
            searchList.setId("listbutton-fontawesome-icon");
        }
        if (button.equals("bookmarkList")) {
            bookmarkList.setText("Bookmark");
            searchList.setId("fontawesome-icon");
        } else {
            bookmarkList.setText("");
            searchList.setId("listbutton-fontawesome-icon");
        }
    }

    public void setStyleProperty() {
        searchField.styleProperty().bind(FontSizeManager.getInstance().fontSizeProperty().asString("-fx-font-size: %dpx;"));
        listOfWord.styleProperty().bind(FontSizeManager.getInstance().fontSizeProperty().asString("-fx-font-size: %dpx;"));
    }

    enum ListViewType {
        SEARCH, HISTORY, BOOKMARK
    }
}