package io.baselogic.springsecurity.audit;

import lombok.Data;

import java.util.List;

// Lombok Annotations:
@Data
public class Method {

    private String methodName;
    private String input;
    private List<Method> methodList;
    private String output;
    private Long timeInMs;

}