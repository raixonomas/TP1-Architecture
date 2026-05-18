module com.example.aspecttp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.aspecttp to javafx.fxml;
    exports com.example.aspecttp;
}