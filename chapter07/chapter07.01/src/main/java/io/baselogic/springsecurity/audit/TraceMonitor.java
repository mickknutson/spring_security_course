package io.baselogic.springsecurity.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.baselogic.springsecurity.audit.logger.LoggerThreadLocal;
import io.baselogic.springsecurity.audit.logger.Method;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;

/**
 * Trace Monitoring Aspect and Advice
 */
@Aspect
@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
//@Scope(value = WebApplicationContext.SCOPE_APPLICATION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Configuration
@Slf4j
public class TraceMonitor {

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

    @Before("inWebLayer() || inServiceLayer() || inDaoLayer() || inUserDetailsLayer()")
    public void pushTraceStack(JoinPoint joinPoint) {
//        log.info("* pushTraceStack(): {}", joinPoint);
        LoggerThreadLocal.pushStack(joinPoint);
    }

    @AfterReturning(value = "inWebLayer() || inServiceLayer() || inDaoLayer() || inUserDetailsLayer()",
            returning = "returnValue")
    public void popTraceStack(Object returnValue) {
//        log.info("* popTraceStack(): {}", returnValue);
        LoggerThreadLocal.popStack(returnValue);
    }

    @AfterReturning(value = "inWebLayer()",
            returning = "returnValue")
    public void printTraceStack(Object returnValue) {
        log.info("****** printTraceStack(): {}", returnValue);
        log.info(LoggerThreadLocal.printTrace());
    }



} // The End...
