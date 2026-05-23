package com.example.aspecttp.subscriber;

import com.example.aspecttp.classes.events.ErrorEvent;
import com.example.aspecttp.classes.events.MetricEvent;
import com.example.aspecttp.types.TelemetryType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;

public class ErrorSubscriber implements EventSubscriber<ErrorEvent> {

    private final ObservableList<TelemetryViewItem> sink;

    public ErrorSubscriber(ObservableList<TelemetryViewItem> sink) {
        this.sink = sink;
    }

    public void onEvent(ErrorEvent event) {
        TelemetryViewItem item = new TelemetryViewItem();
        item.type = TelemetryType.Error;
        item.time = LocalDateTime.now();

        item.message =
                "❌ " + event.errorType +
                        " in " + event.className + "." + event.methodName +
                        " → " + event.message;

        javafx.application.Platform.runLater(() ->
                sink.add(item)
        );
    }
}
