package com.example.aspecttp.classes.events;

import java.time.LocalDateTime;
import java.util.Map;

import static java.util.UUID.*;

public abstract class TelemetryEvent {
    String id;
    LocalDateTime timestamp;
    String className;
    String methodName;
    Map<String, Object> metadata;

    public TelemetryEvent(String className, String methodName, Map<String, Object> metadata){
        this.id = randomUUID().toString();
        this.timestamp = LocalDateTime.now();
        this.className = className;
        this.methodName = methodName;
        this.metadata = metadata;
    }
}
