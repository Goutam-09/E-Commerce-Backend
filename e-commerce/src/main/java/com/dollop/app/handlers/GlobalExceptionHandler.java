package com.dollop.app.handlers;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dollop.app.dtos.ApiResponseMessage;
import com.dollop.app.dtos.BadApiRequestException;
import com.dollop.app.exceptions.ResourceNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponseMessage> resourceNotFoundException(ResourceNotFoundException rnfe){
		logger.info("Exception handler is line");
		ApiResponseMessage response = ApiResponseMessage.builder()
				.message(rnfe.getMessage())
				.status(HttpStatus.NOT_FOUND)
				.success(true).build();
		return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(BadApiRequestException.class)
	public ResponseEntity<ApiResponseMessage> BadApiRequestException(BadApiRequestException bre){
		logger.info("BadApiRequestException handler is line");
		ApiResponseMessage response = ApiResponseMessage.builder()
				.message(bre.getMessage())
				.status(HttpStatus.BAD_REQUEST)
				.success(true).build();
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
		Map<String, Object> map = new HashMap<>();
		ex.getBindingResult().getFieldErrors().
		stream().forEach((error)->{
				map.put(error.getField(), error.getDefaultMessage());
			});
		return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
	}
}
