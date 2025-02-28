package com.abhi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class Handler {
	@ExceptionHandler(exception = Exception.class)
	public ResponseEntity<?> handleException(Exception ex) {
		System.out.println(ex.getMessage());
		String message = ex.getMessage();
		return new ResponseEntity<>(message,HttpStatus.CONFLICT);
	}

}
