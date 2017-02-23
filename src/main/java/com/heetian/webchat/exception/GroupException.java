package com.heetian.webchat.exception;

public class GroupException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5739570355479214067L;

	public GroupException() {

	}

	public GroupException(String msg) {
		super(msg);
	}

	public GroupException(Exception e) {
		super(e);
	}
}
