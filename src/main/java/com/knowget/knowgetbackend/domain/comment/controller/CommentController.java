package com.knowget.knowgetbackend.domain.comment.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.knowget.knowgetbackend.domain.comment.dto.CommentDeleteDTO;
import com.knowget.knowgetbackend.domain.comment.dto.CommentRequestDTO;
import com.knowget.knowgetbackend.domain.comment.dto.CommentResponseDTO;
import com.knowget.knowgetbackend.domain.comment.dto.CommentUpdateDTO;
import com.knowget.knowgetbackend.domain.comment.service.CommentService;
import com.knowget.knowgetbackend.global.dto.ResultMessageDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/success-case")
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	/**
	 * 특정 성공 사례 게시글에 달린 모든 댓글 조회
	 *
	 * @param caseId 취업 성공사례 게시글 ID
	 * @return 특정 취업 성공사례 게시글에 달린 모든 댓글 리스트
	 * @author Jihwan
	 * @see CommentResponseDTO
	 */
	@GetMapping(value = "/{caseId}/comments", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<List<CommentResponseDTO>> findComments(@PathVariable("caseId") Integer caseId) {
		List<CommentResponseDTO> commentResponseDTOList = commentService.findComments(caseId);
		return new ResponseEntity<>(commentResponseDTOList, HttpStatus.OK);
	}

	/**
	 * 취업 성공사례 게시글에 대한 댓글 작성
	 *
	 * @param caseId            취업 성공사례 게시글 ID
	 * @param commentRequestDTO content : 댓글 내용
	 * @return 댓글 작성 성공 여부 메시지
	 * @author Jihwan
	 */
	@PostMapping("/{caseId}/comment")
	public ResponseEntity<ResultMessageDTO> saveComment(@PathVariable("caseId") Integer caseId,
		@RequestBody CommentRequestDTO commentRequestDTO) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		commentRequestDTO.setCaseId(caseId);
		commentRequestDTO.setUsername(username);
		String message = commentService.saveComment(commentRequestDTO);

		return new ResponseEntity<>(new ResultMessageDTO(message), HttpStatus.CREATED);
	}

	/**
	 * 특정 취업 성공사례 게시글에 달린 댓글 수정
	 *
	 * @param caseId           취업 성공사례 게시글 ID
	 * @param commentId        수정할 댓글 ID
	 * @param commentUpdateDTO content : 수정할 댓글 내용
	 * @return 댓글 수정 성공 여부 메시지
	 * @author Jihwan
	 */
	@PatchMapping("/{caseId}/comment/{commentId}")
	public ResponseEntity<ResultMessageDTO> updateComment(@PathVariable("caseId") Integer caseId,
		@PathVariable("commentId") Integer commentId, @RequestBody CommentUpdateDTO commentUpdateDTO) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		commentUpdateDTO.setCaseId(caseId);
		commentUpdateDTO.setCommentId(commentId);
		commentUpdateDTO.setUsername(username);
		String message = commentService.updateComment(commentUpdateDTO);

		return new ResponseEntity<>(new ResultMessageDTO(message), HttpStatus.OK);
	}

	/**
	 * 특정 취업 성공사례 게시글에 달린 댓글 삭제
	 *
	 * @param caseId    취업 성공사례 게시글 ID
	 * @param commentId 삭제할 댓글 ID
	 * @return 댓글 삭제 성공 여부 메시지
	 * @author Jihwan
	 */
	@DeleteMapping("/{caseId}/comment/{commentId}")
	public ResponseEntity<ResultMessageDTO> deleteComment(@PathVariable("caseId") Integer caseId,
		@PathVariable("commentId") Integer commentId) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		CommentDeleteDTO commentDeleteDTO = CommentDeleteDTO.builder()
			.caseId(caseId)
			.commentId(commentId)
			.username(username)
			.build();
		String message = commentService.deleteComment(commentDeleteDTO);

		return new ResponseEntity<>(new ResultMessageDTO(message), HttpStatus.OK);
	}

}
