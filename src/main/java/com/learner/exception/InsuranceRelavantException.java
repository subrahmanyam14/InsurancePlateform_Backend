package com.learner.exception;

public class InsuranceRelavantException extends RuntimeException{
	
	private String message;
	
	public InsuranceRelavantException(String message)
	{
		super(message);
	}

}
