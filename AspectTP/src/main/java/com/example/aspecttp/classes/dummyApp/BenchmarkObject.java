package com.example.aspecttp.classes.dummyApp;

public class BenchmarkObject {

    public double work() {
        double x = 0;
        for (int i = 0; i < 100; i++) {
            x += Math.sqrt(i);
        }

        return x;
    }
}
