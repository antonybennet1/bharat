package com.wl.bharatqr.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class CatchThrowException {

	//@AfterThrowing (pointcut = "execution(* com.wl.upi.service..*(..))")
	/*@Before("loggingOperation()")
    public void logAfterThrowingAllMethods(JoinPoint joinPoint, Exception ex) throws Throwable
    {
        System.out.println("****LoggingAspect.logAfterThrowingAllMethods() " + ex);
        System.out.println("****LoggingAspect.logAfterThrowingAllMethods() " + joinPoint.getSignature().getName());
    }*/
	
	/*@Pointcut("within(com.journaldev.spring.service.*)")
	public void allMethodsPointcut(){}*/
	
	/*@Pointcut("within(com.wl.upi.service.TransferService)")
    protected void loggingOperation()
    {
    }*/
}
