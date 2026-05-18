package com.example.aspecttp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class AppController {
    @FXML
    private TextField searchField;

    @FXML
    private TableView<?> logTable; // You can change <?> to your specific Log object type later

    @FXML
    private TableColumn<?, ?> typeColumn;

    @FXML
    private TableColumn<?, ?> timeColumn;

    @FXML
    private TableColumn<?, ?> messageColumn;

    @FXML
    public void initialize() {
        System.out.println("[Controller] View components initialized successfully!");
    }

    @FXML
    private void handleClearLogs() {
        System.out.println("Clear logs button clicked!");
        // Your logic to clear logs will go here
    }
}
