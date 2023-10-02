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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Hashtable;
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

    @FXML
    TextArea meaning;

    private DictionaryCommandline dictionaryCommandline = new DictionaryCommandline();
    HashMap<String, String> map = new HashMap<String, String>();
    private final ObservableList<String> wordData = FXCollections.observableArrayList();
    private final ObservableList<String> meaningData = FXCollections.observableArrayList();
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
        String queryWord = "SELECT word FROM tbl_edict";
        String queryDetail = "SELECT detail FROM tbl_edict";
        try {
           loadDatabase(connectDataBase,queryWord,queryDetail);

            // Filter list view with search box data.

//            searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
//                filteredData.setPredicate(s -> {
//                    if (newValue == null || newValue.isEmpty()) {
//                        return true;
//                    }
//                    String lowerCaseFilter = newValue.toLowerCase();
//                    if (s.toLowerCase().contains(lowerCaseFilter)) {
//                        return true;
//                    }
//                    return false;
//                });
//            });

            // Set the filter list to be the list view items.

            // Set fixed cell size avoid listview from resizing and warning.
            wordList.setFixedCellSize(30);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Show the meaning of the selectee Word in WordList box.
     */
    public void onWordListClicked(){
        meaning.setText(map.get(wordList.getSelectionModel().getSelectedItem()));
   }

    /**
     * load data from database and put into wordList,meaninglist and set them into data map,
     * @param connectDataBase Connection.
     * @param queryWord String.
     * @param queryDetail String.
     */
    public void loadDatabase(Connection connectDataBase, String queryWord, String queryDetail) throws Exception {
        Statement statementWord = connectDataBase.createStatement();
        ResultSet queryOutput = statementWord.executeQuery(queryWord);
        while (queryOutput.next()) {
            String target = queryOutput.getString("word");
            wordData.add(target);
        }

        Statement statementDetail = connectDataBase.createStatement();
        ResultSet queryOutput2 = statementDetail.executeQuery(queryDetail);
        int i = 0;
        while (queryOutput2.next()) {
            String target2 = queryOutput2.getString("detail");
            meaningData.add(target2);
            map.put(wordData.get(i), meaningData.get(i));
            i++;
        }
        wordList.setItems(wordData);

    }
}
