package com.knowget.knowgetbackend.domain.jobGuide.exception;

public class ResourceNotFoundException extends RuntimeException {
	/**
	 * 게시글 찾을 수 없을 때 예외처리
	 * @param message responseDTO 에서 예외처리 메시지
	 * @author 유진
	 */
	public ResourceNotFoundException(String message) {
		super(message);
	}
}
