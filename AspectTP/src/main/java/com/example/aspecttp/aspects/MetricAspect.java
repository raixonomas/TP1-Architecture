package com.example.aspecttp.aspects;

import com.example.aspecttp.classes.context.TelemetryContext;
import com.example.aspecttp.classes.events.MetricEvent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.Map;

@Aspect
public class MetricAspect {

    @Around("execution(* com.example.aspecttp.classes.dummyApp..*(..))")
    public Object measureExecutionTime(ProceedingJoinPoint pjp) throws Throwable {

        long start = System.nanoTime();
        Object result = pjp.proceed();

        long durationMs = (System.nanoTime() - start) / 1_000_000;

        MetricEvent event = new MetricEvent(
                pjp.getSignature().getDeclaringTypeName(),
                pjp.getSignature().getName(),
                Map.of(),
                "execution_time_ms",
                durationMs
        );

        TelemetryContext
                .getBus(MetricEvent.class)
                .publish(event);

        return result;
    }
}