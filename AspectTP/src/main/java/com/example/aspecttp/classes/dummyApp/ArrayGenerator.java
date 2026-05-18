package com.example.aspecttp.classes.dummyApp;

import java.util.Random;

public class ArrayGenerator {

    int minNbEl;
    int maxNbEl;
    Random rand;

    public ArrayGenerator(int minNbEl, int maxNbEl) {
        this.minNbEl = minNbEl;
        this.maxNbEl = maxNbEl;
        rand = new Random();
    }

    public int[] generateRandomArray(){
        int nbEl = rand.nextInt((maxNbEl - minNbEl) + 1) + minNbEl;

        int[] randomArray = new int[nbEl];

        for(int i = 0; i < randomArray.length; i++){
            randomArray[i] = rand.nextInt((maxNbEl - minNbEl) + 1);
        }

        return randomArray;
    }
}
