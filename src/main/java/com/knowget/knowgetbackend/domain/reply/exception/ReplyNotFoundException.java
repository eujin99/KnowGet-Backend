package com.knowget.knowgetbackend.domain.reply.exception;

public class ReplyNotFoundException extends RuntimeException {

	public ReplyNotFoundException(String message) {
		super(message);
	}

	public ReplyNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
