package com.abhi.filter;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExHandler {
	@ExceptionHandler(exception = RuntimeException.class)
	public String handler(RuntimeException ex) {
		return ex.getMessage();
	}
}
