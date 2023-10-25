package com.controller;

import com.dictionary.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
public class DictionaryController implements Initializable {

    @FXML
    private ListView<String> listOfWord;

    @FXML
    private TextField search_field;

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
                //System.out.println(target);
            }

            /*while (Dictionary.words.iterator().hasNext()) {
                wordData.add(new String(Dictionary.words.iterator().next().getWord_target()));
            }*/
            listOfWord.setItems(wordData);

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

            // Set fixed cell size avoid listview from resizing and warning.
            listOfWord.setFixedCellSize(30);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
