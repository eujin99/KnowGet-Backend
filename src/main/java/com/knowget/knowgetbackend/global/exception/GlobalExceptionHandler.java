package com.knowget.knowgetbackend.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.knowget.knowgetbackend.global.dto.ResultMessageDTO;

import io.jsonwebtoken.ExpiredJwtException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ResultMessageDTO> handleResourceNotFoundException(ResourceNotFoundException e) {
		return new ResponseEntity<>(new ResultMessageDTO(e.getMessage()), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ResultMessageDTO> handleIllegalArgumentException(IllegalArgumentException e) {
		return new ResponseEntity<>(new ResultMessageDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ResultMessageDTO> handleUserNotFoundException(UserNotFoundException e) {
		return new ResponseEntity<>(new ResultMessageDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidPasswordException.class)
	public ResponseEntity<ResultMessageDTO> handleInvalidPasswordException(InvalidPasswordException e) {
		return new ResponseEntity<>(new ResultMessageDTO(e.getMessage()), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(SuccessCaseNotFoundException.class)
	public ResponseEntity<ResultMessageDTO> handleSuccessCaseNotFoundException(SuccessCaseNotFoundException e) {
		return new ResponseEntity<>(new ResultMessageDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(CommentNotFoundException.class)
	public ResponseEntity<ResultMessageDTO> handleCommentNotFoundException(CommentNotFoundException e) {
		return new ResponseEntity<>(new ResultMessageDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AnswerNotFoundException.class)
	public ResponseEntity<ResultMessageDTO> handleAnswerNotFoundException(AnswerNotFoundException e) {
		return new ResponseEntity<>(new ResultMessageDTO(e.getMessage()), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(CounselingNotFoundException.class)
	public ResponseEntity<ResultMessageDTO> handleCounselingNotFoundException(CounselingNotFoundException e) {
		return new ResponseEntity<>(new ResultMessageDTO(e.getMessage()), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(RequestFailedException.class)
	public ResponseEntity<ResultMessageDTO> handleRequestFailedException(RequestFailedException e) {
		return new ResponseEntity<>(new ResultMessageDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ReplyNotFoundException.class)
	public ResponseEntity<ResultMessageDTO> handleReplyNotFoundException(ReplyNotFoundException e) {
		return new ResponseEntity<>(new ResultMessageDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(PostNotFoundException.class)
	public ResponseEntity<ResultMessageDTO> handlePostNotFoundException(PostNotFoundException e) {
		return new ResponseEntity<>(new ResultMessageDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NotificationNotFoundException.class)
	public ResponseEntity<ResultMessageDTO> handleNotificationNotFoundException(NotificationNotFoundException e) {
		return new ResponseEntity<>(new ResultMessageDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ResultMessageDTO> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
		return new ResponseEntity<>(new ResultMessageDTO("File size exceeds limit!"), HttpStatus.PAYLOAD_TOO_LARGE);
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ResultMessageDTO> handleUsernameNotFoundException(UsernameNotFoundException e) {
		return new ResponseEntity<>(new ResultMessageDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<ResultMessageDTO> handleExpiredJwtException(ExpiredJwtException e) {
		return new ResponseEntity<>(new ResultMessageDTO(e.getMessage()), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(ImageNotFoundException.class)
	public ResponseEntity<ResultMessageDTO> handleImageNotFoundException(ImageNotFoundException e) {
		return new ResponseEntity<>(new ResultMessageDTO(e.getMessage()), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DocumentNotFoundException.class)
	public ResponseEntity<ResultMessageDTO> handleDocumentNotFoundException(DocumentNotFoundException e) {
		return new ResponseEntity<>(new ResultMessageDTO(e.getMessage()), HttpStatus.NOT_FOUND);
	}

}
