package io.baselogic.springsecurity.audit.logger;

import lombok.extern.slf4j.Slf4j;

import java.util.Deque;

/**
 *
 */
@Slf4j
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

} // The End...
