package com.github.leovd100.dscatalog.resources.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.github.leovd100.dscatalog.services.exceptions.EntityNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {
	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(EntityNotFoundException entity, HttpServletRequest request){
			StandardError standard = new StandardError();
			standard.setTimestamp(Instant.now());
			standard.setStatus(HttpStatus.NOT_FOUND.value());
			standard.setError("Resource not found");
			standard.setMessage(entity.getMessage());
			standard.setPath(request.getRequestURI());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(standard);
	}
}
