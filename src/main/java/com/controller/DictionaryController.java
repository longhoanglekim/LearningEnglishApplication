package com.controller;

import com.dictionary.Speech;
import com.dictionary.Word;
import com.ui.Model;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import org.controlsfx.control.NotificationPane;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.util.*;

import static com.ui.View.dictionary;

/**
 * Controller for main view.
 * Currently, this controller is for testing and can be refactored later.
 */
public class DictionaryController implements Initializable {
    @FXML
    FontAwesomeIconView bookmarkDefIcon;

    @FXML
    FontAwesomeIconView searchIcon;

    @FXML
    FontAwesomeIconView historyIcon;

    @FXML
    FontAwesomeIconView bookmarkIcon;

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

    @FXML
    private Button speakButton;

    //private Dictionary dictionary;
    private String currentWord;
    private ListViewType listViewType;

    /**
     * Initialize the controller, updating the search view list.
     * Also set the action for the search field and list view.
     * @param url            URL.
     * @param resourceBundle ResourceBundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setStyleProperty();
        setStyleListButton("searchList");
        // Set button action for search field and list view.
        deleteButton.setVisible(false);
        bookmarkButton.setVisible(false);
        speakButton.setVisible(false);
        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (listViewType == ListViewType.SEARCH) {
                    lookupWordSearchField();
                }
            }
        });
        listOfWord.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                lookupWordListView();
            }
        });
        // Set button action for definition field.
        bookmarkButton.setOnAction(event -> configBookmark());
        speakButton.setOnAction(event -> {
            try {
                Speech.play(currentWord);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Set button action for list view status update.
        listViewType = ListViewType.SEARCH;
        searchList.setOnAction(event -> searchListView());
        historyList.setOnAction(event -> historyListView());
        bookmarkList.setOnAction(event -> bookmarkListView());
        onActionSearchField();
    }

    /**
     * Get the current word in list view and show its definition.
     */
    public void lookupWordListView() {
        currentWord = listOfWord.getSelectionModel().getSelectedItem();
        Word word = dictionary.lookUp(currentWord);
        if (word == null) {
            return;
        }
        definitionField.getChildren().clear();
        dictionary.getHistoryList().add(currentWord);
        setDefinitionField(word);
        /*if(dictionary.getBookmarkList().contains(currentWord)){
            colorFont.setFill(Paint.valueOf("#ff0000"));
        }
        else colorFont.setFill(Paint.valueOf("#000000"));*/
    }

    /**
     * Get the current word in search field and show its definition.
     */
    public void lookupWordSearchField() {
        currentWord = searchField.getText();
        Word word = dictionary.lookUp(currentWord);
        if (word == null) {
            return;
        }
        definitionField.getChildren().clear();
        dictionary.getHistoryList().add(currentWord);
        setDefinitionField(word);
    }

    /**
     * Show the word in the definition field.
     * @see #targetField
     * @see #pronounceField
     * @see #definitionField
     * @param word Word to show.
     */
    public void setDefinitionField(Word word) {
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
        if (!bookmarkButton.isVisible()) {
            bookmarkButton.setVisible(true);
        }
        if (!speakButton.isVisible()) {
            speakButton.setVisible(true);
        }
        if (dictionary.getBookmarkList().getList().contains(currentWord)) {
            bookmarkDefIcon.setId("bookmarked");
        } else {
            bookmarkDefIcon.setId("bookmark");
        }
    }

    /**
     * Add or remove the current word from the bookmark list.
     * If the word is already in the bookmark list, remove it.
     * Otherwise, add it to the bookmark list.
     * Also update the list view and button if bookmarkList is selected.
     * @see #listViewType
     * @see #bookmarkButton
     */
    public void configBookmark() {
        Notifications notification = Notifications.create()
                .title("Bookmark")
                .text("Added " + currentWord + " to bookmark list")
                .hideAfter(javafx.util.Duration.seconds(1))
                .position(Pos.BOTTOM_RIGHT)
                .graphic(null)
                .owner(Model.getInstance().getView().getDictionaryPane())
                .hideCloseButton();

        if (dictionary.getBookmarkList().getList().isEmpty())  {
            dictionary.getBookmarkList().add(currentWord);
            bookmarkDefIcon.setId("bookmarked");
            notification.show();
        } else {
            if (!dictionary.getBookmarkList().getList().contains(currentWord)) {
                dictionary.getBookmarkList().add(currentWord);
                bookmarkDefIcon.setId("bookmarked");
                notification.show();
            } else {
                dictionary.getBookmarkList().remove(currentWord);
                bookmarkDefIcon.setId("bookmark");
                notification.text("Removed " + currentWord + " from bookmark list");
                notification.show();
            }
        }
        // Update the list view if bookmarkList is selected.
        if (listViewType == ListViewType.BOOKMARK) {
            listOfWord.getItems().clear();
            listOfWord.getItems().addAll(dictionary.getBookmarkList().getList());
        }
    }
    /*public void updateListViewBookMark(){
        listOfWord.getItems().clear();
        List<String> target = null;
        String newValue = searchField.getText();
        if (listViewType == ListViewType.SEARCH) {
            target = dictionary.search(newValue);
        } else if (listViewType == ListViewType.HISTORY) {
            target = dictionary.searchHistory(newValue);
        } else if (listViewType == ListViewType.BOOKMARK) {
            target = dictionary.searchBookmark(newValue);
        }
        if (target != null) {
            int n = target.size();
            for (int i = 0; i < n; ++i) {
                if (dictionary.getBookmarkList().contains(target.get(i))) {
                    target.set(i, target.get(i) + " ♥");
                }
            }
            //add bookmark color
            listOfWord.getItems().addAll(target);
        }
    }*/

    /**
     * Search the dictionary and update the list view.
     */
    public void onActionSearchField() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Clear the list view if the search field is empty.
            if (newValue == null || newValue.isEmpty()) {
                deleteButton.setVisible(false);
                listOfWord.getItems().clear();
                if (listViewType == ListViewType.HISTORY) {
                    listOfWord.getItems().addAll(dictionary.getHistoryList().getList());
                } else if (listViewType == ListViewType.BOOKMARK) {
                    listOfWord.getItems().addAll(dictionary.getBookmarkList().getList());
                }
            // Otherwise, search the dictionary and update the list view.
            } else {
                if (!deleteButton.isVisible()) {
                    deleteButton.setVisible(true);
                }
                if (!newValue.equals(oldValue)) {
                    List<String> target = null;
                    if (listViewType == ListViewType.SEARCH) {
                        target = dictionary.search(newValue);
                    } else if (listViewType == ListViewType.HISTORY) {
                        target = dictionary.getHistoryList().search(newValue);
                    } else if (listViewType == ListViewType.BOOKMARK) {
                        target = dictionary.getBookmarkList().search(newValue);
                    }
                    if (target != null) {
                        /*int n = target.size();
                        for(int i = 0; i < n; ++i){
                            if(dictionary.getBookmarkList().contains(target.get(i))){
                                target.set(i,target.get(i) + " ♥");
                            }
                        }*/
                        //add bookmark color
                        listOfWord.getItems().setAll(target);
                        listOfWord.scrollTo(0);
                    }
                }
            }
        });
    }

    /**
     * Update the list view to search view.
     */
    public void searchListView() {
        searchField.clear();
        listOfWord.getItems().clear();
        listViewType = ListViewType.SEARCH;
        setStyleListButton("searchList");
    }

    /**
     * Update the list view to history view.
     */
    public void historyListView() {
        searchField.clear();
        listOfWord.getItems().clear();
        if (!dictionary.getHistoryList().getList().isEmpty()) {
            listOfWord.getItems().addAll(dictionary.getHistoryList().getList());
        }
        listViewType = ListViewType.HISTORY;
        setStyleListButton("historyList");
    }

    /**
     * Update the list view to bookmark view.
     */
    public void bookmarkListView() {
        searchField.clear();
        listOfWord.getItems().clear();
        if (!dictionary.getBookmarkList().getList().isEmpty()) {
            listOfWord.getItems().addAll(dictionary.getBookmarkList().getList());
        }
        listViewType = ListViewType.BOOKMARK;
        setStyleListButton("bookmarkList");
    }

    /**
     * Clear the search field.
     */
    @FXML
    public void onDeleteClick() {
        searchField.clear();
    }

    /**
     * Copy the text in the search field to the clipboard.
     */
    public void onCopyClick() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(searchField.getText());
        clipboard.setContent(content);
    }

    /**
     * Set the style for the list view button.
     * @param button The button to set style.
     */
    public void setStyleListButton(String button) {
        if (button == null) {
            return;
        }
        if (button.equals("searchList")) {
            searchList.setText("Search");
            searchIcon.setId("listbutton-icon-selected");
        } else {
            searchList.setText("");
            searchIcon.setId("listbutton-icon");
        }
        if (button.equals("historyList")) {
            historyList.setText("History");
            historyIcon.setId("listbutton-icon-selected");
        } else {
            historyList.setText("");
            historyIcon.setId("listbutton-icon");
        }
        if (button.equals("bookmarkList")) {
            bookmarkList.setText("Bookmark");
            bookmarkIcon.setId("listbutton-icon-selected");
        } else {
            bookmarkList.setText("");
            bookmarkIcon.setId("listbutton-icon");
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