package com.learner.exception;

public class UserRelavantException extends RuntimeException {
	
	private String message;
	
	public UserRelavantException(String message)
	{
		super(message);
	}

}
