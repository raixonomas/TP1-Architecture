package com.example.aspecttp;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class AppController {
    @FXML
    private TextField searchField;
    @FXML
    private TableView logTable;

    @FXML
    protected void handleClearLogs() {
        logTable.getItems().clear();
    }
}
