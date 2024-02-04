package ru.gb.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.event.Level;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

@Slf4j
@Aspect
@Component
public class HWAspect  {

    @Pointcut("within(@ru.gb.aspect.Timer *)")
    public void beansAnnotatedWith() {

    }

    @Pointcut("@annotation(ru.gb.aspect.Timer)")
    public void methodsAnnotatedWith() {

    }

    @Pointcut("@annotation(ru.gb.aspect.RecoverException)")
    public void methodAnnotatedWithRecoverException(){}


    @Around("beansAnnotatedWith() || methodsAnnotatedWith()")
    public Object TimerAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        Level level = extractLevel(joinPoint);
        log.atLevel(level).log("className = {}", joinPoint.getTarget().getClass());
        log.atLevel(level).log("methodName = {}", joinPoint.getSignature().getName());
        try {
            long start = System.currentTimeMillis();
            Object returnValue = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;
            log.info("количество миллисекунд выполнения: {}",duration);
            return returnValue;

        } catch (Throwable e) {
            log.atLevel(level).log("exception = [{}, {}]", e.getClass(), e.getMessage());
             throw e;
        }
    }

    private Level extractLevel(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Loggable annotation = signature.getMethod().getAnnotation(Loggable.class);
        if (annotation != null) {
            return annotation.level();
        }

        return joinPoint.getTarget().getClass().getAnnotation(Loggable.class).level();
    }
    @Around ("methodAnnotatedWithRecoverException()")
    public Object transformException(ProceedingJoinPoint joinPoint) throws Throwable {
        Class<? extends RuntimeException> []noRecoverFor = extractnoRecover(joinPoint);

        try {
            Object returnValue = joinPoint.proceed();
            return returnValue;

        } catch (Throwable e) {
               Class<? extends RuntimeException> ex = Arrays.stream(noRecoverFor).
               filter(it -> Objects.equals(it, e.getClass()))
                    .findFirst()
                    .orElse(null);
            if (ex == null) {
                System.out.println("******");throw e;
            }

        }
        if (joinPoint.getSignature().getDeclaringType().isPrimitive()) return 0;

        return null;

    }
    private Class<? extends RuntimeException>[] extractnoRecover(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RecoverException annotation = signature.getMethod().getAnnotation(RecoverException.class);
        if (annotation != null) {
            return annotation.noRecoverFor();
        }

       return joinPoint.getTarget().getClass().getAnnotation(RecoverException.class).noRecoverFor();
    }
}