package com.example.aspecttp.classes.context;

import com.example.aspecttp.classes.buses.EventBus;
import com.example.aspecttp.classes.events.LogEvent;
import com.example.aspecttp.types.LogLevel;

import java.util.Map;

public class Logging {

    private static StackTraceElement getCaller() {
        return Thread.currentThread().getStackTrace()[3];
    }

    public static void info(String message) {
        log(LogLevel.Information, message);
    }

    public static void debug(String message) {
        log(LogLevel.Debug, message);
    }

    public static void warn(String message) {
        log(LogLevel.Warning, message);
    }

    public static void error(String message) {
        log(LogLevel.Error, message);
    }


    public static void log(LogLevel level, String message) {

        StackTraceElement caller = getCaller();

        EventBus<LogEvent> bus =
                TelemetryContext.getBus(LogEvent.class);

        LogEvent event = new LogEvent(
                caller.getClassName(),
                caller.getMethodName(),
                Map.of(),
                message,
                level
        );

        bus.publish(event);
    }
}