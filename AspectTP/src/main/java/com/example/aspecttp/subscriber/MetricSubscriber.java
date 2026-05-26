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

        StringBuilder sb = new StringBuilder();

        sb.append(event.className)
                .append(".")
                .append(event.methodName)
                .append("\n\n");

        // =========================
        // PERFORMANCE
        // =========================

        sb.append("📊 Performance\n");
        sb.append("  Execution Time : ")
                .append(event.value)
                .append(" ms\n");

        if (event.metadata.containsKey("operations_per_second")) {
            sb.append("  Throughput     : ")
                    .append(event.metadata.get("operations_per_second"))
                    .append(" ops/sec\n");
        }

        if (event.metadata.containsKey("process_cpu_percent")) {
            sb.append("  CPU Usage      : ")
                    .append(String.format("%.2f", event.metadata.get("process_cpu_percent")))
                    .append(" %\n");
        }

        sb.append("\n");

        // =========================
        // MEMORY
        // =========================

        sb.append("🧠 Memory\n");

        append(sb, "Used Before", event.metadata.get("memory_before_kb"), "KB");
        append(sb, "Used After", event.metadata.get("memory_after_kb"), "KB");
        append(sb, "Allocated", event.metadata.get("allocated_memory_kb"), "KB");
        append(sb, "Heap Used", event.metadata.get("heap_used_mb"), "MB");
        append(sb, "Heap Max", event.metadata.get("heap_max_mb"), "MB");

        sb.append("\n");

        // =========================
        // GC
        // =========================

        sb.append("♻ Garbage Collector\n");
        append(sb, "Collections", event.metadata.get("gc_collections"), "");
        append(sb, "GC Time", event.metadata.get("gc_time_ms"), "ms");

        sb.append("\n");

        // =========================
        // THREADS
        // =========================

        sb.append("🧵 Threads\n");
        append(sb, "Before", event.metadata.get("threads_before"), "");
        append(sb, "After", event.metadata.get("threads_after"), "");

        newItem.message = sb.toString();

        Platform.runLater(() -> sink.add(newItem));
    }

    private void append(StringBuilder sb, String label, Object value, String unit) {
        if (value == null) return;

        sb.append("  ")
                .append(label)
                .append(" : ")
                .append(value);

        if (unit != null && !unit.isEmpty()) {
            sb.append(" ").append(unit);
        }
        sb.append("\n");

    }
}
