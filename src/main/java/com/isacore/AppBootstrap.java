package com.isacore;

import com.isacore.util.StaticInjector;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class AppBootstrap {

    private final ApplicationContext applicationContext;

    public AppBootstrap(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() {
        StaticInjector.init(applicationContext);
    }

}
