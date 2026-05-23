package com.example.aspecttp.subscriber;

import com.example.aspecttp.classes.events.LogEvent;
import com.example.aspecttp.classes.events.MetricEvent;
import com.example.aspecttp.types.TelemetryType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LogSubscriber implements EventSubscriber<LogEvent> {

    private final ObservableList<TelemetryViewItem> sink;

    public LogSubscriber(ObservableList<TelemetryViewItem> sink) {
        this.sink = sink;
    }

    public void onEvent(LogEvent event) {
        TelemetryViewItem newItem = new TelemetryViewItem();

        newItem.type = TelemetryType.Log;
        newItem.time = event.timestamp;
        newItem.message = event.message;

        javafx.application.Platform.runLater(() ->
                sink.add(newItem)
        );
    }
}
