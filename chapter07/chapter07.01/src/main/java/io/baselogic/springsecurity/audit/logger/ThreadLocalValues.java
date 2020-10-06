package io.baselogic.springsecurity.audit.logger;

import lombok.Data;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *
 */
// Lombok Annotations:
@Data
public class ThreadLocalValues {

	private Deque<Method> methodStack = new ArrayDeque<>();

	private Method mainMethod;



//	public ThreadLocalValues() {
//		super();
//	}
//
//	public Method getMainMethod() {
//		return mainMethod;
//	}
//	public void setMainMethod(Method mainMethod) {
//		this.mainMethod = mainMethod;
//	}
//
//	public Deque<Method> getMethodStack() {
//		return methodStack;
//	}
//	public void setMethodStack(Deque<Method> methodStack) {
//		this.methodStack = methodStack;
//	}

} // The End...
