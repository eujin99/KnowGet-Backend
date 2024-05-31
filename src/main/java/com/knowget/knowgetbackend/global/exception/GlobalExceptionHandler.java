package com.knowget.knowgetbackend.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.knowget.knowgetbackend.domain.comment.exception.CommentNotFoundException;
import com.knowget.knowgetbackend.domain.comment.exception.SuccessCaseNotFoundException;
import com.knowget.knowgetbackend.domain.answer.exception.AnswerNotFoundException;
import com.knowget.knowgetbackend.domain.counseling.exception.CounselingNotFoundException;
import com.knowget.knowgetbackend.domain.jobGuide.exception.ResourceNotFoundException;
import com.knowget.knowgetbackend.domain.user.exception.InvalidPasswordException;
import com.knowget.knowgetbackend.domain.user.exception.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InvalidPasswordException.class)
	public ResponseEntity<String> handleInvalidPasswordException(InvalidPasswordException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(SuccessCaseNotFoundException.class)
	public ResponseEntity<String> handleSuccessCaseNotFoundException(SuccessCaseNotFoundException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(CommentNotFoundException.class)
	public ResponseEntity<String> handleCommentNotFoundException(CommentNotFoundException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
  }
  
	@ExceptionHandler(AnswerNotFoundException.class)
	public ResponseEntity<String> handleAnswerNotFoundException(AnswerNotFoundException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(CounselingNotFoundException.class)
	public ResponseEntity<String> handleCounselingNotFoundException(CounselingNotFoundException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

}
