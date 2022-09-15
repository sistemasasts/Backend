package com.isacore;

import javax.servlet.MultipartConfigElement;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.unit.DataSize;



@SpringBootApplication
@EnableAsync
@EnableTransactionManagement
public class SpringBootIsa extends SpringBootServletInitializer  {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootIsa.class, args);
	}

	 @Override
	    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	        return application.sources(SpringBootIsa.class);
	    }
	 
	 @Bean
	    MultipartConfigElement multipartConfigElement() {
	        MultipartConfigFactory factory = new MultipartConfigFactory();
	        factory.setMaxFileSize(DataSize.ofBytes(512000000L));
	        factory.setMaxRequestSize(DataSize.ofBytes(512000000L));
	        return factory.createMultipartConfig();
	    }

	@Bean
	public LayoutDialect layoutDialect() {
		return new LayoutDialect();
	}
}
