package com.heetian.webchat.exception;

public class UserTypeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7176443989516293241L;

	public UserTypeException() {

	}

	public UserTypeException(String msg) {
		super(msg);
	}

	public UserTypeException(Exception e) {
		super(e);
	}
}
