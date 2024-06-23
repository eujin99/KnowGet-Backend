package com.knowget.knowgetbackend.domain.answer.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.knowget.knowgetbackend.domain.answer.dto.AnswerModfiyDTO;
import com.knowget.knowgetbackend.domain.answer.dto.AnswerRequestDTO;
import com.knowget.knowgetbackend.domain.answer.dto.AnswerResponseDTO;
import com.knowget.knowgetbackend.domain.answer.repository.AnswerRepository;
import com.knowget.knowgetbackend.domain.counseling.repository.CounselingRepository;
import com.knowget.knowgetbackend.domain.user.repository.UserRepository;
import com.knowget.knowgetbackend.global.entity.Answer;
import com.knowget.knowgetbackend.global.entity.Counseling;
import com.knowget.knowgetbackend.global.entity.User;
import com.knowget.knowgetbackend.global.exception.AnswerNotFoundException;
import com.knowget.knowgetbackend.global.exception.CounselingNotFoundException;

class AnswerServiceImplTest {
	@Mock
	private AnswerRepository answerRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private CounselingRepository counselingRepository;

	@InjectMocks
	private AnswerServiceImpl answerService;

	private Answer answer;
	private Counseling counseling;
	private User admin;
	private User user;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		user = User.builder()
			.username("user")
			.password("password")
			.prefLocation("Seoul")
			.prefJob("Developer")
			.build();

		admin = User.builder()
			.username("admin")
			.password("password")
			.prefLocation("NULL")
			.prefJob("NULL")
			.role("ADMIN")
			.build();

		counseling = Counseling.builder()
			.user(user)
			.category("Career")
			.content("This is a test counseling content.")
			.build();

		answer = Answer.builder()
			.counseling(counseling)
			.content("This is a test answer.")
			.build();
	}

	@Test
	@DisplayName("답변 상세 조회 테스트 - 성공")
	void testGetAnswer() {
		when(answerRepository.findById(anyInt())).thenReturn(Optional.of(answer));

		AnswerResponseDTO responseDTO = answerService.getAnswer(1);

		assertThat(responseDTO).isNotNull();
		assertThat(responseDTO.getContent()).isEqualTo("This is a test answer.");
		assertThat(responseDTO.getCounseling()).isEqualTo("This is a test counseling content.");
		verify(answerRepository, times(1)).findById(anyInt());
	}

	@Test
	@DisplayName("답변 상세 조회 테스트 - 실패 (답변 없음)")
	void testGetAnswerNotFound() {
		when(answerRepository.findById(anyInt())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> answerService.getAnswer(1))
			.isInstanceOf(AnswerNotFoundException.class)
			.hasMessageContaining("존재하지않는 답변입니다.");
		verify(answerRepository, times(1)).findById(anyInt());
	}

	@Test
	@DisplayName("답변 작성 테스트 - 성공")
	void testSaveAnswer() {
		when(counselingRepository.findById(anyInt())).thenReturn(Optional.of(counseling));
		when(answerRepository.save(any(Answer.class))).thenReturn(answer);

		AnswerRequestDTO requestDTO = new AnswerRequestDTO();
		requestDTO.setCounselingId(1);
		requestDTO.setContent("This is a test answer.");

		String result = answerService.saveAnswer(requestDTO);

		assertThat(result).isEqualTo("답변이 저장되었습니다.");
		verify(counselingRepository, times(1)).findById(anyInt());
		verify(answerRepository, times(1)).save(any(Answer.class));
	}

	@Test
	@DisplayName("답변 작성 테스트 - 실패 (상담 없음)")
	void testSaveAnswerCounselingNotFound() {
		when(counselingRepository.findById(anyInt())).thenReturn(Optional.empty());

		AnswerRequestDTO requestDTO = new AnswerRequestDTO();
		requestDTO.setCounselingId(1);
		requestDTO.setContent("This is a test answer.");

		assertThatThrownBy(() -> answerService.saveAnswer(requestDTO))
			.isInstanceOf(CounselingNotFoundException.class)
			.hasMessageContaining("해당 상담을 찾을 수 없습니다.");
		verify(counselingRepository, times(1)).findById(anyInt());
		verify(userRepository, never()).findByUsername(anyString());
		verify(answerRepository, never()).save(any(Answer.class));
	}

	@Test
	@DisplayName("답변 수정 테스트 - 성공")
	void testUpdateAnswer() {
		when(answerRepository.findById(anyInt())).thenReturn(Optional.of(answer));
		when(answerRepository.save(any(Answer.class))).thenReturn(answer);

		AnswerModfiyDTO modfiyDTO = new AnswerModfiyDTO();
		modfiyDTO.setContent("Updated test answer.");

		String result = answerService.updateAnswer(1, modfiyDTO);

		assertThat(result).isEqualTo("답변이 수정되었습니다.");
		verify(answerRepository, times(1)).findById(anyInt());
		verify(answerRepository, times(1)).save(any(Answer.class));
	}

	@Test
	@DisplayName("답변 수정 테스트 - 실패 (답변 없음)")
	void testUpdateAnswerNotFound() {
		when(answerRepository.findById(anyInt())).thenReturn(Optional.empty());

		AnswerModfiyDTO modfiyDTO = new AnswerModfiyDTO();
		modfiyDTO.setContent("Updated test answer.");

		assertThatThrownBy(() -> answerService.updateAnswer(1, modfiyDTO))
			.isInstanceOf(AnswerNotFoundException.class)
			.hasMessageContaining("존재하지않는 답변입니다.");
		verify(answerRepository, times(1)).findById(anyInt());
		verify(answerRepository, never()).save(any(Answer.class));
	}

	@Test
	@DisplayName("답변 삭제 테스트 - 성공")
	void testDeleteAnswer() {
		when(answerRepository.findById(anyInt())).thenReturn(Optional.of(answer));

		String result = answerService.deleteAnswer(1);

		assertThat(result).isEqualTo("답변이 삭제되었습니다.");
		verify(answerRepository, times(1)).findById(anyInt());
		verify(answerRepository, times(1)).delete(any(Answer.class));

	}

	@Test
	@DisplayName("답변 삭제 테스트 - 실패 (답변 없음)")
	void testDeleteAnswerNotFound() {
		when(answerRepository.findById(anyInt())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> answerService.deleteAnswer(1))
			.isInstanceOf(AnswerNotFoundException.class)
			.hasMessageContaining("존재하지않는 답변입니다.");
		verify(answerRepository, times(1)).findById(anyInt());
		verify(answerRepository, never()).delete(any(Answer.class));
		verify(counselingRepository, never()).findById(anyInt());
	}
}