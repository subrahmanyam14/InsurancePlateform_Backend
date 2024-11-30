package com.learner.exception;

public class OtpRelaventException extends RuntimeException {

	private String message;
	
	public OtpRelaventException(String message)
	{
		super(message);
	}
	
}
