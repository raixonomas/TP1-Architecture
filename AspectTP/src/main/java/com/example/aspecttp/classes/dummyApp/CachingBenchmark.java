package com.example.aspecttp.classes.dummyApp;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.example.aspecttp.classes.context.Logging;

public class CachingBenchmark {

    // Simple cache implementation
    private static class SimpleCache<K, V> {
        private final ConcurrentHashMap<K, V> cache = new ConcurrentHashMap<>();

        public V computeIfAbsent(K key, java.util.function.Function<K, V> function) {
            return cache.computeIfAbsent(key, function);
        }

        public V get(K key) {
            return cache.get(key);
        }

        public void clear() {
            cache.clear();
        }
    }

    public static void startCachingBenchmark() {

        // SCENARIO 1: HIGH COMPUTATION COST + HIGH REUSE RATE (Cache favorable)
        try {
            Logging.info("Executing benchmark: High cost, high reuse, NO cache");
            benchmarkHighCostHighReuseNoCache();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            Logging.info("Executing benchmark: High cost, high reuse, WITH cache");
            benchmarkHighCostHighReuseWithCache();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // SCENARIO 2: HIGH COMPUTATION COST + LOW REUSE RATE (Cache less favorable)
        try {
            Logging.info("Executing benchmark: High cost, low reuse, NO cache");
            benchmarkHighCostLowReuseNoCache();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            Logging.info("Executing benchmark: High cost, low reuse, WITH cache");
            benchmarkHighCostLowReuseWithCache();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // SCENARIO 3: LOW COMPUTATION COST + HIGH REUSE RATE (Cache overhead might matter)
        try {
            Logging.info("Executing benchmark: Low cost, high reuse, NO cache");
            benchmarkLowCostHighReuseNoCache();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            Logging.info("Executing benchmark: Low cost, high reuse, WITH cache");
            benchmarkLowCostHighReuseWithCache();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // SCENARIO 4: LOW COMPUTATION COST + LOW REUSE RATE (Cache unfavorable)
        try {
            Logging.info("Executing benchmark: Low cost, low reuse, NO cache");
            benchmarkLowCostLowReuseNoCache();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            Logging.info("Executing benchmark: Low cost, low reuse, WITH cache");
            benchmarkLowCostLowReuseWithCache();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Logging.info("All caching benchmarks completed");
    }

    // ========== CALCULATION METHODS ==========

    // Expensive calculation (high cost)
    private static long expensiveCalculation(int n) {
        long result = 0;
        for (int i = 0; i < 100000; i++) {
            result += (n * i) % 123;
        }
        return result;
    }

    // Cheap calculation (low cost)
    private static long cheapCalculation(int n) {
        return (long) (n * n) + n;
    }

    // ========== SCENARIO 1: HIGH COST + HIGH REUSE ==========

    public static void benchmarkHighCostHighReuseNoCache() throws InterruptedException {
        int nbThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(nbThreads);

        // High reuse: 100000 threads calling with only 10 different parameters
        int reuseFactor = 100000 / 10;
        for (int i = 0; i < 100000; i++) {
            int param = i % 10; // Only 10 different values
            executor.submit(() -> {
                long result = expensiveCalculation(param);
            });
        }

        executor.shutdown();
        boolean success = executor.awaitTermination(1, TimeUnit.HOURS);
    }

    public static void benchmarkHighCostHighReuseWithCache() throws InterruptedException {
        int nbThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(nbThreads);

        SimpleCache<Integer, Long> cache = new SimpleCache<>();

        for (int i = 0; i < 100000; i++) {
            int param = i % 10; // Only 10 different values
            executor.submit(() -> {
                long result = cache.computeIfAbsent(param, CachingBenchmark::expensiveCalculation);
            });
        }

        executor.shutdown();
        boolean success = executor.awaitTermination(1, TimeUnit.HOURS);
    }

    // ========== SCENARIO 2: HIGH COST + LOW REUSE ==========

    public static void benchmarkHighCostLowReuseNoCache() throws InterruptedException {
        int nbThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(nbThreads);

        // Low reuse: each thread calls with a different parameter
        for (int i = 0; i < 100000; i++) {
            int param = i; // Each call uses a different value
            executor.submit(() -> {
                long result = expensiveCalculation(param);
            });
        }

        executor.shutdown();
        boolean success = executor.awaitTermination(1, TimeUnit.HOURS);
    }

    public static void benchmarkHighCostLowReuseWithCache() throws InterruptedException {
        int nbThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(nbThreads);

        SimpleCache<Integer, Long> cache = new SimpleCache<>();

        for (int i = 0; i < 100000; i++) {
            int param = i; // Each call uses a different value
            executor.submit(() -> {
                long result = cache.computeIfAbsent(param, CachingBenchmark::expensiveCalculation);
            });
        }

        executor.shutdown();
        boolean success = executor.awaitTermination(1, TimeUnit.HOURS);
    }

    // ========== SCENARIO 3: LOW COST + HIGH REUSE ==========

    public static void benchmarkLowCostHighReuseNoCache() throws InterruptedException {
        int nbThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(nbThreads);

        // High reuse: 100000 threads calling with only 10 different parameters
        for (int i = 0; i < 100000; i++) {
            int param = i % 10; // Only 10 different values
            executor.submit(() -> {
                long result = cheapCalculation(param);
            });
        }

        executor.shutdown();
        boolean success = executor.awaitTermination(1, TimeUnit.HOURS);
    }

    public static void benchmarkLowCostHighReuseWithCache() throws InterruptedException {
        int nbThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(nbThreads);

        SimpleCache<Integer, Long> cache = new SimpleCache<>();

        for (int i = 0; i < 100000; i++) {
            int param = i % 10; // Only 10 different values
            executor.submit(() -> {
                long result = cache.computeIfAbsent(param, CachingBenchmark::cheapCalculation);
            });
        }

        executor.shutdown();
        boolean success = executor.awaitTermination(1, TimeUnit.HOURS);
    }

    // ========== SCENARIO 4: LOW COST + LOW REUSE ==========

    public static void benchmarkLowCostLowReuseNoCache() throws InterruptedException {
        int nbThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(nbThreads);

        // Low reuse: each thread calls with a different parameter
        for (int i = 0; i < 100000; i++) {
            int param = i; // Each call uses a different value
            executor.submit(() -> {
                long result = cheapCalculation(param);
            });
        }

        executor.shutdown();
        boolean success = executor.awaitTermination(1, TimeUnit.HOURS);
    }

    public static void benchmarkLowCostLowReuseWithCache() throws InterruptedException {
        int nbThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(nbThreads);

        SimpleCache<Integer, Long> cache = new SimpleCache<>();

        for (int i = 0; i < 100000; i++) {
            int param = i; // Each call uses a different value
            executor.submit(() -> {
                long result = cache.computeIfAbsent(param, CachingBenchmark::cheapCalculation);
            });
        }

        executor.shutdown();
        boolean success = executor.awaitTermination(1, TimeUnit.HOURS);
    }
}
