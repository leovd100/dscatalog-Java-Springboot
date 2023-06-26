package com.github.leovd100.dscatalog.resources.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.github.leovd100.dscatalog.services.exceptions.DataBaseException;
import com.github.leovd100.dscatalog.services.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException entity, HttpServletRequest request){
			StandardError standard = new StandardError();
			HttpStatus status = HttpStatus.NOT_FOUND;
			standard.setTimestamp(Instant.now());
			standard.setStatus(status.value());
			standard.setError("Resource not found");
			standard.setMessage(entity.getMessage());
			standard.setPath(request.getRequestURI());
			return ResponseEntity.status(status).body(standard);
	}
	
	@ExceptionHandler(DataBaseException.class)
	public ResponseEntity<StandardError> database(DataBaseException entity, HttpServletRequest request){
			StandardError standard = new StandardError();
			HttpStatus status = HttpStatus.BAD_REQUEST;
			standard.setTimestamp(Instant.now());
			standard.setStatus(status.value());
			standard.setError("Database exception");
			standard.setMessage(entity.getMessage());
			standard.setPath(request.getRequestURI());
			return ResponseEntity.status(status).body(standard);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException entity, HttpServletRequest request){
			ValidationError standard = new ValidationError();
			HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
			standard.setTimestamp(Instant.now());
			standard.setStatus(status.value());
			standard.setError("Validation exception");
			standard.setMessage(entity.getMessage());
			standard.setPath(request.getRequestURI());
			for(FieldError f : entity.getBindingResult().getFieldErrors() ) {
				standard.addError(f.getField(), f.getDefaultMessage());
			}
			
			
			
			return ResponseEntity.status(status).body(standard);
	}
}
