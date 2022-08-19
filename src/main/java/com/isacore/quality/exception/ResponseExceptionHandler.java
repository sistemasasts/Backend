package com.isacore.quality.exception;

import java.time.LocalDate;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler{

	//Metodo genérico para capturar cualquier error que no este mapeado
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ExceptionResponse> manejarTodasExcepciones(ModeloNotFoundxception ex, WebRequest request){
		
		ExceptionResponse er = new ExceptionResponse(LocalDate.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<ExceptionResponse>(er, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	@ExceptionHandler(ModeloNotFoundxception.class)
	public final ResponseEntity<ExceptionResponse> manejarModeloException(ModeloNotFoundxception ex, WebRequest request){
		
		ExceptionResponse er = new ExceptionResponse(LocalDate.now(), ex.getMessage(), request.getDescription(false));		
		return new ResponseEntity<ExceptionResponse>(er, HttpStatus.NOT_FOUND);
		
	}
	
	//Método sobre escrito para lanzar excepciones de acuerdo a los argumentos validados en la clase
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		// TODO Auto-generated method stub
		ExceptionResponse er = new ExceptionResponse(LocalDate.now(), ex.getMessage(), request.getDescription(false));		
		return new ResponseEntity<Object>(er, HttpStatus.BAD_REQUEST);
	}
}
