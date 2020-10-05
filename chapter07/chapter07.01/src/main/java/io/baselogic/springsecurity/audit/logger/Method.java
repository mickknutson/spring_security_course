package com.example.demo.util.log.standartlogger;

import java.util.List;

public class Method {

	private String methodName;
	private String input;
	private List<Method> methodList;
	private String output;
	private Long timeInMs;

	public Long getTimeInMs() {
		return timeInMs;
	}

	public void setTimeInMs(Long timeInMs) {
		this.timeInMs = timeInMs;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public List<Method> getMethodList() {
		return methodList;
	}

	public void setMethodList(List<Method> methodList) {
		this.methodList = methodList;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}	
	
}
