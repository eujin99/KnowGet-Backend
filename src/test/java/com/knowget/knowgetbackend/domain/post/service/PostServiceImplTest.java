package com.knowget.knowgetbackend.domain.post.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.knowget.knowgetbackend.domain.notification.service.NotificationService;
import com.knowget.knowgetbackend.domain.post.dto.PostResponseDTO;
import com.knowget.knowgetbackend.domain.post.repository.PostRepository;
import com.knowget.knowgetbackend.domain.user.repository.UserRepository;
import com.knowget.knowgetbackend.global.entity.Post;
import com.knowget.knowgetbackend.global.entity.User;
import com.knowget.knowgetbackend.global.exception.RequestFailedException;

class PostServiceImplTest {
	@Mock
	private PostRepository postRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private NotificationService notificationService;

	@InjectMocks
	private PostServiceImpl postService;

	@Mock
	private RestTemplateBuilder restTemplateBuilder;

	private RestTemplate restTemplate;

	private MockRestServiceServer mockServer;

	private Post post;
	private User user;

	@Value("${seoul.api.key}")
	private String apiKey;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		restTemplate = new RestTemplate();
		mockServer = MockRestServiceServer.createServer(restTemplate);
		when(restTemplateBuilder.build()).thenReturn(restTemplate);

		post = Post.builder()
			.joReqstNo("testReqstNo")
			.joRegistNo("testRegistNo")
			.cmpnyNm("Test Company")
			.gu("TestGu")
			.jobCode("1")
			.build();

		user = User.builder()
			.username("testuser")
			.prefLocation("TestGu")
			.prefJob("1")
			.build();
	}

	@Test
	@DisplayName("모든 게시글 조회 테스트 - getAllPosts")
	void testGetAllPosts() {
		when(postRepository.findAllByOrderByPostIdDesc()).thenReturn(List.of(post));

		List<PostResponseDTO> result = postService.getAllPosts();

		assertThat(result).hasSize(1);
		verify(postRepository, times(1)).findAllByOrderByPostIdDesc();
	}

	@Test
	@DisplayName("일자리 공고 ID로 상세 조회 테스트 - getPostById")
	void testGetPostById() {
		when(postRepository.findById(1)).thenReturn(Optional.of(post));

		PostResponseDTO result = postService.getPostById(1);

		assertThat(result.getJoRegistNo()).isEqualTo("testRegistNo");
		verify(postRepository, times(1)).findById(1);
	}

	@Test
	@DisplayName("일자리 공고 ID로 상세 조회 실패 테스트 - 존재하지 않는 게시글")
	void testGetPostByIdNotFound() {
		when(postRepository.findById(1)).thenReturn(Optional.empty());

		assertThrows(RequestFailedException.class, () -> postService.getPostById(1));
		verify(postRepository, times(1)).findById(1);
	}

	@Test
	@DisplayName("구 이름으로 게시글 조회 테스트 - getPostsByLocation")
	void testGetPostsByLocation() {
		when(postRepository.findByWorkPararBassAdresCnContainingOrderByPostIdDesc("TestGu")).thenReturn(List.of(post));

		List<PostResponseDTO> result = postService.getPostsByLocation("TestGu");

		assertThat(result).hasSize(1);
		verify(postRepository, times(1)).findByWorkPararBassAdresCnContainingOrderByPostIdDesc("TestGu");
	}

	@Test
	@DisplayName("직종 코드로 게시글 조회 테스트 - getPostsByJobCode")
	void testGetPostsByJobCode() {
		when(postRepository.findByRcritJssfcCmmnCodeSeOrderByPostIdDesc("1")).thenReturn(List.of(post));

		List<PostResponseDTO> result = postService.getPostsByJobCode("1");

		assertThat(result).hasSize(1);
		verify(postRepository, times(1)).findByRcritJssfcCmmnCodeSeOrderByPostIdDesc("1");
	}

	@Test
	@DisplayName("일자리 정보를 저장하고 관심있는 사용자에게 알림 전송 테스트 - notifyUsers")
	void testNotifyUsers() {
		when(userRepository.findByPrefLocationOrPrefJob("TestGu", "1")).thenReturn(List.of(user));

		postService.notifyUsers(post);

		verify(notificationService, times(1)).sendNotification(any(User.class), any(Post.class));
	}

	@Test
	@DisplayName("구 이름 추출 테스트 - extractGu")
	void testExtractGu() {
		String guiLn = "(월급)206만원 / 서울 강남구 / 경력 무관";

		String result = postService.extractGu(guiLn);

		assertThat(result).isEqualTo("강남구");
	}

	@Test
	@DisplayName("직종 코드 변환 테스트 - convertJobCode")
	void testConvertJobCode() {
		String code = "24001";

		String result = postService.convertJobCode(code);

		assertThat(result).isEqualTo("1");
	}
}