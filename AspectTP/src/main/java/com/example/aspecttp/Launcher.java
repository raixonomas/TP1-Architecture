package com.example.aspecttp;

import com.example.aspecttp.classes.buses.EventBus;
import com.example.aspecttp.classes.context.TelemetryContext;
import com.example.aspecttp.classes.events.ErrorEvent;
import com.example.aspecttp.classes.events.LogEvent;
import com.example.aspecttp.classes.events.MetricEvent;
import javafx.application.Application;

public class Launcher {
    public static void main(String[] args) {
        var metricBus = new EventBus<MetricEvent>();
        TelemetryContext.registerBus(MetricEvent.class, metricBus);

        var errorBus = new EventBus<ErrorEvent>();
        TelemetryContext.registerBus(ErrorEvent.class, errorBus);

        var logBus = new EventBus<LogEvent>();
        TelemetryContext.registerBus(LogEvent.class, logBus);

        AppPage.main(args);
    }
}
