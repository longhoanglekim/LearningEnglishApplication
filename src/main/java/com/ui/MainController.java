package com.ui;

import com.dictionary.Database;
import com.dictionary.Dictionary;
import com.dictionary.DictionaryCommandline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * Controller for main view.
 * Currently, this controller is for testing and can be refactored later.
 */
public class MainController implements Initializable {

    @FXML
    private ListView<String> wordList;

    @FXML
    private TextField searchBox;

    private DictionaryCommandline dictionaryCommandline = new DictionaryCommandline();

    private final ObservableList<String> wordData = FXCollections.observableArrayList();

    /**
     * Initialize the controller, updating the search view list.
     * @param url URL.
     * @param resourceBundle ResourceBundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Database database = new Database();
        Connection connectDataBase = database.getDatabaseConnection();
        //database.getDataFromDatabase(); currently bugged, will be fixed later.
        String query = "SELECT word FROM tbl_edict";

        try {
            Statement statement = connectDataBase.createStatement();
            ResultSet queryOutput = statement.executeQuery(query);
            while (queryOutput.next()) {
                String target = queryOutput.getString("word");
                wordData.add(target);
            }

            /*while (Dictionary.words.iterator().hasNext()) {
                wordData.add(new String(Dictionary.words.iterator().next().getWord_target()));
            }*/
            wordList.setItems(wordData);

            // Filter list view with search box data.
            FilteredList<String> filteredData = new FilteredList<>(wordData, s -> true);
            searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(s -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (s.toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });

            // Set the filter list to be the list view items.
            wordList.setItems(filteredData);

            // Set fixed cell size avoid listview from resizing and warning.
            wordList.setFixedCellSize(30);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
