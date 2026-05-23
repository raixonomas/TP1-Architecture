package com.example.aspecttp.aspects;

import com.example.aspecttp.classes.context.TelemetryContext;
import com.example.aspecttp.classes.events.MetricEvent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.lang.management.*;
import java.util.List;
import java.util.Map;

@Aspect
public class MetricAspect {

    @Around("execution(* com.example.aspecttp.classes.dummyApp..*(..))")
    public Object measureMetrics(ProceedingJoinPoint pjp) throws Throwable {

        Runtime runtime = Runtime.getRuntime();

        // =========================
        // MEMORY BEFORE
        // =========================

        long usedMemoryBefore =
                runtime.totalMemory() - runtime.freeMemory();

        // =========================
        // GC BEFORE
        // =========================

        long gcCountBefore = getGcCount();
        long gcTimeBefore = getGcTime();

        // =========================
        // THREAD BEFORE
        // =========================

        ThreadMXBean threadBean =
                ManagementFactory.getThreadMXBean();

        int threadCountBefore =
                threadBean.getThreadCount();

        // =========================
        // CPU BEFORE
        // =========================

        com.sun.management.OperatingSystemMXBean osBean =
                (com.sun.management.OperatingSystemMXBean)
                        ManagementFactory.getOperatingSystemMXBean();

        long cpuTimeBefore =
                ManagementFactory.getThreadMXBean()
                        .getCurrentThreadCpuTime();

        // =========================
        // EXECUTION START
        // =========================

        long startTime = System.nanoTime();

        Object result = pjp.proceed();

        long executionTimeMs =
                (System.nanoTime() - startTime) / 1_000_000;

        // =========================
        // CPU AFTER
        // =========================

        long cpuTimeAfter =
                ManagementFactory.getThreadMXBean()
                        .getCurrentThreadCpuTime();

        long cpuUsageMs =
                (cpuTimeAfter - cpuTimeBefore) / 1_000_000;

        double processCpuLoad =
                osBean.getProcessCpuLoad() * 100.0;

        // =========================
        // MEMORY AFTER
        // =========================

        long usedMemoryAfter =
                runtime.totalMemory() - runtime.freeMemory();

        long allocatedMemory =
                usedMemoryAfter - usedMemoryBefore;

        // =========================
        // GC AFTER
        // =========================

        long gcCountAfter = getGcCount();
        long gcTimeAfter = getGcTime();

        long gcCollections =
                gcCountAfter - gcCountBefore;

        long gcTimeMs =
                gcTimeAfter - gcTimeBefore;

        // =========================
        // THREAD AFTER
        // =========================

        int threadCountAfter =
                threadBean.getThreadCount();

        // =========================
        // HEAP INFO
        // =========================

        MemoryMXBean memoryBean =
                ManagementFactory.getMemoryMXBean();

        MemoryUsage heapUsage =
                memoryBean.getHeapMemoryUsage();

        long heapUsedMb =
                heapUsage.getUsed() / (1024 * 1024);

        long heapMaxMb =
                heapUsage.getMax() / (1024 * 1024);

        // =========================
        // THROUGHPUT
        // =========================

        double operationsPerSecond =
                executionTimeMs > 0
                        ? 1000.0 / executionTimeMs
                        : 0;

        // =========================
        // CREATE EVENT
        // =========================

        Map<String, Object> metrics = Map.ofEntries(
                Map.entry("execution_time_ms", executionTimeMs),
                Map.entry("cpu_usage_ms", cpuUsageMs),
                Map.entry("process_cpu_percent", processCpuLoad),
                Map.entry("operations_per_second", operationsPerSecond),

                Map.entry("memory_before_kb", usedMemoryBefore / 1024),
                Map.entry("memory_after_kb", usedMemoryAfter / 1024),
                Map.entry("allocated_memory_kb", allocatedMemory / 1024),
                Map.entry("heap_used_mb", heapUsedMb),
                Map.entry("heap_max_mb", heapMaxMb),

                Map.entry("gc_collections", gcCollections),
                Map.entry("gc_time_ms", gcTimeMs),

                Map.entry("threads_before", threadCountBefore),
                Map.entry("threads_after", threadCountAfter)
        );

        MetricEvent event = new MetricEvent(
                pjp.getSignature().getDeclaringTypeName(),
                pjp.getSignature().getName(),
                metrics,
                "execution_time_ms",
                executionTimeMs
        );

        TelemetryContext
                .getBus(MetricEvent.class)
                .publish(event);

        return result;
    }

    private long getGcCount() {

        long total = 0;

        List<GarbageCollectorMXBean> beans =
                ManagementFactory.getGarbageCollectorMXBeans();

        for (GarbageCollectorMXBean bean : beans) {

            long count = bean.getCollectionCount();

            if (count != -1) {
                total += count;
            }
        }

        return total;
    }

    private long getGcTime() {

        long total = 0;

        List<GarbageCollectorMXBean> beans =
                ManagementFactory.getGarbageCollectorMXBeans();

        for (GarbageCollectorMXBean bean : beans) {

            long time = bean.getCollectionTime();

            if (time != -1) {
                total += time;
            }
        }

        return total;
    }
}