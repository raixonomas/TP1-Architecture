package com.example.aspecttp.subscriber;

import com.example.aspecttp.types.TelemetryType;

import java.time.LocalDateTime;

public class TelemetryViewItem {
    public TelemetryType type = TelemetryType.Metric;
    public LocalDateTime time;
    public String message;

    public TelemetryType getType() {
        return type;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }
}
