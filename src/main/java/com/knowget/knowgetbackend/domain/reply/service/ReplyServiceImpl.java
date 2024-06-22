package com.knowget.knowgetbackend.domain.reply.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.knowget.knowgetbackend.domain.comment.repository.CommentRepository;
import com.knowget.knowgetbackend.domain.reply.dto.ReplyDeleteDTO;
import com.knowget.knowgetbackend.domain.reply.dto.ReplyRequestDTO;
import com.knowget.knowgetbackend.domain.reply.dto.ReplyResponseDTO;
import com.knowget.knowgetbackend.domain.reply.dto.ReplyUpdateDTO;
import com.knowget.knowgetbackend.domain.reply.repository.ReplyRepository;
import com.knowget.knowgetbackend.domain.user.repository.UserRepository;
import com.knowget.knowgetbackend.global.entity.Comment;
import com.knowget.knowgetbackend.global.entity.Reply;
import com.knowget.knowgetbackend.global.entity.User;
import com.knowget.knowgetbackend.global.exception.CommentNotFoundException;
import com.knowget.knowgetbackend.global.exception.ReplyNotFoundException;
import com.knowget.knowgetbackend.global.exception.RequestFailedException;
import com.knowget.knowgetbackend.global.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

	private final ReplyRepository replyRepository;
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;

	/**
	 * 특정 댓글에 달린 모든 답글 조회
	 *
	 * @param commentId 답글이 달린 댓글 ID
	 * @return 특정 댓글에 달린 답글 리스트
	 * @throws CommentNotFoundException 존재하지 않는 댓글일 경우
	 * @author Jihwan
	 * @see ReplyResponseDTO
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ReplyResponseDTO> findReplies(Integer commentId) {
		commentRepository.findById(commentId)
			.orElseThrow(() -> new CommentNotFoundException("[Error] 답글을 불러오는데에 실패했습니다 : 존재하지 않는 댓글입니다"));
		List<Reply> replies = replyRepository.findAllByCommentIdOrderByCreatedDateAsc(commentId);
		return replies.stream().map(ReplyResponseDTO::new).collect(Collectors.toList());
	}

	/**
	 * 특정 댓글에 답글 작성
	 *
	 * @param replyRequestDTO commentId : 답글이 달린 댓글 ID, username : 사용자 ID, content : 답글 내용
	 * @return 답글 작성 성공 여부 메시지
	 * @throws CommentNotFoundException 존재하지 않는 댓글일 경우
	 * @throws UserNotFoundException    존재하지 않는 사용자일 경우
	 * @throws RequestFailedException   답글 작성에 실패했을 경우
	 * @author Jihwan
	 */
	@Override
	@Transactional
	public String saveReply(ReplyRequestDTO replyRequestDTO) {
		try {
			Comment comment = commentRepository.findById(replyRequestDTO.getCommentId())
				.orElseThrow(() -> new CommentNotFoundException("존재하지 않는 댓글입니다"));
			User user = userRepository.findByUsername(replyRequestDTO.getUsername())
				.orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다"));
			Reply reply = Reply.builder()
				.comment(comment)
				.user(user)
				.content(replyRequestDTO.getContent())
				.build();
			Reply result = replyRepository.save(reply);
			return "답글이 작성되었습니다 : [ReplyId=" + result.getReplyId() + "]";
		} catch (Exception e) {
			throw new RequestFailedException("[Error] 답글 작성에 실패했습니다 : " + e.getMessage());
		}
	}

	/**
	 * 특정 댓글에 달린 답글 수정
	 *
	 * @param replyUpdateDTO commentId : 답글이 달린 댓글 ID, replyId : 답글 ID, username : 사용자 ID, content : 수정할 답글 내용
	 * @return 답글 수정 성공 여부 메시지 + ReplyId
	 * @throws ReplyNotFoundException 존재하지 않는 답글일 경우
	 * @throws RequestFailedException 답글 수정에 실패했을 경우
	 * @author Jihwan
	 */
	@Override
	@Transactional
	public String updateReply(ReplyUpdateDTO replyUpdateDTO) {
		try {
			Reply reply = replyRepository.findById(replyUpdateDTO.getReplyId())
				.orElseThrow(() -> new ReplyNotFoundException("존재하지 않는 답글입니다"));
			reply.updateContent(replyUpdateDTO.getContent());
			replyRepository.save(reply);
			return "답글이 수정되었습니다 : [ReplyId=" + reply.getReplyId() + "]";
		} catch (Exception e) {
			throw new RequestFailedException("[Error] 답글 수정에 실패했습니다 : " + e.getMessage());
		}
	}

	/**
	 * 특정 댓글에 달린 답글 삭제
	 *
	 * @param replyDeleteDTO commentId : 답글이 달린 댓글 ID, replyId : 삭제할 답글 ID, username : 사용자 ID
	 * @return 답글 삭제 성공 여부 메시지
	 * @throws ReplyNotFoundException 존재하지 않는 답글일 경우
	 * @throws RequestFailedException 답글 삭제에 실패했을 경우
	 * @author Jihwan
	 */
	@Override
	@Transactional
	public String deleteReply(ReplyDeleteDTO replyDeleteDTO) {
		try {
			Reply reply = replyRepository.findById(replyDeleteDTO.getReplyId())
				.orElseThrow(() -> new ReplyNotFoundException("존재하지 않는 답글입니다"));
			replyRepository.delete(reply);
			return "답글이 삭제되었습니다";
		} catch (Exception e) {
			throw new RequestFailedException("[Error] 답글 삭제에 실패했습니다 : " + e.getMessage());
		}
	}

}
