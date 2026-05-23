package com.example.aspecttp.subscriber;

import com.example.aspecttp.classes.events.MetricEvent;
import com.example.aspecttp.types.TelemetryType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class MetricSubscriber implements EventSubscriber<MetricEvent> {

    private final ObservableList<TelemetryViewItem> sink;

    public MetricSubscriber(ObservableList<TelemetryViewItem> sink) {
        this.sink = sink;
    }

    public void onEvent(MetricEvent event) {
        TelemetryViewItem newItem = new TelemetryViewItem();

        newItem.type = TelemetryType.Metric;
        newItem.time = event.timestamp;
        newItem.message = event.className + "." + event.methodName;

        javafx.application.Platform.runLater(() ->
                sink.add(newItem)
        );
    }
}
