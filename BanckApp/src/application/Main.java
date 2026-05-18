package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Main extends Application {

	@Override
    public void start(Stage stage) {

        BankAccount account = new BankAccount();
        BankingView view = new BankingView();

        // connect UI to aspect
        BankAccountAspect aspect = BankAccountAspect.aspectOf();
        aspect.addSubscriber(view);

        // ---- LEFT SIDE (operations) ----
        TextField amountField = new TextField();
        amountField.setPromptText("Amount");

        Button depositBtn = new Button("Deposit");
        Button withdrawBtn = new Button("Withdraw");

        depositBtn.setOnAction(e -> {
            double amount = Double.parseDouble(amountField.getText());
            account.deposit(amount);
        });

        withdrawBtn.setOnAction(e -> {
            double amount = Double.parseDouble(amountField.getText());
            account.withdraw(amount);
        });

        VBox left = new VBox(10,
                new Label("Operations"),
                amountField,
                depositBtn,
                withdrawBtn,
                view.getBalanceLabel()
        );

        // ---- RIGHT SIDE (history) ----
        VBox right = view.createHistoryPanel();

        SplitPane root = new SplitPane(left, right);

        stage.setTitle("AspectJ Banking System");
        stage.setScene(new Scene(root, 800, 400));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
