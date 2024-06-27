package com.knowget.knowgetbackend.global.exception;

public class DeactivatedUserException extends RuntimeException {

	public DeactivatedUserException(String message) {
		super(message);
	}

}
