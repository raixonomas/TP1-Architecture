package com.example.aspecttp.aspects;

import com.example.aspecttp.classes.context.TelemetryContext;
import com.example.aspecttp.classes.events.MetricEvent;

public class MetricAspect {

    @Around("execution(* com.example..*(..))")
    public Object measureExecutionTime(ProceedingJoinPoint pjp) throws Throwable {

        long start = System.nanoTime();

        Object result = pjp.proceed();

        long durationMs = (System.nanoTime() - start) / 1_000_000;

        MetricEvent event = new MetricEvent(
                "execution_time_ms",
                durationMs,
                pjp.getSignature().getDeclaringTypeName(),
                pjp.getSignature().getName()
        );

        TelemetryContext
                .getBus(MetricEvent.class)
                .publish(event);

        return result;
    }
}