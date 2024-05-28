package com.knowget.knowgetbackend.domain.comment.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.knowget.knowgetbackend.domain.comment.dto.CommentRequestDto;
import com.knowget.knowgetbackend.domain.comment.dto.CommentUpdateDto;
import com.knowget.knowgetbackend.domain.comment.service.CommentService;
import com.knowget.knowgetbackend.global.entity.Comment;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	//작성
	@PostMapping("/comment/create")
	public ResponseEntity<String> createComment(@RequestBody CommentRequestDto commentRequestDto) {
		if (commentRequestDto.getContent() == null || commentRequestDto.getContent().isEmpty()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		commentRequestDto.setUsername(userId);

		String msg = commentService.createComment(commentRequestDto);
		return new ResponseEntity<>(msg, HttpStatus.CREATED);
	}

	//특정 게시글의 모든 댓글 조회
	@PostMapping(value = "/qna/{caseId}/comments", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<List<Comment>> getComments(@PathVariable("caseId") Integer caseId) {
		List<Comment> comments = commentService.getComments(caseId);
		return new ResponseEntity<>(comments, HttpStatus.OK);
	}

	//수정
	@PutMapping("/comment/update/{commentId}")
	public ResponseEntity<String> updateComment(@PathVariable("commentId") Integer commentId,
		@RequestBody CommentUpdateDto commentUpdateDto) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		commentUpdateDto.setUsername(username);
		String msg = commentService.updateComment(commentId, commentUpdateDto);
		return new ResponseEntity<>(msg, HttpStatus.OK);
	}

	//삭제
	@DeleteMapping("/comment/delete/{commentId}")
	public ResponseEntity<String> deleteComment(@PathVariable("commentId") Integer commentId) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		String msg = commentService.deleteComment(commentId, username);
		return new ResponseEntity<>(msg, HttpStatus.OK);
	}
}