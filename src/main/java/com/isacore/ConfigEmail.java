package com.isacore;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import javax.annotation.PostConstruct;

/**
 * Para que Thymeleaf 3 funcione correctamente tanto con el TemplateResolver autoconfigurado por Spring Boot
 * como con el resto que se configuran manualmente en esta clase, fue necesario considerar lo mencionado
 * en el siguiente recurso:
 * 
 * https://github.com/spring-projects/spring-boot/issues/6500
 * 
 * Otros detalles relacionados con el rediseño de TemplateResolver se encuentran aquí:
 * 
 * https://github.com/thymeleaf/thymeleaf/issues/419
 */
@Configuration
public class ConfigEmail {
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private SpringTemplateEngine templateEngine;
	
	private void agregarConfiguracionPlantillasHtml() {
		
		final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setOrder(2); // Debe ser de menor prioridad que el TemplateResolver configurado por Spring Boot.
		templateResolver.setPrefix("templatesEmail/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCharacterEncoding("UTF-8");
		templateResolver.setCheckExistence(true);
		templateResolver.setCacheable(environment.getRequiredProperty("spring.thymeleaf.cache", Boolean.class));
		
		templateEngine.addTemplateResolver(templateResolver);
		templateEngine.addDialect(new LayoutDialect());
	}
	
	private void agregarConfiguracionPlantillasText() {
		
		final StringTemplateResolver templateResolver = new StringTemplateResolver();
		templateResolver.setOrder(3); // Este debe ser el de menor prioridad entre todos los TemplateResolver.
		templateResolver.setTemplateMode(TemplateMode.TEXT);
		templateResolver.setCheckExistence(false);
		
		templateEngine.addTemplateResolver(templateResolver);
	}

	@PostConstruct
	public void agregarThymeleafTemplateResolver() {
		agregarConfiguracionPlantillasHtml();
		agregarConfiguracionPlantillasText();
	}
}
