package com.knowget.knowgetbackend.domain.reply.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
import com.knowget.knowgetbackend.global.exception.RequestFailedException;

class ReplyServiceImplTest {
	@Mock
	private ReplyRepository replyRepository;

	@Mock
	private CommentRepository commentRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private ReplyServiceImpl replyService;

	private Reply reply;
	private Comment comment;
	private User user;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		user = User.builder().username("testuser").build();
		comment = Comment.builder().build();
		reply = Reply.builder().comment(comment).user(user).content("Test Reply").build();
		setReplyId(reply, 1);  // Reflection을 사용하여 replyId를 설정
	}

	@Test
	@DisplayName("특정 댓글에 답글 작성 테스트 - saveReply")
	void testSaveReply() {
		when(commentRepository.findById(1)).thenReturn(Optional.of(comment));
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
		when(replyRepository.save(any(Reply.class))).thenAnswer(invocation -> {
			Reply savedReply = invocation.getArgument(0);
			setReplyId(savedReply, 1);  // Reflection을 사용하여 replyId를 설정
			return savedReply;
		});

		ReplyRequestDTO requestDTO = new ReplyRequestDTO(1, "testuser", "New Reply");
		String result = replyService.saveReply(requestDTO);

		assertThat(result).isEqualTo("답글이 작성되었습니다 : [ReplyId=1]");
		verify(commentRepository, times(1)).findById(1);
		verify(userRepository, times(1)).findByUsername("testuser");
		verify(replyRepository, times(1)).save(any(Reply.class));
	}

	@Test
	@DisplayName("특정 댓글에 답글 작성 실패 테스트 - 존재하지 않는 댓글")
	void testSaveReplyCommentNotFound() {
		when(commentRepository.findById(1)).thenReturn(Optional.empty());

		ReplyRequestDTO requestDTO = new ReplyRequestDTO(1, "testuser", "New Reply");

		assertThrows(RequestFailedException.class, () -> replyService.saveReply(requestDTO));
		verify(commentRepository, times(1)).findById(1);
	}

	@Test
	@DisplayName("특정 댓글에 답글 작성 실패 테스트 - 존재하지 않는 사용자")
	void testSaveReplyUserNotFound() {
		when(commentRepository.findById(1)).thenReturn(Optional.of(comment));
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

		ReplyRequestDTO requestDTO = new ReplyRequestDTO(1, "testuser", "New Reply");

		assertThrows(RequestFailedException.class, () -> replyService.saveReply(requestDTO));
		verify(commentRepository, times(1)).findById(1);
		verify(userRepository, times(1)).findByUsername("testuser");
	}

	@Test
	@DisplayName("특정 댓글에 달린 답글 수정 테스트 - updateReply")
	void testUpdateReply() {
		when(replyRepository.findById(1)).thenReturn(Optional.of(reply));

		ReplyUpdateDTO updateDTO = new ReplyUpdateDTO(1, 1, "testuser", "Updated Reply");
		String result = replyService.updateReply(updateDTO);

		assertThat(result).isEqualTo("답글이 수정되었습니다 : [ReplyId=1]");
		verify(replyRepository, times(1)).findById(1);
	}

	@Test
	@DisplayName("특정 댓글에 달린 답글 수정 실패 테스트 - 존재하지 않는 답글")
	void testUpdateReplyNotFound() {
		when(replyRepository.findById(1)).thenReturn(Optional.empty());

		ReplyUpdateDTO updateDTO = new ReplyUpdateDTO(1, 1, "testuser", "Updated Reply");

		assertThrows(RequestFailedException.class, () -> replyService.updateReply(updateDTO));
		verify(replyRepository, times(1)).findById(1);
	}

	@Test
	@DisplayName("특정 댓글에 달린 답글 삭제 테스트 - deleteReply")
	void testDeleteReply() {
		when(replyRepository.findById(1)).thenReturn(Optional.of(reply));

		ReplyDeleteDTO deleteDTO = new ReplyDeleteDTO(1, 1, "testuser");
		String result = replyService.deleteReply(deleteDTO);

		assertThat(result).isEqualTo("답글이 삭제되었습니다");
		verify(replyRepository, times(1)).findById(1);
		verify(replyRepository, times(1)).delete(reply);
	}

	@Test
	@DisplayName("특정 댓글에 달린 답글 삭제 실패 테스트 - 존재하지 않는 답글")
	void testDeleteReplyNotFound() {
		when(replyRepository.findById(1)).thenReturn(Optional.empty());

		ReplyDeleteDTO deleteDTO = new ReplyDeleteDTO(1, 1, "testuser");

		assertThrows(RequestFailedException.class, () -> replyService.deleteReply(deleteDTO));
		verify(replyRepository, times(1)).findById(1);
	}

	@Test
	@DisplayName("특정 댓글에 달린 모든 답글 조회 테스트 - findReplies")
	void testFindReplies() {
		when(commentRepository.findById(1)).thenReturn(Optional.of(comment));
		when(replyRepository.findAllByCommentIdOrderByCreatedDateAsc(1)).thenReturn(List.of(reply));

		List<ReplyResponseDTO> replies = replyService.findReplies(1);

		assertThat(replies).hasSize(1);
		verify(commentRepository, times(1)).findById(1);
		verify(replyRepository, times(1)).findAllByCommentIdOrderByCreatedDateAsc(1);
	}

	@Test
	@DisplayName("특정 댓글에 달린 모든 답글 조회 실패 테스트 - 존재하지 않는 댓글")
	void testFindRepliesCommentNotFound() {
		when(commentRepository.findById(1)).thenReturn(Optional.empty());

		assertThrows(CommentNotFoundException.class, () -> replyService.findReplies(1));
		verify(commentRepository, times(1)).findById(1);
	}

	// Reflection을 사용하여 private 필드 설정하는 유틸리티 메서드
	private void setReplyId(Reply reply, Integer replyId) {
		try {
			Field field = Reply.class.getDeclaredField("replyId");
			field.setAccessible(true);
			field.set(reply, replyId);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}