package com.knowget.knowgetbackend.domain.comment.service;

import java.util.List;

import com.knowget.knowgetbackend.domain.comment.dto.CommentRequestDto;
import com.knowget.knowgetbackend.domain.comment.dto.CommentUpdateDto;
import com.knowget.knowgetbackend.global.entity.Comment;

public interface CommentService {
	//create
	String createComment(CommentRequestDto commentRequestDto);

	//read
	List<Comment> getComments(Integer caseId);

	//update
	String updateComment(Integer commentId, CommentUpdateDto commentUpdateDto);

	//delete
	String deleteComment(Integer commentId, String userId);
}