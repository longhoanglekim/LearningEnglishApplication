package com.controller;

import com.dictionary.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Controller for main view.
 * Currently, this controller is for testing and can be refactored later.
 */
public class DictionaryController implements Initializable {

    @FXML
    private ListView<String> listOfWord;

    @FXML
    private TextField search_field;

    @FXML
    TextArea definitionField;

    private final ObservableList<String> wordData = FXCollections.observableArrayList();
    private final ObservableList<String> meaningData = FXCollections.observableArrayList();
    HashMap<String, String> map = new HashMap<String, String>();

    /**
     * Initialize the controller, updating the search view list.
     *
     * @param url            URL.
     * @param resourceBundle ResourceBundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Database database = new Database();
        Connection connectDataBase = database.getDatabaseConnection();
        //database.getDataFromDatabase(); currently bugged, will be fixed later.
        String query_word = "SELECT word FROM tbl_edict";
        String detail_word = "SELECT detail FROM tbl_edict";
        try {
            loadDatabase(connectDataBase, query_word, detail_word);



            // Set fixed cell size avoid listview from resizing and warning.
            listOfWord.setFixedCellSize(30);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadDatabase(Connection connectDataBase, String query_word, String query_detail) throws Exception{
        Statement statement = connectDataBase.createStatement();
        ResultSet wordOutput = statement.executeQuery(query_word);
        while (wordOutput.next()) {
            String target = wordOutput.getString("word");
            wordData.add(target);
            //System.out.println(target);
        }

            /*while (Dictionary.words.iterator().hasNext()) {
                wordData.add(new String(Dictionary.words.iterator().next().getWord_target()));
            }*/
        listOfWord.setItems(wordData);
        ResultSet detailOutput = statement.executeQuery(query_detail);
        int i = 0;
        while (detailOutput.next()) {
            String target = detailOutput.getString("detail");
            meaningData.add(target);
            map.put(wordData.get(i), meaningData.get(i));
            i++;
        }
        statement = connectDataBase.createStatement();

        filterListOfWord();
    }
    public void chooseWordClikced() {
        definitionField.setText(map.get(listOfWord.getSelectionModel().getSelectedItem()));
    }

    public void filterListOfWord() {
        // Filter list view with search box data.
        FilteredList<String> filteredData = new FilteredList<>(wordData, s -> true);
        search_field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                listOfWord.scrollTo(0);
            }
            filteredData.setPredicate(s -> {
                if (newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return s.toLowerCase().startsWith(lowerCaseFilter);
            });
        });
        // Set the filter list to be the list view items.
        listOfWord.setItems(filteredData);
    }
}