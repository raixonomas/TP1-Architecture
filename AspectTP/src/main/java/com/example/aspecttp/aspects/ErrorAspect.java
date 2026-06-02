package com.example.aspecttp.aspects;

import com.example.aspecttp.classes.context.TelemetryContext;
import com.example.aspecttp.classes.events.ErrorEvent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.Map;

@Aspect
public class ErrorAspect {

    //@Around("execution(* com.example.aspecttp.classes.dummyApp..*(..))")
    public Object captureErrors(ProceedingJoinPoint pjp) throws Throwable {

        try {
            return pjp.proceed();
        }
        catch (Throwable ex) {

            String stackTrace = java.util.Arrays.stream(ex.getStackTrace())
                    .limit(10)
                    .map(StackTraceElement::toString)
                    .reduce((a, b) -> a + "\n" + b)
                    .orElse("");

            ErrorEvent event = new ErrorEvent(
                    pjp.getSignature().getDeclaringTypeName(),
                    pjp.getSignature().getName(),
                    Map.of(),
                    ex.getMessage() != null ? ex.getMessage() : "No message",
                    stackTrace
            );

            var bus = TelemetryContext.getBus(ErrorEvent.class);
            if (bus != null) {
                bus.publish(event);
            }

            throw ex;
        }
    }
}