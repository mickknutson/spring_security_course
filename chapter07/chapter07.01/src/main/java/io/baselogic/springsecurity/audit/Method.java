package io.baselogic.springsecurity.audit;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
// Lombok Annotations:
@Data
public class Method {

	private String methodName;
	private String input;
	private List<Method> methodList = new ArrayList<>();
	private String output;
	private Long startTime;
	private Long endTime;
	private Long timeInMs;

} // The End...
