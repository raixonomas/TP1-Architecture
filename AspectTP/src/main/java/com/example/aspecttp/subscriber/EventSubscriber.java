package com.example.aspecttp.subscriber;

public interface EventSubscriber<T> {

    void onEvent(T event);
}