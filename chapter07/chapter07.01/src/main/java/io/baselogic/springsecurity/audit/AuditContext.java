package io.baselogic.springsecurity.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.JoinPoint;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

/**
 *
 */
// Lombok Annotations:
@Data
@Slf4j
public class AuditContext {

    private Deque<Method> methodStack = new ArrayDeque<>();

    private Method mainMethod;

    private static final ObjectMapper mapper = new ObjectMapper();

    //-----------------------------------------------------------------------//

    public void beginAudit(long start, JoinPoint joinPoint) {

        Method m = createMethod(joinPoint);
        m.setStartTime(start);

        if (getMethodStack().isEmpty()) {
            setMainMethod(m);
            log.info("* beginAudit() setMainMethod: [{}]", getMainMethod());
        }

        getMethodStack().push(m);
    }


    public void endAudit(long end, Object output) {

        Method childMethod = getMethodStack().pop();
        childMethod.setEndTime(end);
        childMethod.setTimeInMs(childMethod.getEndTime() - childMethod.getStartTime());

        log.info("* endAudit() childMethod: [{}]", childMethod);

        try {
            childMethod.setOutput(output == null? "": mapper.writeValueAsString(output));
        } catch (JsonProcessingException e) {
            childMethod.setOutput(e.getMessage());
        }

//        if (getMethodStack().isEmpty()) {
////            setMainMethod(childMethod);
////            log.info("* endAudit() setMainMethod: [{}]", getMainMethod());
//        }
//        else {
            Method parentMethod = getMethodStack().peek();
//            log.info("* addChildMethod: child: [{}], parent: [{}]", childMethod, parentMethod);
            addChildMethod(childMethod, parentMethod);
//        }
    }



    private Method createMethod(JoinPoint joinPoint) {
        log.info("* createMethod(): [{}]", joinPoint);
        Method m = new Method();
        m.setMethodName(
                StringUtils.replace(joinPoint.getSignature().toString(),
                        "io.baselogic.springsecurity.", "i.b.s."));
        String input = getInputParametersString(joinPoint.getArgs());
        m.setInput(input);
        m.setTimeInMs(Long.valueOf(System.currentTimeMillis()));
        return m;
    }


    private String getInputParametersString(Object[] joinPointArgs) {
        String input;
        try {
            input = mapper.writeValueAsString(joinPointArgs);
        } catch (Exception e) {
            input = "Unable to create input parameters string. Error:" + e.getMessage();
        }
//        log.info("getInputParametersString(): [{}]", input);
        return input;
    }

    private void addChildMethod(Method childMethod, Method parentMethod) {
        if (parentMethod != null) {
            if (parentMethod.getMethodList() == null) {
                parentMethod.setMethodList(new ArrayList<>());
            }
            parentMethod.getMethodList().add(childMethod);
            log.info("* parentMethod size: [{}]", parentMethod.getMethodList().size());
        }

    }


    public String printTrace() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n<AUDIT>\n");
        try {
            sb.append(
                    mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                            getMainMethod()
                    ));
        } catch (JsonProcessingException e) {
            sb.append(
                    StringUtils.abbreviate(ExceptionUtils.getStackTrace(e), 2_000)
            );
        }
        sb.append("\n</AUDIT>");
        return sb.toString();
    }



} // The End...
