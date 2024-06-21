package com.knowget.knowgetbackend.domain.comment.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.knowget.knowgetbackend.domain.comment.dto.CommentDeleteDTO;
import com.knowget.knowgetbackend.domain.comment.dto.CommentRequestDTO;
import com.knowget.knowgetbackend.domain.comment.dto.CommentResponseDTO;
import com.knowget.knowgetbackend.domain.comment.dto.CommentUpdateDTO;
import com.knowget.knowgetbackend.domain.comment.repository.CommentRepository;
import com.knowget.knowgetbackend.domain.successCase.repository.SuccessCaseRepository;
import com.knowget.knowgetbackend.domain.user.repository.UserRepository;
import com.knowget.knowgetbackend.global.entity.Comment;
import com.knowget.knowgetbackend.global.entity.SuccessCase;
import com.knowget.knowgetbackend.global.entity.User;
import com.knowget.knowgetbackend.global.exception.CommentNotFoundException;
import com.knowget.knowgetbackend.global.exception.RequestFailedException;
import com.knowget.knowgetbackend.global.exception.SuccessCaseNotFoundException;
import com.knowget.knowgetbackend.global.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	private final SuccessCaseRepository successCaseRepository;

	/**
	 * 특정 취업 성공사례 게시글에 달린 모든 댓글 조회
	 *
	 * @param caseId 취업 성공사례 게시글 ID
	 * @return 특정 취업 성공사례 게시글에 달린 모든 댓글 리스트
	 * @throws SuccessCaseNotFoundException 존재하지 않는 게시글일 경우
	 * @author Jihwan
	 * @see CommentResponseDTO
	 */
	@Override
	@Transactional(readOnly = true)
	public List<CommentResponseDTO> findComments(Integer caseId) {
		successCaseRepository.findById(caseId)
			.orElseThrow(() -> new SuccessCaseNotFoundException("[Error] 댓글을 불러오는데에 실패했습니다 : 존재하지 않는 게시글입니다"));
		List<Comment> comments = commentRepository.findBySuccessCaseIdOrderByCreatedDateAsc(caseId);
		return comments.stream().map(CommentResponseDTO::new).collect(Collectors.toList());
	}

	/**
	 * 취업 성공사례 게시글에 대한 댓글 작성
	 *
	 * @param commentRequestDTO caseId : 취업 성공사례 게시글 ID, username : 사용자 ID, content : 댓글 내용
	 * @return 댓글 작성 성공 여부 메시지 + CommentId
	 * @throws SuccessCaseNotFoundException 존재하지 않는 게시글일 경우
	 * @throws UserNotFoundException        존재하지 않는 사용자일 경우
	 * @throws RequestFailedException       댓글 작성에 실패했을 경우
	 * @author Jihwan
	 */
	@Override
	@Transactional
	public String saveComment(CommentRequestDTO commentRequestDTO) {
		try {
			SuccessCase successCase = successCaseRepository.findById(commentRequestDTO.getCaseId())
				.orElseThrow(() -> new SuccessCaseNotFoundException("존재하지 않는 게시글입니다"));
			User user = userRepository.findByUsername(commentRequestDTO.getUsername())
				.orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다"));
			Comment comment = Comment.builder()
				.successCase(successCase)
				.user(user)
				.content(commentRequestDTO.getContent())
				.build();
			Comment result = commentRepository.save(comment);
			return "댓글이 작성되었습니다 : [CommentId=" + result.getCommentId() + "]";
		} catch (Exception e) {
			throw new RequestFailedException("[Error] 댓글 작성에 실패했습니다 : " + e.getMessage());
		}
	}

	/**
	 * 특정 취업 성공사례 게시글에 달린 댓글 수정
	 *
	 * @param commentUpdateDTO caseId : 취업 성공사례 게시글 ID, commentId : 댓글 ID, username : 사용자 ID, content : 수정할 댓글 내용
	 * @return 댓글 수정 성공 여부 메시지 + CommentId
	 * @throws CommentNotFoundException 존재하지 않는 댓글일 경우
	 * @throws RequestFailedException   댓글 수정에 실패했을 경우
	 * @author Jihwan
	 */
	@Override
	@Transactional
	public String updateComment(CommentUpdateDTO commentUpdateDTO) {
		try {
			Comment comment = commentRepository.findById(commentUpdateDTO.getCommentId())
				.orElseThrow(() -> new CommentNotFoundException("존재하지 않는 댓글입니다"));
			comment.updateContent(commentUpdateDTO.getContent());
			commentRepository.save(comment);
			return "댓글이 수정되었습니다 : [CommentId=" + comment.getCommentId() + "]";
		} catch (Exception e) {
			throw new RequestFailedException("[Error] 댓글 수정에 실패했습니다 : " + e.getMessage());
		}
	}

	/**
	 * 특정 취업 성공사례 게시글에 달린 댓글 삭제
	 *
	 * @param commentDeleteDTO caseId : 취업 성공사례 게시글 ID, commentId : 댓글 ID, username : 사용자 ID
	 * @return 댓글 삭제 성공 여부 메시지
	 * @throws CommentNotFoundException 존재하지 않는 댓글일 경우
	 * @throws RequestFailedException   댓글 삭제에 실패했을 경우
	 * @author Jihwan
	 */
	@Override
	@Transactional
	public String deleteComment(CommentDeleteDTO commentDeleteDTO) {
		try {
			Comment comment = commentRepository.findById(commentDeleteDTO.getCommentId())
				.orElseThrow(() -> new CommentNotFoundException("존재하지 않는 댓글입니다"));
			commentRepository.delete(comment);
			return "댓글이 삭제되었습니다";
		} catch (Exception e) {
			throw new RequestFailedException("[Error] 댓글 삭제에 실패했습니다 : " + e.getMessage());
		}
	}

}
