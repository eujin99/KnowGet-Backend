package com.knowget.knowgetbackend.domain.reply.controller;

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

import com.knowget.knowgetbackend.domain.reply.dto.ReplyDeleteDTO;
import com.knowget.knowgetbackend.domain.reply.dto.ReplyRequestDTO;
import com.knowget.knowgetbackend.domain.reply.dto.ReplyResponseDTO;
import com.knowget.knowgetbackend.domain.reply.dto.ReplyUpdateDTO;
import com.knowget.knowgetbackend.domain.reply.service.ReplyService;
import com.knowget.knowgetbackend.global.dto.ResultMessageDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class ReplyController {

	private final ReplyService replyService;

	/**
	 * 특정 댓글에 달린 답글 조회
	 *
	 * @param commentId 답글이 달린 댓글 ID
	 * @return 특정 댓글에 달린 답글 리스트
	 * @author Jihwan
	 * @see ReplyResponseDTO
	 */
	@GetMapping(value = "/{commentId}/replies", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<List<ReplyResponseDTO>> findReplies(@PathVariable("commentId") Integer commentId) {
		List<ReplyResponseDTO> replyResponseDTOList = replyService.findReplies(commentId);
		return new ResponseEntity<>(replyResponseDTOList, HttpStatus.OK);
	}

	/**
	 * 특정 댓글에 달린 답글 작성
	 *
	 * @param commentId       답글이 달린 댓글 ID
	 * @param replyRequestDTO content : 답글 내용
	 * @return 답글 작성 성공 여부 메시지
	 * @author Jihwan
	 */
	@PostMapping("/{commentId}/reply")
	public ResponseEntity<ResultMessageDTO> saveReply(@PathVariable("commentId") Integer commentId,
		@RequestBody ReplyRequestDTO replyRequestDTO) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		replyRequestDTO.setCommentId(commentId);
		replyRequestDTO.setUsername(username);
		String message = replyService.saveReply(replyRequestDTO);

		return new ResponseEntity<>(new ResultMessageDTO(message), HttpStatus.CREATED);
	}

	/**
	 * 특정 댓글에 달린 답글 수정
	 *
	 * @param commentId      답글이 달린 댓글 ID
	 * @param replyId        수정할 답글 ID
	 * @param replyUpdateDTO content : 수정할 답글 내용
	 * @return 답글 수정 성공 여부 메시지
	 * @author Jihwan
	 */
	@PatchMapping("/{commentId}/reply/{replyId}")
	public ResponseEntity<ResultMessageDTO> updateReply(@PathVariable("commentId") Integer commentId,
		@PathVariable("replyId") Integer replyId, @RequestBody ReplyUpdateDTO replyUpdateDTO) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		replyUpdateDTO.setCommentId(commentId);
		replyUpdateDTO.setReplyId(replyId);
		replyUpdateDTO.setUsername(username);
		String message = replyService.updateReply(replyUpdateDTO);

		return new ResponseEntity<>(new ResultMessageDTO(message), HttpStatus.OK);
	}

	/**
	 * 특정 댓글에 달린 답글 삭제
	 *
	 * @param commentId 답글이 달린 댓글 ID
	 * @param replyId   삭제할 답글 ID
	 * @return 답글 삭제 성공 여부 메시지
	 * @author Jihwan
	 */
	@DeleteMapping("/{commentId}/reply/{replyId}")
	public ResponseEntity<ResultMessageDTO> deleteReply(@PathVariable("commentId") Integer commentId,
		@PathVariable("replyId") Integer replyId) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		ReplyDeleteDTO replyDeleteDTO = ReplyDeleteDTO.builder()
			.commentId(commentId)
			.replyId(replyId)
			.username(username)
			.build();
		String message = replyService.deleteReply(replyDeleteDTO);

		return new ResponseEntity<>(new ResultMessageDTO(message), HttpStatus.OK);
	}

}
