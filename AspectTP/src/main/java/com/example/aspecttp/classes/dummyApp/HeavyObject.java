package com.example.aspecttp.classes.dummyApp;

public class HeavyObject extends BenchmarkObject {

    private int[] data;

    public HeavyObject() {
        data = new int[1024 * 1024];
    }
}
