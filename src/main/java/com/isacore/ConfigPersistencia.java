package com.isacore;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan(basePackageClasses = EntidadBase.class)
@EnableJpaAuditing
@EnableTransactionManagement
public class ConfigPersistencia {

	@Bean
	public AuditorAware<String> auditorAware() {
		return new AuditorAwareEntidades();
	}

}
