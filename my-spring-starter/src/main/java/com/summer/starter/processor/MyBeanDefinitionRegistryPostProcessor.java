package com.summer.starter.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.Ordered;


/**
 * refresh -> invokeBeanFactoryPostProcessors
 * 动态注册Bean到Spring容器
 */
public class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor, Ordered {

    //手动注册bean给spring容器管理
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        //向 spring 容器注册 ControllerEnhanceBeanPostProcessor
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(ControllerEnhanceBeanPostProcessor.class);
        beanDefinitionRegistry.registerBeanDefinition("controllerEnhanceBeanPostProcessor", beanDefinition);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        //这里注册 ControllerEnhanceBeanPostProcessor 其实也是可以的，触发时机不同而已
        //configurableListableBeanFactory.addBeanPostProcessor(new ControllerEnhanceBeanPostProcessor());
    }


    //执行顺序最后
    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}







