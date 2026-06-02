package com.example.aspecttp.classes.dummyApp;

import com.example.aspecttp.classes.context.Logging;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PoolBenchmark {
    public static void startPoolBenchmark() {

        // OBJETS LEGERS
        forceCleanup();

        // Haute fréquence de création/destruction (moins de travail par objet) avec des objets légers, sans pooling.
        try {
            Logging.info("Executing benchmark : high frequency, light object, no pooling");
            benchmarkHighFrequencyLightObject();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        forceCleanup();


        // Haute fréquence de création/destruction (moins de travail par objet) avec des objets légers, avec pooling.
        try {
            Logging.info("Executing benchmark : high frequency, light object, pooling");
            benchmarkHighFrequencyLightObjectWithPool();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        forceCleanup();


        // Basse fréquence de création/destruction (plus de travail par objet) avec des objets légers, sans pooling.
        try {
            Logging.info("Executing benchmark : low frequency, light object, no pooling");
            benchmarkLowFrequencyLightObject();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        forceCleanup();


        // Basse fréquence de création/destruction (plus de travail par objet) avec des objets légers, avec pooling.
        try {
            Logging.info("Executing benchmark : low frequency, light object, pooling");
            benchmarkLowFrequencyLightObjectWithPool();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        forceCleanup();


        // OBJETS LOURDS

        // Haute fréquence de création/destruction (moins de travail par objet) avec des objets lourds, sans pooling.
        try {
            Logging.info("Executing benchmark : high frequency, heavy object, no pooling");
            benchmarkHighFrequencyHeavyObject();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        forceCleanup();


        // Haute fréquence de création/destruction (moins de travail par objet) avec des objets lourds, avec pooling.
        try {
            Logging.info("Executing benchmark : high frequency, heavy object, pooling");
            benchmarkHighFrequencyHeavyObjectWithPool();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        forceCleanup();


        // Basse fréquence de création/destruction (plus de travail par objet) avec des objets lourds, sans pooling.
        try {
            Logging.info("Executing benchmark : low frequency, heavy object, no pooling");
            benchmarkLowFrequencyHeavyObject();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        forceCleanup();


        // Basse fréquence de création/destruction (plus de travail par objet) avec des objets lourds, avec pooling.
        try {
            Logging.info("Executing benchmark : low frequency, heavy object, pooling");
            benchmarkLowFrequencyHeavyObjectWithPool();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        forceCleanup();


        Logging.info("All benchmarks completed");
    }

    public static void benchmarkHighFrequencyLightObject() throws InterruptedException {
        int nbThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(nbThreads);

        for (int i = 0; i < 100000; i++) {
            executor.submit(() -> {
                LightObject obj = new LightObject();
                double x = obj.work();
            });
        }

        executor.shutdown();
        boolean success = executor.awaitTermination(1, TimeUnit.HOURS);
    }

    public static void benchmarkHighFrequencyLightObjectWithPool() throws InterruptedException {
        int nbThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(nbThreads);

        // On initialise le pool avec assez d'objets pour alimenter les threads en parallèle
        ObjectPool<LightObject> pool = new ObjectPool<>(nbThreads, LightObject::new);

        for (int i = 0; i < 100000; i++) {
            executor.submit(() -> {
                LightObject obj = null;
                try {
                    obj = pool.get();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                double x = obj.work();
                pool.release(obj);
            });
        }

        executor.shutdown();
        boolean success = executor.awaitTermination(1, TimeUnit.HOURS);

    }

    public static void benchmarkLowFrequencyLightObject() throws InterruptedException {
        int nbThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(nbThreads);

        for (int i = 0; i < 100000; i++) {
            executor.submit(() -> {
                LightObject obj = new LightObject();
                for (int j = 0; j < 1000; j++) {
                    double x = obj.work();
                }
            });
        }

        executor.shutdown();
        boolean success = executor.awaitTermination(1, TimeUnit.HOURS);
    }

    public static void benchmarkLowFrequencyLightObjectWithPool() throws InterruptedException {
        int nbThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(nbThreads);

        // On initialise le pool avec assez d'objets pour alimenter les threads en parallèle
        ObjectPool<LightObject> pool = new ObjectPool<>(nbThreads, LightObject::new);

        for (int i = 0; i < 100000; i++) {
            executor.submit(() -> {
                LightObject obj = null;
                try {
                    obj = pool.get();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                for (int j = 0; j < 1000; j++) {
                    double x = obj.work();
                }
                pool.release(obj);
            });
        }

        executor.shutdown();
        boolean success = executor.awaitTermination(1, TimeUnit.HOURS);
    }

    public static void benchmarkHighFrequencyHeavyObject() throws InterruptedException {
        int nbThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(nbThreads);

        for (int i = 0; i < 100000; i++) {
            executor.submit(() -> {
                HeavyObject obj = new HeavyObject();
                double x = obj.work();
            });
        }

        executor.shutdown();
        boolean success = executor.awaitTermination(1, TimeUnit.HOURS);
    }

    public static void benchmarkHighFrequencyHeavyObjectWithPool() throws InterruptedException {
        int nbThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(nbThreads);

        // On initialise le pool avec assez d'objets pour alimenter les threads en parallèle
        ObjectPool<HeavyObject> pool = new ObjectPool<>(nbThreads, HeavyObject::new);

        for (int i = 0; i < 100000; i++) {
            executor.submit(() -> {
                HeavyObject obj = null;
                try {
                    obj = pool.get();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                double x = obj.work();
                pool.release(obj);
            });
        }

        executor.shutdown();
        boolean success = executor.awaitTermination(1, TimeUnit.HOURS);
    }

    public static void benchmarkLowFrequencyHeavyObject() throws InterruptedException {
        int nbThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(nbThreads);

        for (int i = 0; i < 100000; i++) {
            executor.submit(() -> {
                HeavyObject obj = new HeavyObject();
                for (int j = 0; j < 1000; j++) {
                    double x = obj.work();
                }
            });
        }

        executor.shutdown();
        boolean success = executor.awaitTermination(1, TimeUnit.HOURS);
    }

    public static void benchmarkLowFrequencyHeavyObjectWithPool() throws InterruptedException {
        int nbThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(nbThreads);

        // On initialise le pool avec assez d'objets pour alimenter les threads en parallèle
        ObjectPool<HeavyObject> pool = new ObjectPool<>(nbThreads, HeavyObject::new);

        for (int i = 0; i < 100000; i++) {
            executor.submit(() -> {
                HeavyObject obj = null;
                try {
                    obj = pool.get();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                for (int j = 0; j < 1000; j++) {
                    double x = obj.work();
                }
                pool.release(obj);
            });
        }

        executor.shutdown();
        boolean success = executor.awaitTermination(1, TimeUnit.HOURS);
    }

    private static void forceCleanup() {
        try {
            // Suggest explicit garbage collection to the JVM
            System.gc();
            // Suggest execution of pending finalizers
            System.runFinalization();

            // Give the GC background threads a small window to complete their work
            Thread.sleep(300);

            System.gc(); // Double-check pass
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}