package com.dollop.app.dtos;

public class BadApiRequestException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BadApiRequestException() {
		super();
	}

	public BadApiRequestException(String message) {
		super(message);
	}

	
}
