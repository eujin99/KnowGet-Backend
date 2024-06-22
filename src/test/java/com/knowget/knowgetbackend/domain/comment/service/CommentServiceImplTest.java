package com.knowget.knowgetbackend.domain.comment.service;

import static org.assertj.core.api.Assertions.*;
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
import com.knowget.knowgetbackend.global.exception.RequestFailedException;

class CommentServiceImplTest {
	@Mock
	private CommentRepository commentRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private SuccessCaseRepository successCaseRepository;

	@InjectMocks
	private CommentServiceImpl commentService;

	private Comment comment;
	private User user;
	private SuccessCase successCase;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		user = User.builder().username("testuser").build();
		successCase = SuccessCase.builder().user(user).title("Success Case Title").content("Content").build();
		setPrivateField(successCase, "caseId", 1);

		comment = Comment.builder().successCase(successCase).user(user).content("Test Comment").build();
		setPrivateField(comment, "commentId", 1);

		// Mock save method to set Comment ID
		when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> {
			Comment savedComment = invocation.getArgument(0);
			setPrivateField(savedComment, "commentId", 1);
			return savedComment;
		});
	}

	private void setPrivateField(Object target, String fieldName, Object value) {
		try {
			Field field = target.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(target, value);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	@DisplayName("특정 취업 성공사례 게시글에 달린 모든 댓글 조회 테스트 - findComments")
	void testFindComments() {
		when(successCaseRepository.findById(1)).thenReturn(Optional.of(successCase));
		when(commentRepository.findBySuccessCaseIdOrderByCreatedDateAsc(1)).thenReturn(List.of(comment));

		List<CommentResponseDTO> results = commentService.findComments(1);

		assertThat(results).hasSize(1);
		assertThat(results.get(0).getContent()).isEqualTo("Test Comment");
		assertThat(results.get(0).getUsername()).isEqualTo("testuser");
		assertThat(results.get(0).getCaseId()).isEqualTo(1);
		assertThat(results.get(0).getCommentId()).isEqualTo(1);
		assertThat(results.get(0).getCreatedDate()).isEqualTo(comment.getCreatedDate());
		assertThat(results.get(0).getUpdatedDate()).isEqualTo(comment.getUpdatedDate());
		verify(successCaseRepository, times(1)).findById(1);
		verify(commentRepository, times(1)).findBySuccessCaseIdOrderByCreatedDateAsc(1);
	}

	@Test
	@DisplayName("취업 성공사례 게시글에 대한 댓글 작성 테스트 - saveComment")
	void testSaveComment() {
		CommentRequestDTO requestDTO = new CommentRequestDTO(1, "testuser", "New Comment");
		when(successCaseRepository.findById(1)).thenReturn(Optional.of(successCase));
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

		String result = commentService.saveComment(requestDTO);

		assertThat(result).isEqualTo("댓글이 작성되었습니다 : [CommentId=1]");
		verify(successCaseRepository, times(1)).findById(1);
		verify(userRepository, times(1)).findByUsername("testuser");
		verify(commentRepository, times(1)).save(any(Comment.class));
	}

	@Test
	@DisplayName("취업 성공사례 게시글에 대한 댓글 작성 실패 테스트 - caseId가 없는 경우")
	void testSaveCommentCaseNotFound() {
		CommentRequestDTO requestDTO = new CommentRequestDTO(1, "testuser", "New Comment");
		when(successCaseRepository.findById(1)).thenReturn(Optional.empty());

		Throwable thrown = catchThrowable(() -> commentService.saveComment(requestDTO));
		assertThat(thrown).isInstanceOf(RequestFailedException.class)
			.hasMessageContaining("존재하지 않는 게시글입니다");

		verify(successCaseRepository, times(1)).findById(1);
		verify(userRepository, never()).findByUsername(anyString());
		verify(commentRepository, never()).save(any(Comment.class));
	}

	@Test
	@DisplayName("취업 성공사례 게시글에 대한 댓글 작성 실패 테스트 - username이 없는 경우")
	void testSaveCommentUserNotFound() {
		CommentRequestDTO requestDTO = new CommentRequestDTO(1, "testuser", "New Comment");
		when(successCaseRepository.findById(1)).thenReturn(Optional.of(successCase));
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

		Throwable thrown = catchThrowable(() -> commentService.saveComment(requestDTO));
		assertThat(thrown).isInstanceOf(RequestFailedException.class)
			.hasMessageContaining("존재하지 않는 사용자입니다");

		verify(successCaseRepository, times(1)).findById(1);
		verify(userRepository, times(1)).findByUsername("testuser");
		verify(commentRepository, never()).save(any(Comment.class));
	}

	@Test
	@DisplayName("특정 취업 성공사례 게시글에 달린 댓글 수정 테스트 - updateComment")
	void testUpdateComment() {
		CommentUpdateDTO updateDTO = new CommentUpdateDTO(1, 1, "testuser", "Updated Comment");
		when(commentRepository.findById(1)).thenReturn(Optional.of(comment));

		String result = commentService.updateComment(updateDTO);

		assertThat(result).isEqualTo("댓글이 수정되었습니다 : [CommentId=1]");
		assertThat(comment.getContent()).isEqualTo("Updated Comment");
		verify(commentRepository, times(1)).findById(1);
	}

	@Test
	@DisplayName("특정 취업 성공사례 게시글에 달린 댓글 수정 실패 테스트 - 존재하지 않는 댓글")
	void testUpdateCommentNotFound() {
		CommentUpdateDTO updateDTO = new CommentUpdateDTO(1, 1, "testuser", "Updated Comment");
		when(commentRepository.findById(1)).thenReturn(Optional.empty());

		Throwable thrown = catchThrowable(() -> commentService.updateComment(updateDTO));
		assertThat(thrown).isInstanceOf(RequestFailedException.class)
			.hasMessageContaining("존재하지 않는 댓글입니다");

		verify(commentRepository, times(1)).findById(1);
	}

	@Test
	@DisplayName("특정 취업 성공사례 게시글에 달린 댓글 삭제 테스트 - deleteComment")
	void testDeleteComment() {
		CommentDeleteDTO deleteDTO = CommentDeleteDTO.builder()
			.caseId(1)
			.commentId(1)
			.username("testuser")
			.build();
		when(commentRepository.findById(1)).thenReturn(Optional.of(comment));
		doNothing().when(commentRepository).delete(comment);

		String result = commentService.deleteComment(deleteDTO);

		assertThat(result).isEqualTo("댓글이 삭제되었습니다");
		verify(commentRepository, times(1)).findById(1);
		verify(commentRepository, times(1)).delete(comment);
	}

	@Test
	@DisplayName("존재하지 않는 댓글 삭제 시 예외 처리 - deleteComment")
	void testDeleteNonExistentComment() {
		CommentDeleteDTO deleteDTO = CommentDeleteDTO.builder()
			.caseId(1)
			.commentId(1)
			.username("testuser")
			.build();
		when(commentRepository.findById(1)).thenReturn(Optional.empty());

		Throwable thrown = catchThrowable(() -> commentService.deleteComment(deleteDTO));
		assertThat(thrown).isInstanceOf(RequestFailedException.class)
			.hasMessageContaining("존재하지 않는 댓글입니다");

		verify(commentRepository, times(1)).findById(1);
		verify(commentRepository, never()).delete(any(Comment.class));
	}
}