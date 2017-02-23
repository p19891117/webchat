package com.heetian.webchat.exception;

public class UserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8427237069903188722L;

	public UserException() {

	}

	public UserException(String msg) {
		super(msg);
	}

	public UserException(Exception e) {
		super(e);
	}
}
