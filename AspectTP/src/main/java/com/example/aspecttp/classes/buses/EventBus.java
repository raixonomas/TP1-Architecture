package com.example.aspecttp.classes.buses;

import com.example.aspecttp.subscriber.EventSubscriber;

import java.util.ArrayList;
import java.util.List;

public class EventBus<T> {

    private final List<EventSubscriber<T>> subscribers =
            new ArrayList<>();

    public void subscribe(EventSubscriber<T> sub) {
        subscribers.add(sub);
    }

    public void publish(T event) {
        for (EventSubscriber<T> s : subscribers) {
            s.onEvent(event);
        }
    }
}
