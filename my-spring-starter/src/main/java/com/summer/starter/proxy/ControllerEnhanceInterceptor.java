package com.summer.starter.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class ControllerEnhanceInterceptor implements MethodInterceptor {


    /**
     * 是否开启增强log
     */
    private boolean enhanceLogOpen;

    public ControllerEnhanceInterceptor(boolean enhanceLogOpen) {
        this.enhanceLogOpen = enhanceLogOpen;
    }

    /**
     * 执行前后的逻辑
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (enhanceLogOpen) {
            System.out.println(invocation.getMethod() + "方法执行前================");
        }
        Object proceed = invocation.proceed();
        if (enhanceLogOpen) {
            System.out.println(invocation.getMethod() + "方法执行后================\n");
        }
        return proceed;
    }
}






