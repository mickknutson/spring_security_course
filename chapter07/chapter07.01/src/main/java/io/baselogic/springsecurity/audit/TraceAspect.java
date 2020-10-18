package io.baselogic.springsecurity.audit;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;

/**
 * Trace Monitoring Aspect and Advice
 */
@Aspect
@Service
@Slf4j
public class TraceAspect {

    @Pointcut(value = "execution(* io.baselogic.springsecurity.web.controllers.*Controller.*(..))")
    public void inWebLayer() {}

    @Pointcut(value = "execution(* io.baselogic.springsecurity.service.*Service.*(..))")
    public void inServiceLayer() {}

    @Pointcut(value = "execution(* io.baselogic.springsecurity.dao.*Dao.*(..))")
    public void inDaoLayer() {}

    @Pointcut(value = "execution(* io.baselogic.springsecurity.userdetails.*Service.*(..))")
    public void inUserDetailsLayer() {}



    @Around("inWebLayer() || inServiceLayer() || inDaoLayer() || inUserDetailsLayer()")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("* beginAudit(): {}", joinPoint);

        long start = System.currentTimeMillis();
        AuditContextHolder.getContext().beginAudit(start, joinPoint);

        Object proceed = joinPoint.proceed();

        long end = System.currentTimeMillis();

        AuditContextHolder.getContext().endAudit(end, proceed);

        return proceed;
    }


    @AfterReturning(value = "inWebLayer()",
            returning = "returnValue")
    public void printTrace(Object returnValue) {
        log.info("****** printTrace(): {}", returnValue);
        String trace = AuditContextHolder.getContext().printTrace();
        log.info(trace);
    }



} // The End...
