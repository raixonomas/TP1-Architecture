package com.example.aspecttp.classes.context;

import com.example.aspecttp.classes.buses.EventBus;

import java.util.HashMap;
import java.util.Map;

public class TelemetryContext {

    private static final Map<Class<?>, Object> buses = new HashMap<>();

    public static <T> void registerBus(Class<T> type, EventBus<T> bus) {
        buses.put(type, bus);
    }

    @SuppressWarnings("unchecked")
    public static <T> EventBus<T> getBus(Class<T> type) {
        return (EventBus<T>) buses.get(type);
    }
}
