package com.heg.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex){
		ErrorResponse error= new ErrorResponse(
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				ex.getMessage(),
				LocalDateTime.now()
				);
		return new ResponseEntity<ErrorResponse>(error,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse> handleRunTimeException(RuntimeException ex){
		ErrorResponse error = new ErrorResponse(
				HttpStatus.BAD_REQUEST.value(), 
				ex.getMessage(), 
				LocalDateTime.now());
		return new ResponseEntity<ErrorResponse>(error,HttpStatus.BAD_REQUEST);
	}
	
	
	//Handle Custom Exception
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex ){
		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(),
				ex.getMessage(),
				LocalDateTime.now());
		return new ResponseEntity<ErrorResponse>(error,HttpStatus.NOT_FOUND);
	}
	
	
	@ExceptionHandler(ServiceUnavailableException.class)
	public ResponseEntity<ErrorResponse> handleServiceDown(ServiceUnavailableException ex) {
	    return new ResponseEntity<>(
	        new ErrorResponse(503, ex.getMessage(), LocalDateTime.now()),
	        HttpStatus.SERVICE_UNAVAILABLE
	    );
	}

	@ExceptionHandler(InsufficientStockException.class)
	public ResponseEntity<ErrorResponse> handleStock(InsufficientStockException ex) {
	    return new ResponseEntity<>(
	        new ErrorResponse(400, ex.getMessage(), LocalDateTime.now()),
	        HttpStatus.BAD_REQUEST
	    );
	}

}
