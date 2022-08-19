package com.isacore.util;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class LocalDateSerializeIsa extends JsonSerializer<LocalDate>{

	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public void serialize(LocalDate ld, JsonGenerator generator, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		String dateString = ld.format(formatter);
		generator.writeString(dateString);
		
	}

}
