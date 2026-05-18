package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

public class BankingView implements AccountSubscriber {

    private final ObservableList<String> history = FXCollections.observableArrayList();
    private final Label balanceLabel = new Label("Balance: 0.0");

    public VBox createHistoryPanel() {
        return new VBox(new Label("History"), new ListView<>(history));
    }

    public Label getBalanceLabel() {
        return balanceLabel;
    }

    @Override
    public void onEvent(String type, double amount, double balance) {

        history.add(
            type +
            " | amount: " + amount +
            " | balance: " + balance
        );

        balanceLabel.setText("Balance: " + balance);
    }
}