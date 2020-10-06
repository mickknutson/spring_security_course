package io.baselogic.springsecurity.audit;

import io.baselogic.springsecurity.audit.logger.Method;
import lombok.Data;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *
 */
// Lombok Annotations:
@Data
public class AuditContext {

    private Deque<Method> methodStack = new ArrayDeque<>();

    private Method mainMethod;

}
