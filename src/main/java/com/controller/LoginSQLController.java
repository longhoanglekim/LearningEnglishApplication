package com.controller;

import com.dictionary.Database;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static com.ui.Model.setDictionary;

public class LoginSQLController implements Initializable {

    @FXML
    private TextField userField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField databaseField;

    @FXML
    private Button cancelButton;

    @FXML
    private Button connectButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cancelButton.setOnAction(actionEvent -> cancel());
        connectButton.setOnAction(actionEvent -> connect());
    }

    public void connect() {
        Database database = new Database();
        try {
            database.getDatabaseConnection(userField.getText(), passwordField.getText(), databaseField.getText());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cannot connect to database.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        if (!database.initialize()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cannot initialize database.");
            alert.setContentText("Cannot initialize database.");
            alert.showAndWait();
        } else {
            setDictionary(database);
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        }
    }

    public void cancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
