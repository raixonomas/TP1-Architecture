package com.example.aspecttp;

import java.time.LocalDateTime;

import com.example.aspecttp.classes.buses.EventBus;
import com.example.aspecttp.classes.context.TelemetryContext;
import com.example.aspecttp.classes.dummyApp.CachingBenchmark;
import com.example.aspecttp.classes.dummyApp.PoolBenchmark;
import com.example.aspecttp.classes.events.ErrorEvent;
import com.example.aspecttp.classes.events.LogEvent;
import com.example.aspecttp.classes.events.MetricEvent;
import com.example.aspecttp.subscriber.ErrorSubscriber;
import com.example.aspecttp.subscriber.LogSubscriber;
import com.example.aspecttp.subscriber.MetricSubscriber;
import com.example.aspecttp.subscriber.TelemetryViewItem;
import com.example.aspecttp.types.TelemetryType;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class AppController {

    private final ObservableList<TelemetryViewItem> allItems =
            FXCollections.observableArrayList();

    MetricSubscriber metricSubscriber = new MetricSubscriber(allItems);
    LogSubscriber logSubscriber = new LogSubscriber(allItems);
    ErrorSubscriber errorSubscriber = new ErrorSubscriber(allItems);

    @FXML
    private TextField searchField;

    @FXML
    private TableView<TelemetryViewItem> logTable; // You can change <?> to your specific Log object type later

    @FXML
    private TableColumn<TelemetryViewItem, TelemetryType> typeColumn;

    @FXML
    private TableColumn<TelemetryViewItem, LocalDateTime> timeColumn;

    @FXML
    private TableColumn<TelemetryViewItem, String> messageColumn;

    @FXML
    public void initialize() {

        System.out.println("[Controller] View components initialized successfully!");

        typeColumn.setCellValueFactory(data ->
                new javafx.beans.property.ReadOnlyObjectWrapper<>(data.getValue().getType())
        );

        timeColumn.setCellValueFactory(data ->
                new javafx.beans.property.ReadOnlyObjectWrapper<>(data.getValue().getTime())
        );

        messageColumn.setCellValueFactory(data ->
                new javafx.beans.property.ReadOnlyStringWrapper(data.getValue().getMessage())
        );

        EventBus<MetricEvent> busMetric = TelemetryContext.getBus(MetricEvent.class);
        busMetric.subscribe(metricSubscriber);

        EventBus<ErrorEvent> busError = TelemetryContext.getBus(ErrorEvent.class);
        busError.subscribe(errorSubscriber);

        EventBus<LogEvent> busLog = TelemetryContext.getBus(LogEvent.class);
        busLog.subscribe(logSubscriber);

        logTable.setItems(allItems);
    }

    @FXML
    private void handleClearLogs() {
        logTable.getItems().clear();
        allItems.clear();
    }

    @FXML
    private void executeRandomOperation() throws InterruptedException {
        CachingBenchmark.startCachingBenchmark();
        PoolBenchmark.startPoolBenchmark();

    }

    @FXML
    private void executeCachingBenchmark() throws InterruptedException {
        CachingBenchmark.startCachingBenchmark();
    }
}
