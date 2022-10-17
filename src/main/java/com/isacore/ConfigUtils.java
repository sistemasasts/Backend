package com.isacore;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;

@Configuration
public class ConfigUtils {

	@Bean
	public Gson gsonLog() {
		return new Gson();
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
