package io.baselogic.springsecurity.audit.logger;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *
 */
@Slf4j
public class LoggerThreadLocal {

	static final ThreadLocal<ThreadLocalValues> threadLocal = new ThreadLocal<>();

	private LoggerThreadLocal() {
		super();
	}

	public static void setMethodStack(Deque<Method> methodStack) {
		ThreadLocalValues threadLocalValues = threadLocal.get();

		if (null == threadLocalValues) {
			threadLocalValues = new ThreadLocalValues();
		}

		threadLocalValues.setMethodStack(methodStack);
		threadLocal.set(threadLocalValues);
	}	
	
	public static void setMainMethod(Method mainMethod){
		ThreadLocalValues threadLocalValues = threadLocal.get();

		if (null == threadLocalValues) {
			threadLocalValues = new ThreadLocalValues();
		}

		threadLocalValues.setMainMethod(mainMethod);
		threadLocal.set(threadLocalValues);		
	}	
	
	public static Method getMainMethod() {
		if (threadLocal.get() == null) {
			return null;
		}

		return threadLocal.get().getMainMethod();
	}	
	
	public static Deque<Method> getMethodStack() {
		if (threadLocal.get() == null) {
			setMethodStack(new ArrayDeque<>());
		}

		return threadLocal.get().getMethodStack();
	}

} // The End...
