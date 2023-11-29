package com.controller;

import com.dictionary.ACTION;
import com.dictionary.Word;
import com.task.LookupTask;
import com.task.SearchTask;
import com.task.TextToSpeechTask;
import com.ui.Model;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.controlsfx.control.Notifications;

import java.net.URL;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.ui.Model.dictionary;

/**
 * Controller for main view.
 * Currently, this controller is for testing and can be refactored later.
 */
public class DictionaryController implements Initializable {
    @FXML
    private FontAwesomeIconView bookmarkDefIcon;

    @FXML
    private FontAwesomeIconView searchIcon;

    @FXML
    private FontAwesomeIconView historyIcon;

    @FXML
    private FontAwesomeIconView bookmarkIcon;

    @FXML
    private FontAwesomeIconView formatInfoIcon;

    @FXML
    private FontAwesomeIconView validateIcon;

    @FXML
    private FontAwesomeIconView editIcon;

    @FXML
    private FontAwesomeIconView addIcon;

    @FXML
    private HBox buttonHBox;

    @FXML
    private VBox definitionField;

    @FXML
    private HBox searchHBox;

    @FXML
    private Label currentFieldLabel;

    @FXML
    private TextField searchField;

    @FXML
    private ListView<String> wordList;

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

    @FXML
    private TextField targetField;

    @FXML
    private TextField pronouceField;

    @FXML
    private Button editButton;

    @FXML
    private Button addButton;

    @FXML
    private Button removeButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button formatInfoButton;

    @FXML
    private HBox listviewToolBar;

    @FXML
    private Button removeSelectedButton;

    @FXML
    private Button removeAllButton;

    @FXML
    private Button moveUpButton;

    @FXML
    private Button moveDownButton;

    @FXML
    private Line seperateLine;

    private ListViewType listViewType;
    private Mode mode;
    private SearchTask searchTask;
    private TextToSpeechTask speechTask;
    private LookupTask lookupTask;
    private String currentWord;
    private static Word liveWord;
    private ExecutorService executorService;

    /**
     * Initialize the controller, updating the search view list.
     * Also set the action for the search field and list view.
     * @param url            URL.
     * @param resourceBundle ResourceBundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the executor service.
        executorService = Executors.newFixedThreadPool(20);
        
        // Set the style for the list view button.
        setStyleListButton("searchList");
        
        // Set visibility for some components. (Config the view at start)
        bookmarkButton.setVisible(false);
        speakButton.setVisible(false);
        cancelButton.setVisible(false);
        saveButton.setVisible(false);
        formatInfoButton.setVisible(false);
        validateIcon.setVisible(false);
        seperateLine.setVisible(false);
        listviewToolBar.setVisible(false);
        deleteButton.visibleProperty().bind(searchField.textProperty().isNotEmpty());
        removeSelectedButton.disableProperty().bind(wordList.getSelectionModel().selectedItemProperty().isNull());
        moveUpButton.disableProperty().bind(wordList.getSelectionModel().selectedItemProperty().isNull());
        moveDownButton.disableProperty().bind(wordList.getSelectionModel().selectedItemProperty().isNull());

        // Set the text field to be uneditable.
        targetField.setEditable(false);
        pronouceField.setEditable(false);

        // Set action for list view and search field.
        searchField.setOnKeyPressed(event -> onSearchFieldKeyPressed(event));
        wordList.setOnKeyPressed(event -> onWordListKeyPressed(event));
        wordList.setOnMouseClicked(event -> onWordListClicked());
        
        // Set button action in definition field when show definition.
        bookmarkButton.setOnAction(event -> configBookmark());
        speakButton.setOnAction(event -> onSpeakButton());

        // Set button action for edit, add, remove, cancel, save, format info, validate icon.
        editButton.setOnAction(event -> setEditField());
        addButton.setOnAction(event -> setAddField());
        removeButton.setOnAction(event -> setRemoveField());
        cancelButton.setOnAction(event -> onCancelEdit());
        saveButton.setOnAction(event -> onSaveButton());
        formatInfoButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                formatInfoIcon.setId("formatInfoIcon-selected");
            } else {
                formatInfoIcon.setId("formatInfoIcon");
            }
        });
        formatInfoButton.setOnAction(event -> onFormatInfoButton());
        removeSelectedButton.setOnAction(event -> onRemoveSelectedButton());
        removeAllButton.setOnAction(event -> onRemoveAllButton());
        moveUpButton.setOnAction(event -> onMoveUpButton());
        moveDownButton.setOnAction(event -> onMoveDownButton());

        // Set button action for list view status update.
        listViewType = ListViewType.SEARCH;
        mode = Mode.SEARCH;
        searchList.setOnAction(event -> searchListView());
        historyList.setOnAction(event -> historyListView());
        bookmarkList.setOnAction(event -> bookmarkListView());
        copyButton.setOnAction(event -> onCopyClick());
        deleteButton.setOnAction(event -> onDeleteClick());

        // Listener
        listenerSearchField();
        listenerTargetField();
        //searchListView();
        Platform.runLater(() -> searchField.requestFocus());
    }

    private void onSearchFieldKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (listViewType == ListViewType.SEARCH) {
                currentWord = searchField.getText();
                lookupWord();
            }
        }
        if (event.getCode() == KeyCode.DOWN) {
            wordList.requestFocus();
            wordList.getSelectionModel().select(0);
        }
    }

    private void onWordListKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (wordList.getSelectionModel().getSelectedItem() == null) {
                return;
            }
            currentWord = wordList.getSelectionModel().getSelectedItem();
            lookupWord();
        }
        else if (event.getCode() == KeyCode.PAGE_DOWN) {
            wordList.scrollTo(wordList.getSelectionModel().getSelectedIndex());
        }
        else if (event.getCode() == KeyCode.PAGE_UP) {
            wordList.scrollTo(wordList.getSelectionModel().getSelectedIndex());
        }
        else if (event.getCode() == KeyCode.UP
                && wordList.getSelectionModel().getSelectedIndex() == 0) {
            searchField.requestFocus();
        }
    }

    private void onWordListClicked() {
        if (wordList.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        currentWord = wordList.getSelectionModel().getSelectedItem();
        if (listViewType == ListViewType.HISTORY) {
            currentWord = currentWord.substring(1);
        }
        lookupWord();
    }

    private void onSaveButton() {
        if (mode == Mode.ADD) {
            onAddWordClick();
        } else if (mode == Mode.EDIT) {
            onSaveEditClick();
        }
    }

    private void onSpeakButton() {
        try {
            if (speechTask != null) {
                speechTask.cancel();
            }
            speechTask = new TextToSpeechTask(currentWord, "en");
            new Thread(speechTask).start();
        } catch (Exception e) {
            System.err.println(e.getMessage() + "\nCaused: " + e.getCause());
        }
    }

    private static void onFormatInfoButton() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Format Information");
        alert.setHeaderText("Format Information");
        alert.setContentText("""
                In order to have a pretty format, please follow these rules:
                Define '-' as a meaning block.
                Define '=' as a example follow with '+' as a translate example.
                Define '!' as a example of use.
                Define '*' as a part of speech.
                Define '|' as none type special.""");
        alert.showAndWait();
    }

    private void listenerTargetField() {
        targetField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                validateIcon.glyphNameProperty().setValue("TIMES");
                validateIcon.setId("validateIcon-rejected");
                return;
            }
            if (mode == Mode.ADD) {
                if (lookupTask != null && lookupTask.isRunning()) {
                    lookupTask.cancel();
                }
                lookupTask = new LookupTask(newValue);
                lookupTask.setOnSucceeded(event -> {
                    if (lookupTask.getValue() != null) {
                        validateIcon.glyphNameProperty().setValue("TIMES");
                        validateIcon.setId("validateIcon-rejected");
                    }
                    else {
                        validateIcon.glyphNameProperty().setValue("CHECK");
                        validateIcon.setId("validateIcon-accepted");
                    }
                });
                executorService.submit(lookupTask);
            } else if (mode == Mode.EDIT) {
                if (newValue.equals(currentWord)) {
                    validateIcon.glyphNameProperty().setValue("CHECK");
                    validateIcon.setId("validateIcon-accepted");
                } else {
                    lookupTask = new LookupTask(newValue);
                    lookupTask.setOnSucceeded(event -> {
                        if (lookupTask.getValue() != null) {
                            validateIcon.glyphNameProperty().setValue("TIMES");
                            validateIcon.setId("validateIcon-rejected");
                        } else {
                            validateIcon.glyphNameProperty().setValue("CHECK");
                            validateIcon.setId("validateIcon-accepted");
                        }
                        liveWord = lookupTask.getValue();
                    });
                    executorService.submit(lookupTask);
                }
            }
        });
    }

    private void onRemoveSelectedButton() {
        if (listViewType == ListViewType.HISTORY) {
            dictionary.getHistoryList().remove(wordList.getSelectionModel().getSelectedItem());
            wordList.getItems().remove(wordList.getSelectionModel().getSelectedItem());
        } else if (listViewType == ListViewType.BOOKMARK) {
            dictionary.getBookmarkList().remove(wordList.getSelectionModel().getSelectedItem());
            wordList.getItems().remove(wordList.getSelectionModel().getSelectedItem());
        }
    }

    private void onRemoveAllButton() {
        if (listViewType == ListViewType.HISTORY) {
            dictionary.getHistoryList().clear();
            wordList.getItems().clear();
        } else if (listViewType == ListViewType.BOOKMARK) {
            dictionary.getBookmarkList().clear();
            wordList.getItems().clear();
        }
    }

    private void onMoveUpButton() {
        int index = wordList.getSelectionModel().getSelectedIndex();
        if (index > 0) {
            String temp = wordList.getItems().get(index - 1);
            wordList.getItems().set(index - 1, wordList.getItems().get(index));
            wordList.getItems().set(index, temp);
            wordList.getSelectionModel().select(index - 1);
            Collections.swap(dictionary.getBookmarkList().getList(), index - 1, index);
        }
    }

    /**
     * Get the current word and show its definition.
     */
    public void lookupWord() {
        System.out.println("Lookup word: " + currentWord);
        if (notGiveAlert()) {
            return;
        }
        mode = Mode.SEARCH;
        if (currentWord == null) {
            return;
        }
        if (lookupTask != null && lookupTask.isRunning()) {
            lookupTask.cancel();
        }
        lookupTask = new LookupTask(currentWord);
        lookupTask.setOnSucceeded(event -> {
            setDefinitionField(lookupTask.getValue());
        });
        executorService.submit(lookupTask);
    }

    /**
     * Show the word in the definition field.
     * Additionally, for format rendering check {@link Word}.
     * @see #targetField
     * @see #pronouceField
     * @see #definitionField
     * @param word Word to show.
     */
    public void setDefinitionField(Word word) {
        definitionField.getChildren().clear();
        cancelButton.setVisible(false);
        saveButton.setVisible(false);
        formatInfoButton.setVisible(false);
        validateIcon.setVisible(false);
        targetField.setEditable(false);
        pronouceField.setEditable(false);
        targetField.setId("word");
        pronouceField.setId("wordpronounce");
        liveWord = word;

        if (word == null) {
            targetField.setText("Currently not available.");
            pronouceField.setText("");
            bookmarkButton.setVisible(false);
            speakButton.setVisible(false);
            currentWord = null;
            return;
        }
        targetField.setText(word.getTarget());
        pronouceField.setText(word.getPronounce());
        targetField.setPromptText("");
        pronouceField.setPromptText("");
        definitionField.setTranslateX(10);

        for (String s : word.getDefinition()) {
            if (s.isEmpty()) {
                continue;
            }
            Text result = new Text(s.substring(1).trim());
            TextFlow textFlow = new TextFlow(result);
            char firstChar = s.charAt(0);
            if (firstChar == '*') {
                VBox.setMargin(textFlow, new javafx.geometry.Insets(0, 10, 0, 5));
                result.setId("wordtype");
            } else if (firstChar == '-') {
                VBox.setMargin(textFlow, new javafx.geometry.Insets(0, 10, 0, 30));
                result.setId("wordmean");
            } else if (firstChar == '=') {
                VBox.setMargin(textFlow, new javafx.geometry.Insets(0, 10, 0, 50));
                result.setId("wordexample");
            } else if (firstChar == '!') {
                VBox.setMargin(textFlow, new javafx.geometry.Insets(0, 10, 0, 50));
                result.setId("wordexample");
            } else {
                VBox.setMargin(textFlow, new javafx.geometry.Insets(0, 10, 0, 10));
            }
            definitionField.getChildren().add(textFlow);
        }

        bookmarkButton.setVisible(true);
        speakButton.setVisible(true);
        if (dictionary.getBookmarkList().getList().contains(currentWord)) {
            bookmarkDefIcon.setId("bookmarked");
        } else {
            bookmarkDefIcon.setId("bookmark");
        }
    }

    /**
     * Set up the edit field.
     * @see #targetField
     * @see #pronouceField
     * @see #definitionField
     */
    public void setEditField() {
        if (notGiveAlert() || mode == Mode.EDIT) {
            return;
        }
        if (currentWord == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("No word selected.");
            alert.setContentText("Please select a word to edit.");
            alert.showAndWait();
            return;
        }
        if (lookupTask != null && lookupTask.isRunning()) {
            lookupTask.cancel();
        }
        liveWord = dictionary.lookup(currentWord);
        pronouceField.setEditable(true);
        targetField.setEditable(true);
        targetField.setText(liveWord.getTarget());
        pronouceField.setText(liveWord.getPronounce());
        pronouceField.setId("");
        targetField.setId("");

        speakButton.setVisible(false);
        bookmarkButton.setVisible(false);

        definitionField.getChildren().clear();
        TextArea result = new TextArea();
        lookupTask = new LookupTask(currentWord);
        lookupTask.setOnSucceeded(event -> {
            if (lookupTask.getValue() != null) {
                result.setText(lookupTask.getValue().getExplain());
            }
            liveWord = lookupTask.getValue();
        });
        executorService.submit(lookupTask);

        VBox.setVgrow(result, javafx.scene.layout.Priority.ALWAYS);
        VBox.setMargin(result, new Insets(0, 10, 0, -10));
        definitionField.getChildren().add(result);

        cancelButton.setVisible(true);
        saveButton.setVisible(true);
        saveButton.setText("Save");
        formatInfoButton.setVisible(true);
        validateIcon.setVisible(true);

        mode = Mode.EDIT;
    }

    /**
     * Set add field.
     * @see #targetField
     * @see #pronouceField
     * @see #definitionField
     */
    public void setAddField() {
        if (notGiveAlert() || mode == Mode.ADD) {
            return;
        }
        targetField.setText("");
        pronouceField.setText("");
        targetField.setPromptText("Target");
        pronouceField.setPromptText("Pronounce");
        definitionField.getChildren().clear();
        definitionField.setTranslateX(10);

        TextArea result = new TextArea();
        result.setPromptText("Definition");
        VBox.setVgrow(result, javafx.scene.layout.Priority.ALWAYS);
        VBox.setMargin(result, new Insets(0, 10, 0, -10));
        definitionField.getChildren().add(result);

        targetField.setEditable(true);
        pronouceField.setEditable(true);
        targetField.setId("");
        pronouceField.setId("");

        speakButton.setVisible(false);
        bookmarkButton.setVisible(false);

        cancelButton.setVisible(true);
        saveButton.setVisible(true);
        saveButton.setText("Add");
        formatInfoButton.setVisible(true);
        validateIcon.setVisible(true);

        mode = Mode.ADD;
    }

    public void setRemoveField() {
        if (notGiveAlert() || mode == Mode.REMOVE) {
            return;
        }
        if (currentWord == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("No word selected.");
            alert.setContentText("Please select a word to remove.");
            alert.showAndWait();
            return;
        }
        mode = Mode.REMOVE;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to remove this word?\n" +
                                "You can't undo this action.");
        alert.setContentText(currentWord);
        alert.getButtonTypes().setAll(ButtonType.NO, ButtonType.YES);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (!buttonType.isPresent() || buttonType.get() == ButtonType.NO) {
            mode = Mode.SEARCH;
            return;
        }
        dictionary.removeWord(currentWord);
        wordList.getItems().remove(currentWord);
        mode = Mode.SEARCH;
        currentWord = null;
        definitionField.getChildren().clear();
        targetField.setText("");
        pronouceField.setText("");
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
            wordList.getItems().clear();
            wordList.getItems().addAll(dictionary.getBookmarkList().getList());
        }
    }

    /**
     * Search the dictionary and update the list view.
     */
    public void listenerSearchField() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Clear the list view if the search field is empty.
            if (newValue == null || newValue.isEmpty()) {
                if (searchTask != null && searchTask.isRunning()) {
                    searchTask.cancel();
                }
                wordList.getItems().clear();
            // Otherwise, search the dictionary and update the list view.
            } else {
                if (!newValue.equals(oldValue)) {
                    // Force refresh the list view.
                    wordList.getItems().clear();
                    if (searchTask != null && searchTask.isRunning()) {
                        searchTask.cancel();
                    }
                    searchTask = new SearchTask(newValue);
                    searchTask.setOnSucceeded(event -> {
                        if (searchTask.getValue() != null && searchTask.getMessage().equals(newValue)) {
                            wordList.getItems().setAll(searchTask.getValue());
                            wordList.scrollTo(0);
                        }
                    });
                    executorService.submit(searchTask);
                }
            }
        });
    }

    /**
     * Validate the current word when the user try to add or edit a word.
     */
    
    public void onMoveDownButton() {
        int index = wordList.getSelectionModel().getSelectedIndex();
        if (index < wordList.getItems().size() - 1) {
            String temp = wordList.getItems().get(index + 1);
            wordList.getItems().set(index + 1, wordList.getItems().get(index));
            wordList.getItems().set(index, temp);
            wordList.getSelectionModel().select(index + 1);
            Collections.swap(dictionary.getBookmarkList().getList(), index + 1, index);
        }
    }
    
    /**
     * Update the list view to search view.
     */
    public void searchListView() {
        searchField.clear();
        wordList.getItems().clear();
        wordList.setCellFactory(param -> new Cell());
        listViewType = ListViewType.SEARCH;
        setStyleListButton("searchList");
        if (!searchHBox.isVisible()) {
            searchHBox.setVisible(true);
            AnchorPane.setTopAnchor(wordList, searchHBox.getHeight() + buttonHBox.getHeight());
        }
        AnchorPane.setBottomAnchor(wordList, 0.0);
        listviewToolBar.setVisible(false);
        seperateLine.setVisible(false);
    }

    /**
     * Update the list view to history view.
     */
    public void historyListView() {
        searchField.clear();
        wordList.getItems().clear();
        if (!dictionary.getHistoryList().getList().isEmpty()) {
            wordList.setCellFactory(param -> new HistoryCell());
            wordList.getItems().addAll(dictionary.getHistoryList().getList());
        }
        if (searchHBox.isVisible()) {
            searchHBox.setVisible(false);
            AnchorPane.setTopAnchor(wordList, buttonHBox.getHeight());
        }
        AnchorPane.setBottomAnchor(wordList, listviewToolBar.getHeight());
        listviewToolBar.setVisible(true);
        listviewToolBar.getChildren().remove(moveUpButton);
        listviewToolBar.getChildren().remove(moveDownButton);

        seperateLine.setVisible(true);

        listViewType = ListViewType.HISTORY;
        setStyleListButton("historyList");
    }

    /**
     * Update the list view to bookmark view.
     */
    public void bookmarkListView() {
        if (listViewType == ListViewType.BOOKMARK) {
            return;
        }
        searchField.clear();
        wordList.getItems().clear();
        if (!dictionary.getBookmarkList().getList().isEmpty()) {
            wordList.setCellFactory(param -> new BookmarkCell());
            wordList.getItems().addAll(dictionary.getBookmarkList().getList());
        }
        if (searchHBox.isVisible()) {
            searchHBox.setVisible(false);
            AnchorPane.setTopAnchor(wordList, buttonHBox.getHeight());
        }
        AnchorPane.setBottomAnchor(wordList, listviewToolBar.getHeight());
        listviewToolBar.setVisible(true);
        if (!listviewToolBar.getChildren().contains(moveUpButton)) {
            listviewToolBar.getChildren().add(moveUpButton);
        }
        if (!listviewToolBar.getChildren().contains(moveDownButton)) {
            listviewToolBar.getChildren().add(moveDownButton);
        }

        seperateLine.setVisible(true);

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
     * Cancel the current action.
     */
    public void onCancelEdit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Cancel editing.");
        alert.setContentText("Are you sure you want to cancel editing this word?\nAll changes will be lost.");
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (!buttonType.isPresent() || buttonType.get() == ButtonType.CANCEL) {
            return;
        }
        liveWord = dictionary.lookup(currentWord);
        setDefinitionField(liveWord);
        mode = Mode.SEARCH;

    }

    /**
     * Save the changes to the dictionary.
     */
    public void onSaveEditClick() {
        Node node = definitionField.getChildren().get(0);
        if (node instanceof TextArea textArea) {
            String newTarget = targetField.getText();
            String newPronounce = pronouceField.getText();
            String newExplain = textArea.getText();
            Word oldWord = dictionary.lookup(currentWord);

            if (validateIcon.glyphNameProperty().getValue().equals("TIMES_CIRCLE")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Word already exists.");
                alert.showAndWait();
                return;
            }

            StringBuilder alertMessage = new StringBuilder(oldWord.getTarget() + "\n");
            if (!newTarget.equals(oldWord.getTarget())) {
                alertMessage.append("Target: ");
                alertMessage.append(oldWord.getTarget());
                alertMessage.append(" -> ");
                alertMessage.append(newTarget);
                alertMessage.append("\n");
            }
            if (!newPronounce.equals(oldWord.getPronounce())) {
                alertMessage.append("Pronounce: ");
                alertMessage.append(oldWord.getPronounce());
                alertMessage.append(" -> ");
                alertMessage.append(newPronounce);
                alertMessage.append("\n");
            }
            if (!newExplain.equals(oldWord.getExplain())) {
                alertMessage.append("Explain: ");
                alertMessage.append(oldWord.getExplain());
                alertMessage.append(" -> ");
                alertMessage.append(newExplain);
                alertMessage.append("\n");
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Are you sure you want to edit this word?");
            alert.setContentText(alertMessage.toString());
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (!buttonType.isPresent() || buttonType.get() == ButtonType.CANCEL) {
                return;
            }

            Word newWord = new Word(newTarget, newPronounce, newExplain);
            if (!newTarget.equals(oldWord.getTarget())) {
                dictionary.removeWord(oldWord.getTarget());
                dictionary.addWord(newWord);
                wordList.getItems().set(wordList.getSelectionModel().getSelectedIndex(), newTarget);
                currentWord = newTarget;
            } else {
                dictionary.editWord(newWord);
            }
            setDefinitionField(newWord);
            mode = Mode.SEARCH;
        } else {
            System.err.println("Error: Node is not TextArea - " + node.getClass());
        }
    }

    public void onAddWordClick() {
        Node node = definitionField.getChildren().get(0);
        if (node instanceof TextArea textArea) {
            String newTarget = targetField.getText();
            String newPronounce = pronouceField.getText();
            String newExplain = textArea.getText();

            // Validate word, rely heavily on the validateIcon property
            if (validateIcon.glyphNameProperty().getValue().equals("TIMES")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Word is null or already exists.");
                alert.showAndWait();
                return;
            }
            Word newWord = new Word(newTarget, newPronounce, newExplain);

            // Add word to dictionary, if failed, show error message
            if (!dictionary.addWord(newWord)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to add word. Please try again.");
                alert.showAndWait();
                return;
            }
            currentWord = newTarget;
            Notifications notification = Notifications.create()
                    .title("Add Word")
                    .text("Added " + newTarget + " to dictionary")
                    .hideAfter(javafx.util.Duration.seconds(1))
                    .position(Pos.BOTTOM_RIGHT)
                    .graphic(null)
                    .owner(Model.getInstance().getView().getDictionaryPane())
                    .hideCloseButton();
            notification.show();
            mode = Mode.SEARCH;
            setAddField();
        } else {
            System.err.println("Error: Node is not TextArea - " + node.getClass());
        }
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



    /**
     * Give an alert when the user is about to cancel the current action.
     * Give an alert when the user in edit mode, add mode or remove mode.
     * @see #mode
     * @return True if the user wants to cancel the current action.
     */
    public boolean notGiveAlert() {
        if (mode == Mode.SEARCH) {
            return false;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Cancel " + mode.toString() + " word.");
        alert.setContentText("Are you sure you want to cancel "
                + mode.toString()
                + " this word?\n"
                + "All changes will be lost.");
        alert.getButtonTypes().setAll(ButtonType.NO, ButtonType.YES);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (!buttonType.isPresent()) {
            return true;
        } else if (buttonType.get() == ButtonType.NO) {
            return true;
        }
        return false;
    }

    /**
     * Custom cell for history list view.
     */
    static class HistoryCell extends ListCell<String> {
        HBox hBox = new HBox();
        FontAwesomeIconView iconView = new FontAwesomeIconView();
        FontAwesomeIconView iconView2 = new FontAwesomeIconView();

        public HistoryCell() {
            super();
            iconView.setId("listbutton-icon");
            iconView.setGlyphSize(15);
            iconView.setWrappingWidth(15);
            iconView2.glyphNameProperty().setValue("DATABASE");
            iconView2.setId("database-icon");
            iconView2.setGlyphSize(8);
            hBox.getChildren().addAll(iconView, iconView2);
        }
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null && !empty) {
                setText(item.substring(1));
                ACTION action = ACTION.parseAction(item);
                switch (action) {
                    case DADD -> {
                        iconView.glyphNameProperty().setValue("PLUS");
                        setGraphic(hBox);
                    }
                    case DREMOVE -> {
                        iconView.glyphNameProperty().setValue("MINUS");
                        setGraphic(hBox);
                    }
                    case DEDIT -> {
                        iconView.glyphNameProperty().setValue("PENCIL");
                        setGraphic(hBox);
                    }
                    case DLOOKUP -> {
                        iconView.glyphNameProperty().setValue("HISTORY");
                        setGraphic(hBox);
                    }
                    case LEDIT -> {
                        iconView.glyphNameProperty().setValue("PENCIL");
                        iconView2.setVisible(false);
                        setGraphic(hBox);
                    }
                    case LREMOVE -> {
                        iconView.glyphNameProperty().setValue("MINUS");
                        iconView2.setVisible(false);
                        setGraphic(hBox);
                    }
                    case LADD -> {
                        iconView.glyphNameProperty().setValue("PLUS");
                        iconView2.setVisible(false);
                        setGraphic(hBox);
                    }
                    default -> {
                        iconView.glyphNameProperty().setValue("HISTORY");
                        iconView2.setVisible(false);
                        setGraphic(hBox);
                    }
                }
            }
        }

        @Override
        public void updateSelected(boolean selected) {
            super.updateSelected(selected);
            if (selected) {
                iconView.setId("listbutton-icon-selected");
                iconView2.setId("listbutton-icon-selected");
            } else {
                iconView.setId("listbutton-icon");
                iconView2.setId("database-icon");
            }
        }
    }

    /**
     * Custom cell for bookmark list view.
     */
    static class BookmarkCell extends ListCell<String> {
        FontAwesomeIconView iconView = new FontAwesomeIconView();
        public BookmarkCell() {
            super();
            iconView.glyphNameProperty().setValue("BOOKMARK");
            iconView.setId("bookmarked");
            iconView.setGlyphSize(15);
        }
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);
            setGraphic(null);
            if (item != null && !empty) {
                setText(item);
                setGraphic(iconView);
            }
        }
    }

    /**
     * Simple cell for search list view.
     */
    static class Cell extends ListCell<String> {
        public Cell() {
            super();
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);
            setGraphic(null);
            if (item != null && !empty) {
                setText(item);
            }
        }
    }

    enum ListViewType {
        SEARCH, HISTORY, BOOKMARK
    }

    enum Mode {
        SEARCH, EDIT, ADD, REMOVE;

        public String toString() {
            return switch (this) {
                case EDIT -> "edit";
                case ADD -> "add";
                case REMOVE -> "remove";
                default -> "search";
            };
        }
    }
}