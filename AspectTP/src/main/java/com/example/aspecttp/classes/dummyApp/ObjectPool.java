package com.example.aspecttp.classes.dummyApp;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;

public class ObjectPool<T> {
    private final BlockingQueue<T> pool;
    private final Supplier<T> creator;

    public ObjectPool(int initialSize, Supplier<T> creator) {
        this.pool = new LinkedBlockingQueue<>(initialSize);
        this.creator = creator;

        for (int i = 0; i < initialSize; i++) {
            if (!pool.offer(creator.get())) System.out.println("Couldn't create object in pool.");
        }
    }

    public T get() throws InterruptedException {
        // On utilise pool.take() afin de bloquer jusqu'a ce qu'un objet se libere si le pool est vide.
        return pool.take();
    }

    public void release(T object) {
        pool.offer(object);
    }
}