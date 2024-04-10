package com.isacore.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationContext;

@Getter
public final class StaticInjector {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static StaticInjector instance = null;

    @Setter(AccessLevel.PRIVATE)
    private ApplicationContext context;


    private StaticInjector(ApplicationContext context) {
        this.context = context;
    }


    // static method to create instance of Singleton class
    public static StaticInjector getInstance() {
        return instance;
    }

    public static void init(ApplicationContext context) {
        if (instance == null)
            instance = new StaticInjector(context);
    }

    public <T> T getBean(Class<T> aClass) {
        return this.context.getAutowireCapableBeanFactory().getBean(aClass);
    }

    public <T> T getBean(String beanName, Class<T> aClass) {
        return this.context.getAutowireCapableBeanFactory().getBean(beanName, aClass);
    }

}
