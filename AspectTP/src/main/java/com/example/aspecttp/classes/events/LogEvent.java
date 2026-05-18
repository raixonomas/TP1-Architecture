package com.example.aspecttp.classes.events;

import com.example.aspecttp.types.LogLevel;

import java.util.Map;

public class LogEvent extends TelemetryEvent {

    String message;
    LogLevel level;

    public LogEvent(String className, String methodName, Map<String, Object> metadata, String message, LogLevel level)
    {
        super(className, methodName, metadata);

        this.message =  message;
        this.level = level;
    }
}
