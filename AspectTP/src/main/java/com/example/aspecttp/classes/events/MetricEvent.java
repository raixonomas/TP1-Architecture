package com.example.aspecttp.classes.events;

import java.util.Map;

public class MetricEvent extends TelemetryEvent {

    public String metricName;
    public double value;

    public MetricEvent(String className, String methodName, Map<String, Object> metadata, String metricName, double value) {
        super(className, methodName, metadata);

        this.metricName = metricName;
        this.value = value;
    }
}
