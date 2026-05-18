package com.example.aspecttp.classes.events;

import java.util.Map;

public class ErrorEvent extends TelemetryEvent{
    String errorType;
    String message;

    public ErrorEvent(String className, String methodName, Map<String, Object> metadata, String errorType, String message)
    {
        super(className, methodName, metadata);

        this.message =  message;
        this.errorType = errorType;
    }
}
