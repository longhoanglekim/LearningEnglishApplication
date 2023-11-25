package com.controller;

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

    @FXML
    private TextField tfield;

    @FXML
    private TextField pfield;

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
    private static Word currentWordObject;

    /**
     * Initialize the controller, updating the search view list.
     * Also set the action for the search field and list view.
     * @param url            URL.
     * @param resourceBundle ResourceBundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //setStyleProperty();
        setStyleListButton("searchList");
        // Set button action for search field and list view.
        bookmarkButton.setVisible(false);
        speakButton.setVisible(false);
        cancelButton.setVisible(false);
        saveButton.setVisible(false);
        formatInfoButton.setVisible(false);
        validateIcon.setVisible(false);
        seperateLine.setVisible(false);
        listviewToolBar.setVisible(false);
        deleteButton.visibleProperty().bind(searchField.textProperty().isNotEmpty());
        removeSelectedButton.disableProperty().bind(listOfWord.getSelectionModel().selectedItemProperty().isNull());
        moveUpButton.disableProperty().bind(listOfWord.getSelectionModel().selectedItemProperty().isNull());
        moveDownButton.disableProperty().bind(listOfWord.getSelectionModel().selectedItemProperty().isNull());

        tfield.setEditable(false);
        pfield.setEditable(false);

        // Set action for list view and search field.
        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (listViewType == ListViewType.SEARCH) {
                    currentWord = searchField.getText();
                    LookupWord();
                }
            }
            if (event.getCode() == KeyCode.DOWN) {
                listOfWord.requestFocus();
                listOfWord.getSelectionModel().select(0);
            }
        });
        listOfWord.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                currentWord = listOfWord.getSelectionModel().getSelectedItem();
                LookupWord();
            }
            if (event.getCode() == KeyCode.PAGE_DOWN) {
                listOfWord.scrollTo(listOfWord.getSelectionModel().getSelectedIndex());
            }
            if (event.getCode() == KeyCode.PAGE_UP) {
                listOfWord.scrollTo(listOfWord.getSelectionModel().getSelectedIndex());
            }
            if (event.getCode() == KeyCode.UP && listOfWord.getSelectionModel().getSelectedIndex() == 0) {
                searchField.requestFocus();
            }
        });
        listOfWord.setOnMouseClicked(event -> {
            currentWord = listOfWord.getSelectionModel().getSelectedItem();
            LookupWord();
        });

        // Set button action in definition field.
        bookmarkButton.setOnAction(event -> configBookmark());
        speakButton.setOnAction(event -> {
            try {
                if (speechTask != null) {
                    speechTask.cancel();
                }
                speechTask = new TextToSpeechTask(currentWord, "en");
                new Thread(speechTask).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        editButton.setOnAction(event -> setEditField());
        addButton.setOnAction(event -> setAddField());
        removeButton.setOnAction(event -> setRemoveField());
        cancelButton.setOnAction(event -> onCancelEditClick());
        saveButton.setOnAction(event -> {
            if (mode == Mode.ADD) {
                onAddWordClick();
            } else if (mode == Mode.EDIT) {
                onSaveEditClick();
            }
        });
        formatInfoButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                formatInfoIcon.setId("formatInfoIcon-selected");
            } else {
                formatInfoIcon.setId("formatInfoIcon");
            }
        });
        formatInfoButton.setOnAction(event -> {
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
        });
        tfield.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                validateIcon.glyphNameProperty().setValue("TIMES");
                validateIcon.setId("validateIcon-rejected");
                return;
            }
            if (mode == Mode.ADD) {
                if (dictionary.lookup(newValue) != null) {
                    validateIcon.glyphNameProperty().setValue("TIMES");
                    validateIcon.setId("validateIcon-rejected");
                } else {
                    validateIcon.glyphNameProperty().setValue("CHECK");
                    validateIcon.setId("validateIcon-accepted");
                }
            } else if (mode == Mode.EDIT) {
                if (newValue.equals(currentWord)) {
                    validateIcon.glyphNameProperty().setValue("CHECK");
                    validateIcon.setId("validateIcon-accepted");
                } else {
                    currentWordObject = dictionary.lookup(tfield.getText());
                    if (currentWordObject != null) {
                        validateIcon.glyphNameProperty().setValue("TIMES");
                        validateIcon.setId("validateIcon-rejected");
                    } else {
                        validateIcon.glyphNameProperty().setValue("CHECK");
                        validateIcon.setId("validateIcon-accepted");
                    }
                }
            }
        });

        removeSelectedButton.setOnAction(event -> {
            if (listViewType == ListViewType.HISTORY) {
                dictionary.getHistoryList().remove(listOfWord.getSelectionModel().getSelectedItem());
                listOfWord.getItems().remove(listOfWord.getSelectionModel().getSelectedItem());
            } else if (listViewType == ListViewType.BOOKMARK) {
                dictionary.getBookmarkList().remove(listOfWord.getSelectionModel().getSelectedItem());
                listOfWord.getItems().remove(listOfWord.getSelectionModel().getSelectedItem());
            }
        });

        removeAllButton.setOnAction(event -> {
            if (listViewType == ListViewType.HISTORY) {
                dictionary.getHistoryList().getList().clear();
                listOfWord.getItems().clear();
            } else if (listViewType == ListViewType.BOOKMARK) {
                dictionary.getBookmarkList().getList().clear();
                listOfWord.getItems().clear();
            }
        });

        moveUpButton.setOnAction(event -> {
            int index = listOfWord.getSelectionModel().getSelectedIndex();
            if (index > 0) {
                String temp = listOfWord.getItems().get(index - 1);
                listOfWord.getItems().set(index - 1, listOfWord.getItems().get(index));
                listOfWord.getItems().set(index, temp);
                listOfWord.getSelectionModel().select(index - 1);
                Collections.swap(dictionary.getBookmarkList().getList(), index - 1, index);
            }
        });

        moveDownButton.setOnAction(event -> {
            int index = listOfWord.getSelectionModel().getSelectedIndex();
            if (index < listOfWord.getItems().size() - 1) {
                String temp = listOfWord.getItems().get(index + 1);
                listOfWord.getItems().set(index + 1, listOfWord.getItems().get(index));
                listOfWord.getItems().set(index, temp);
                listOfWord.getSelectionModel().select(index + 1);
                Collections.swap(dictionary.getBookmarkList().getList(), index + 1, index);
            }
        });

        // Set button action for list view status update.
        listViewType = ListViewType.SEARCH;
        mode = Mode.SEARCH;
        searchList.setOnAction(event -> searchListView());
        historyList.setOnAction(event -> historyListView());
        bookmarkList.setOnAction(event -> bookmarkListView());
        copyButton.setOnAction(event -> onCopyClick());
        deleteButton.setOnAction(event -> onDeleteClick());
        onActionSearchField();
        searchListView();
        Platform.runLater(() -> searchField.requestFocus());
    }

    /**
     * Get the current word and show its definition.
     */
    public void LookupWord() {
        if (!giveAlert()) {
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
            if (lookupTask.getValue() != null) {
                definitionField.getChildren().clear();
                setDefinitionField(lookupTask.getValue());
            }
        });
        Thread thread = new Thread(lookupTask);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Show the word in the definition field.
     * Additionally, for format rendering check {@link Word}.
     * @see #tfield
     * @see #pfield
     * @see #definitionField
     * @param word Word to show.
     */
    public void setDefinitionField(Word word) {
        tfield.setText(word.getTarget());
        pfield.setText(word.getPronounce());
        tfield.setPromptText("");
        pfield.setPromptText("");
        definitionField.getChildren().clear();
        definitionField.setTranslateX(10);

        for (String s : word.getDefinition()) {
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
        cancelButton.setVisible(false);
        saveButton.setVisible(false);
        formatInfoButton.setVisible(false);
        validateIcon.setVisible(false);
        tfield.setEditable(false);
        pfield.setEditable(false);
        tfield.setId("word");
        pfield.setId("wordpronounce");
        currentWordObject = word;
    }

    public void setEditField() {
        if (!giveAlert() || mode == Mode.EDIT) {
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
        currentWordObject = dictionary.lookup(currentWord);
        pfield.setEditable(true);
        tfield.setEditable(true);
        tfield.setText(currentWordObject.getTarget());
        pfield.setText(currentWordObject.getPronounce());
        pfield.setId("");
        tfield.setId("");

        speakButton.setVisible(false);
        bookmarkButton.setVisible(false);

        definitionField.getChildren().clear();
        TextArea result = new TextArea();
        LookupTask lookupTask = new LookupTask(currentWord);
        lookupTask.setOnSucceeded(event -> {
            if (lookupTask.getValue() != null) {
                result.setText(lookupTask.getValue().getExplain());
            }
        });
        Thread thread = new Thread(lookupTask);
        thread.setDaemon(true);
        thread.start();

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
     * Add a new word to the dictionary.
     * @see #tfield
     * @see #pfield
     * @see #definitionField
     */
    public void setAddField() {
        if (!giveAlert() || mode == Mode.ADD) {
            return;
        }
        tfield.setText("");
        pfield.setText("");
        tfield.setPromptText("Target");
        pfield.setPromptText("Pronounce");
        definitionField.getChildren().clear();
        definitionField.setTranslateX(10);

        TextArea result = new TextArea();
        result.setPromptText("Definition");
        VBox.setVgrow(result, javafx.scene.layout.Priority.ALWAYS);
        VBox.setMargin(result, new Insets(0, 10, 0, -10));
        definitionField.getChildren().add(result);

        tfield.setEditable(true);
        pfield.setEditable(true);
        tfield.setId("");
        pfield.setId("");

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
        if (!giveAlert() || mode == Mode.REMOVE) {
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
        listOfWord.getItems().remove(currentWord);
        mode = Mode.SEARCH;
        currentWord = null;
        definitionField.getChildren().clear();
        tfield.setText("");
        pfield.setText("");
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

    /**
     * Search the dictionary and update the list view.
     */
    public void onActionSearchField() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Clear the list view if the search field is empty.
            if (newValue == null || newValue.isEmpty()) {
                listOfWord.getItems().clear();
            // Otherwise, search the dictionary and update the list view.
            } else {
                if (!newValue.equals(oldValue)) {
                    // Force refresh the list view.
                    listOfWord.getItems().clear();
                    if (searchTask != null && searchTask.isRunning()) {
                        searchTask.cancel();
                    }
                    searchTask = new SearchTask(newValue);
                    Thread thread = new Thread(searchTask);
                    thread.setDaemon(true);
                    thread.start();
                }
                searchTask.setOnSucceeded(event -> {
                    if (searchTask.getValue() != null) {
                        listOfWord.getItems().setAll(searchTask.getValue());
                        listOfWord.scrollTo(0);
                    }
                });
            }
        });
    }

    /**
     * Update the list view to search view.
     */
    public void searchListView() {
        searchField.clear();
        listOfWord.getItems().clear();
        listOfWord.setCellFactory(param -> new Cell());
        listViewType = ListViewType.SEARCH;
        setStyleListButton("searchList");
        if (!searchHBox.isVisible()) {
            searchHBox.setVisible(true);
            AnchorPane.setTopAnchor(listOfWord, searchHBox.getHeight() + buttonHBox.getHeight());
        }
        AnchorPane.setBottomAnchor(listOfWord, 0.0);
        listviewToolBar.setVisible(false);
        seperateLine.setVisible(false);
    }

    /**
     * Update the list view to history view.
     */
    public void historyListView() {
        searchField.clear();
        listOfWord.getItems().clear();
        if (!dictionary.getHistoryList().getList().isEmpty()) {
            listOfWord.setCellFactory(param -> new HistoryCell());
            listOfWord.getItems().addAll(dictionary.getHistoryList().getList());
        }
        if (searchHBox.isVisible()) {
            searchHBox.setVisible(false);
            AnchorPane.setTopAnchor(listOfWord, buttonHBox.getHeight());
        }
        AnchorPane.setBottomAnchor(listOfWord, listviewToolBar.getHeight());
        listviewToolBar.setVisible(true);
        listviewToolBar.getChildren().remove(moveUpButton);
        listviewToolBar.getChildren().remove(moveDownButton);
        //removeSelectedButton.setDisable(true);
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
        listOfWord.getItems().clear();
        if (!dictionary.getBookmarkList().getList().isEmpty()) {
            listOfWord.setCellFactory(param -> new BookmarkCell());
            listOfWord.getItems().addAll(dictionary.getBookmarkList().getList());
        }
        if (searchHBox.isVisible()) {
            searchHBox.setVisible(false);
            AnchorPane.setTopAnchor(listOfWord, buttonHBox.getHeight());
        }
        AnchorPane.setBottomAnchor(listOfWord, listviewToolBar.getHeight());
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
    public void onCancelEditClick() {
        currentWordObject = dictionary.lookup(currentWord);
        Node node = definitionField.getChildren().get(0);
        if (node instanceof TextArea textArea) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Detected changes.");
            alert.setContentText("Are you sure you want to cancel editing this word?\nAll changes will be lost.");
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (!buttonType.isPresent() || buttonType.get() == ButtonType.CANCEL) {
                return;
            }
            setDefinitionField(currentWordObject);
            mode = Mode.SEARCH;
        }

    }

    /**
     * Save the changes to the dictionary.
     */
    public void onSaveEditClick() {
        Node node = definitionField.getChildren().get(0);
        if (node instanceof TextArea textArea) {
            String newTarget = tfield.getText();
            String newPronounce = pfield.getText();
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
            dictionary.removeWord(currentWord);
            dictionary.editWord(newWord);
            setDefinitionField(newWord);
            mode = Mode.SEARCH;
        } else {
            System.err.println("Error: Node is not TextArea - " + node.getClass());
        }
    }

    public void onAddWordClick() {
        Node node = definitionField.getChildren().get(0);
        if (node instanceof TextArea textArea) {
            String newTarget = tfield.getText();
            String newPronounce = pfield.getText();
            String newExplain = textArea.getText();
            System.out.println(newExplain);
            if (validateIcon.glyphNameProperty().getValue().equals("TIMES_CIRCLE")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Word is null or already exists.");
                alert.showAndWait();
                return;
            }

            Word newWord = new Word(newTarget, newPronounce, newExplain);
            dictionary.addWord(newWord);
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
    public boolean giveAlert() {
        if (mode == Mode.SEARCH) {
            return true;
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
            return false;
        } else if (buttonType.get() == ButtonType.NO) {
            return false;
        }
        return true;
    }

    static class HistoryCell extends ListCell<String> {
        HBox hbox = new HBox();
        Label label = new Label("");
        FontAwesomeIconView iconView = new FontAwesomeIconView();

        public HistoryCell() {
            super();
            iconView.glyphNameProperty().setValue("HISTORY");
            iconView.setId("listbutton-icon");
            iconView.setGlyphSize(15);
            label.setStyle("-fx-text-fill: #191919;");
            hbox.getChildren().addAll(iconView, label);
            HBox.setMargin(iconView, new Insets(5, 5, 0, 0));
            HBox.setMargin(label, new Insets(5, 5, 0, 0));
        }
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);
            setGraphic(null);
            if (item != null && !empty) {
                label.setText(item);
                setGraphic(hbox);
            }
        }
    }

    static class BookmarkCell extends ListCell<String> {
        HBox hbox = new HBox();
        Label label = new Label("");
        FontAwesomeIconView iconView = new FontAwesomeIconView();

        public BookmarkCell() {
            super();
            iconView.glyphNameProperty().setValue("BOOKMARK");
            iconView.setId("bookmarked");
            iconView.setGlyphSize(15);
            hbox.getChildren().addAll(iconView, label);
            HBox.setMargin(iconView, new Insets(5, 5, 0, 0));
            HBox.setMargin(label, new Insets(5, 5, 0, 0));
        }
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);
            setGraphic(null);
            if (item != null && !empty) {
                label.setText(item);
                setGraphic(hbox);
            }
        }
    }

    static class Cell extends ListCell<String> {
        Label label = new Label("");
        public Cell() {
            super();
            label.setStyle("-fx-text-fill: #191919;");
            label.setPadding(new Insets(5, 5, 0, 0));
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);
            setGraphic(null);
            if (item != null && !empty) {
                label.setText(item);
                setGraphic(label);
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