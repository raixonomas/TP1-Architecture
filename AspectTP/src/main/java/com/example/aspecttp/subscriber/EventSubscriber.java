package com.example.aspecttp.subscriber;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public interface EventSubscriber<T> {

    void onEvent(T event);
}