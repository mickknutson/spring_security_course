package io.baselogic.springsecurity.audit.logger;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.JoinPoint;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

@Slf4j
public class LoggerThreadLocal {

	static final ThreadLocal<ThreadLocalValues> threadLocal = new InheritableThreadLocal<>();

	static ObjectMapper mapper = new ObjectMapper();

	private LoggerThreadLocal() {
		super();
	}

	public static ThreadLocalValues getThreadLocalValues() {
		ThreadLocalValues threadLocalValues = threadLocal.get();
		if (null == threadLocalValues) {
			log.info("***** getThreadLocalValues() threadLocalValues == null *****");
			threadLocalValues = new ThreadLocalValues();
		}
		threadLocal.set(threadLocalValues);
		return threadLocal.get();
	}	

	public static void setMethodStack(Deque<Method> methodStack) {
//		ThreadLocalValues threadLocalValues = threadLocal.get();
//		if (null == threadLocalValues) {
//			log.info("***** setMethodStack() threadLocalValues == null *****");
//			threadLocalValues = new ThreadLocalValues();
//		}
//		log.info("set methodStack [{}]", methodStack.size());
//		threadLocalValues.setMethodStack(methodStack);
//		threadLocal.set(threadLocalValues);


		log.info("set methodStack [{}]", methodStack.size());
		getThreadLocalValues().setMethodStack(methodStack);
	}

	public static void setMainMethod(Method mainMethod){
		log.info("---> setMainMethod(): {}", mainMethod);
		ThreadLocalValues threadLocalValues = getThreadLocalValues();
		threadLocalValues.setMainMethod(mainMethod);
		log.info("---> setMainMethod() name: {}", threadLocalValues.getMainMethod().getMethodName());
		threadLocalValues.setMainMethod(mainMethod);
		threadLocal.set(threadLocalValues);

	}	
	
	public static Method getMainMethod() {
		if (getThreadLocalValues() == null) {
			log.info("***** getMainMethod() threadLocal.get() == null *****");
			return null;
		}
		log.info("---> getMainMethod() name: {}", getThreadLocalValues().getMainMethod());
		return getThreadLocalValues().getMainMethod();
	}	
	
	public static void addMethodStack(Method method) {
		Deque<Method> stack = getThreadLocalValues().getMethodStack();
		if(stack.isEmpty()){
			getThreadLocalValues().setMainMethod(method);
		}

		stack.add(method);
	}


	public static Deque<Method> getMethodStack() {

		log.info("methodStack size [{}]", getThreadLocalValues().getMethodStack().size());
		log.info("methodStack [{}]", getThreadLocalValues().getMethodStack());
		log.info("methodStack MainMethod {}", getMainMethod());
		log.info(printTrace());
		return getThreadLocalValues().getMethodStack();
	}


	public static String printTrace() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n<AUDIT>\n");
		try {
			sb.append(
					mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
							getThreadLocalValues().getMainMethod()
//							LoggerThreadLocal.getMainMethod()
					));
		} catch (JsonProcessingException e) {
			sb.append(
					StringUtils.abbreviate(ExceptionUtils.getStackTrace(e), 2_000)
			);
		}
		sb.append("\n</AUDIT>");
		return sb.toString();
	}



	//-----------------------------------------------------------------------//

	public static void pushStack(JoinPoint joinPoint) {
		log.info("* pushStack() output: {}", joinPoint);
		Method m = new Method();
		m.setMethodName(StringUtils.replace(joinPoint.getSignature().toString(), "io.baselogic.springsecurity.", "i.b.s."));
		String input = getInputParametersString(joinPoint.getArgs());
		m.setInput(input);
		m.setTimeInMs(Long.valueOf(System.currentTimeMillis()));
//		LoggerThreadLocal.getMethodStack().push(m);
		addMethodStack(m);
	}

	private static String getInputParametersString(Object[] joinPointArgs) {
		String input;
		try {
			input = mapper.writeValueAsString(joinPointArgs);
		} catch (Exception e) {
			input = "Unable to create input parameters string. Error:" + e.getMessage();
		}
		return input;
	}


	public static void popStack(Object output) {
		Method childMethod = threadLocal.get().getMethodStack().pop();
		log.info("* popStack() output: {}", output);
		log.info("* popStack() childMethod: {}", childMethod);

		try {
			childMethod.setOutput(output == null? "": mapper.writeValueAsString(output));
			log.info("* popStack() childMethod: {}", childMethod);
		} catch (JsonProcessingException e) {
			childMethod.setOutput(e.getMessage());
		}

		childMethod.setTimeInMs(Long.valueOf(System.currentTimeMillis() - childMethod.getTimeInMs().longValue()));

		if (getThreadLocalValues().getMethodStack().isEmpty()) {
			getThreadLocalValues().setMainMethod(childMethod);
			log.info("* popStack() setMainMethod: {}", getThreadLocalValues().getMainMethod());
		} else {
			Method parentMethod = getThreadLocalValues().getMethodStack().peek();
			log.info("* addChildMethod: child: {}, parent: {}", childMethod, parentMethod);
			addChildMethod(childMethod, parentMethod);
		}
	}

	private static void addChildMethod(Method childMethod, Method parentMethod) {
		if (parentMethod != null) {
			if (parentMethod.getMethodList() == null) {
				parentMethod.setMethodList(new ArrayList<>());
			}
			parentMethod.getMethodList().add(childMethod);
			log.info("* parentMethod size: {}", parentMethod.getMethodList().size());
		}

	}


} // The End...
