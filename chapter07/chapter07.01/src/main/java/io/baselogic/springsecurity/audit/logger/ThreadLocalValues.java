package com.example.demo.util.log.standartlogger;


import java.util.Deque;

public class ThreadLocalValues {

	private Deque<Method> methodStack;
	private Method mainMethod;

	public ThreadLocalValues() {
		super();
	}

	public Method getMainMethod() {
		return mainMethod;
	}

	public void setMainMethod(Method mainMethod) {
		this.mainMethod = mainMethod;
	}

	public Deque<Method> getMethodStack() {
		return methodStack;
	}	
	
	public void setMethodStack(Deque<Method> methodStack) {
		this.methodStack = methodStack;
	}

}
