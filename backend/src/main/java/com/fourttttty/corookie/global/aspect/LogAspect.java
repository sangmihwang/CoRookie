package com.fourttttty.corookie.global.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Pointcut("execution(* com.fourttttty.corookie..*Controller.*(..))")
    public void allController() {

    }

    @Around("allController()")
    public Object createControllerLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[REQUEST] Controller: {} | Method {} | Arguments: {}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().toShortString(),
                toParametersString(
                        ((CodeSignature) (joinPoint.getSignature())).getParameterNames(),
                        joinPoint.getArgs()));

        Object result = joinPoint.proceed();
        log.info("[REQUEST] Result: {}", result.getClass().getSimpleName());
        return result;
    }

    private String toParametersString(String[] parameterNames, Object[] args) {
        return IntStream.range(0, parameterNames.length)
                .mapToObj(i -> parameterNames[i] + "=" + args[i])
                .collect(Collectors.joining(","));
    }
}
