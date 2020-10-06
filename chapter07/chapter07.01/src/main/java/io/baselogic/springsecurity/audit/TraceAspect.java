package io.baselogic.springsecurity.audit;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Service;

/**
 * Trace Monitoring Aspect and Advice
 */
@Aspect
@Service
//@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
//@Scope(value = WebApplicationContext.SCOPE_APPLICATION, proxyMode = ScopedProxyMode.TARGET_CLASS)
//@Configuration
@Slf4j
public class TraceAspect {

//    @Pointcut("within(io.baselogic.springsecurity.web.controllers..*)")
    @Pointcut(value = "execution(* io.baselogic.springsecurity.web.controllers.*Controller.*(..))")
    public void inWebLayer() {}

//    @Pointcut("within(io.baselogic.springsecurity.service..*)")
    @Pointcut(value = "execution(* io.baselogic.springsecurity.service.*Service.*(..))")
    public void inServiceLayer() {}

//    @Pointcut("within(io.baselogic.springsecurity.dao..*)")
    @Pointcut(value = "execution(* io.baselogic.springsecurity.dao.*Dao.*(..))")
    public void inDaoLayer() {}

//    @Pointcut("within(io.baselogic.springsecurity.userdetails..*)")
    @Pointcut(value = "execution(* io.baselogic.springsecurity.userdetails.*Service.*(..))")
    public void inUserDetailsLayer() {}


    //-----------------------------------------------------------------------//

//    @Before("inWebLayer() || inServiceLayer() || inDaoLayer() || inUserDetailsLayer()")
//    public void beginAudit(JoinPoint joinPoint) {
//        log.info("* beginAudit(): {}", joinPoint);
//        AuditContextHolder.getContext().beginAudit(joinPoint);
//    }

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

//    @AfterReturning(value = "inWebLayer() || inServiceLayer() || inDaoLayer() || inUserDetailsLayer()",
//            returning = "returnValue")
//    public void endAudit(Object returnValue) {
//        log.info("* endAudit(): {}", returnValue);
//        AuditContextHolder.getContext().endAudit(returnValue);
//    }

    @AfterReturning(value = "inWebLayer()",
            returning = "returnValue")
    public void printTrace(Object returnValue) {
        log.info("****** printTrace(): {}", returnValue);
        String trace = AuditContextHolder.getContext().printTrace();
        log.info(trace);
    }



} // The End...
