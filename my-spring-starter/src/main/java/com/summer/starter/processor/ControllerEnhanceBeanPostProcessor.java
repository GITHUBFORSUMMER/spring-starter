package com.summer.starter.processor;


import com.summer.starter.proxy.ControllerEnhanceInterceptor;
import com.summer.starter.proxy.ControllerEnhanceInvocationHandler;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 控制器增强后置处理
 */

public class ControllerEnhanceBeanPostProcessor implements BeanPostProcessor, EnvironmentAware {

    /**
     * 增强log是否打开
     */
    public static enum EnhanceLogEnum {

        LOG_ON,
        LOG_OFF;

        private EnhanceLogEnum() {
        }
    }

    /**
     * 记录已经创建过代理对象的 bean
     */
    private ConcurrentHashMap<String, Object> beanCache = new ConcurrentHashMap<>();

    //增强 log 配置 key
    private static final String enhanceLogOpenEnv = "spring.controller.enhance.log.open";

    //是否开启增强log
    private boolean enhanceLogOpen = true;

    //可以拿到 application.yml 的配置信息
    @Override
    public void setEnvironment(Environment environment) {
        //读取配置中的设置
        String openLogSetting = environment.getProperty(enhanceLogOpenEnv);
        if (EnhanceLogEnum.LOG_OFF.name().toLowerCase().equals(openLogSetting)) {
            enhanceLogOpen = false;
        }
    }


    /**
     * 实例化完成，放进单例池之前的阶段
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //是否是 controller 对象
        boolean hasControllerAnnotation = false;

        Class<?>[] interfaces = bean.getClass().getInterfaces();

        if (interfaces.length <= 0) {
            //检验是否是 controller bean  普通对象 bean.getClass() 就可以获取到 class 的 Annotation 信息
            hasControllerAnnotation = matchController(bean.getClass());
        } else {
            //被springboot处理过的代理对象需要获取 super class 才能拿到真实的 class 的 Annotation 信息，否则拿不到注解信息
            //检验是否是 controller bean
            hasControllerAnnotation = matchController(bean.getClass().getSuperclass());
        }
        //如果是 controller bean 创建代理对象      //如果是 controller bean 创建代理对象
        if (hasControllerAnnotation) {
            return this.creatCglibProxy(bean, beanName, enhanceLogOpen);
        }
        //返回默认 bean
        return bean;
    }


    /**
     * 递归获取包含 base 中是否带有四个标签的注解来判断是否是 controller
     *
     * @param clazz
     * @return
     */
    private boolean matchController(Class<?> clazz) {
        for (Annotation annotation : clazz.getAnnotations()) {
            if (annotation instanceof Controller
                    || annotation instanceof RestController
                    || annotation instanceof Mapping
                    || annotation instanceof RequestMapping) {
                return true;
            }
        }
        if (clazz.getSuperclass() != null) {
            matchController(clazz.getSuperclass());
        }
        return false;
    }


    /**
     * 创建代理对象
     *
     * @param bean
     * @param beanName
     * @param enhanceLogOpen
     * @return
     */
    private Object creatJdkProxy(Object bean, String beanName, boolean enhanceLogOpen) {
        Object beanCache = this.beanCache.get(beanName);
        if (beanCache != null) {
            return beanCache;
        }

        //ControllerEnhanceInvocationHandler  jdk代理对象
        ControllerEnhanceInvocationHandler invocationHandler = new ControllerEnhanceInvocationHandler(bean, enhanceLogOpen);
        Object proxyBean = Proxy.newProxyInstance(bean.getClass().getClassLoader(), bean.getClass().getInterfaces(), invocationHandler);

        this.beanCache.put(beanName, proxyBean);
        return proxyBean;
    }

    /**
     * 创建代理对象
     *
     * @param bean
     * @param beanName
     * @param enhanceLogOpen
     * @return
     */
    private Object creatCglibProxy(Object bean, String beanName, boolean enhanceLogOpen) {
        Object beanCache = this.beanCache.get(beanName);
        if (beanCache != null) {
            return beanCache;
        }

        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(bean);
        proxyFactory.addAdvice(new ControllerEnhanceInterceptor(enhanceLogOpen));
        Object proxyBean = proxyFactory.getProxy();

        this.beanCache.put(beanName, proxyBean);
        return proxyBean;
    }


}
