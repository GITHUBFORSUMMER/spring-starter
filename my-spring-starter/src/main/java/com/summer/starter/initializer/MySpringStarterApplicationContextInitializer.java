package com.summer.starter.initializer;

import com.summer.starter.processor.MyBeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;


/**
 * 根据上下文环境注册属性源或激活配置文件等
 * <p>
 * 注意 spring bean 生命周期中的触发阶段
 */
public class MySpringStarterApplicationContextInitializer implements ApplicationContextInitializer {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {


        /**
         * 注册一个 BeanDefinitionRegistryPostProcessor 的实现类 [MyBeanDefinitionRegistryPostProcessor]
         * 注意不同的 BeanPostProcessor 执行的顺序
         */
        applicationContext.addBeanFactoryPostProcessor(new MyBeanDefinitionRegistryPostProcessor());

        //这里还可以做很多扩展，想象空间非常大，就是太费时间了
        //applicationContext.addApplicationListener();
        //applicationContext.addProtocolResolver();
    }
}



