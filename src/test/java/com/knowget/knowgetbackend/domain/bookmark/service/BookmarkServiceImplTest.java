package com.knowget.knowgetbackend.domain.bookmark.service;

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

import com.knowget.knowgetbackend.domain.bookmark.dto.BookmarkRequestDTO;
import com.knowget.knowgetbackend.domain.bookmark.repository.BookmarkRepository;
import com.knowget.knowgetbackend.domain.post.repository.PostRepository;
import com.knowget.knowgetbackend.domain.user.repository.UserRepository;
import com.knowget.knowgetbackend.global.entity.Bookmark;
import com.knowget.knowgetbackend.global.entity.Post;
import com.knowget.knowgetbackend.global.entity.User;
import com.knowget.knowgetbackend.global.exception.RequestFailedException;

class BookmarkServiceImplTest {
	@Mock
	private BookmarkRepository bookmarkRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private PostRepository postRepository;

	@InjectMocks
	private BookmarkServiceImpl bookmarkService;

	private User user;
	private Post post;
	private Bookmark bookmark;
	private BookmarkRequestDTO bookmarkRequestDTO;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		user = User.builder()
			.username("testuser")
			.password("password")
			.prefLocation("Seoul")
			.prefJob("Developer")
			.build();

		post = Post.builder()
			.joReqstNo("req1")
			.joRegistNo("reg1")
			.cmpnyNm("Company A")
			.bsnsSumryCn("Business Summary A")
			.rcritJssfcCmmnCodeSe("code1")
			.jobcodeNm("Job Code A")
			.rcritNmprCo(1)
			.acdmcrCmmnCodeSe("code2")
			.acdmcrNm("Academic Name A")
			.emplymStleCmmnCodeSe("style1")
			.emplymStleCmmnMm("style2")
			.workPararBassAdresCn("Seoul")
			.subwayNm("Subway A")
			.dtyCn("Duty A")
			.careerCndCmmnCodeSe("career1")
			.careerCndNm("Career Name A")
			.hopeWage("Wage A")
			.retGrantsNm("Grant A")
			.workTimeNm("Work Time A")
			.workTmNm("Work TM A")
			.holidayNm("Holiday A")
			.weekWorkHr("40")
			.joFeinsrSbscrbNm("Insurance A")
			.rceptClosNm("Close A")
			.rceptMthIemNm("Method A")
			.modelMthNm("Model A")
			.rceptMthNm("Receipt A")
			.presentnPapersNm("Papers A")
			.mngrNm("Manager A")
			.mngrPhonNo("Phone A")
			.mngrInsttNm("Institution A")
			.bassAdresCn("Base Address A")
			.joSj("Job Subject A")
			.joRegDt("2023-01-01")
			.guiLn("Guide Line A")
			.gu("Gangnam")
			.jobCode("Job Code A")
			.build();

		// postId를 설정한 Post 객체를 mock을 통해 반환하도록 설정
		post = mock(Post.class);
		when(post.getPostId()).thenReturn(1);
		when(postRepository.findById(anyInt())).thenReturn(Optional.of(post));

		bookmark = Bookmark.builder()
			.post(post)
			.user(user)
			.build();

		bookmarkRequestDTO = BookmarkRequestDTO.builder()
			.postId(1)  // 직접 설정된 postId 사용
			.username(user.getUsername())
			.build();
	}

	@Test
	@DisplayName("북마크 상태 변경 테스트 - 새로운 북마크 생성")
	void testBookmarkCreate() {
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
		when(bookmarkRepository.findByPostAndUser(any(Post.class), any(User.class))).thenReturn(Optional.empty());
		when(bookmarkRepository.save(any(Bookmark.class))).thenReturn(bookmark);

		String result = bookmarkService.bookmark(bookmarkRequestDTO);

		assertThat(result).isEqualTo("북마크 상태가 변경되었습니다 : [북마크=true]");
	}

	@Test
	@DisplayName("북마크 상태 변경 테스트 - 기존 북마크 업데이트")
	void testBookmarkUpdate() {
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
		when(bookmarkRepository.findByPostAndUser(any(Post.class), any(User.class))).thenReturn(Optional.of(bookmark));

		String result = bookmarkService.bookmark(bookmarkRequestDTO);

		assertThat(result).isEqualTo("북마크 상태가 변경되었습니다 : [북마크=false]");
	}

	@Test
	@DisplayName("북마크 상태 변경 실패 테스트 - 사용자 없음")
	void testBookmarkUserNotFound() {
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

		Throwable thrown = catchThrowable(() -> bookmarkService.bookmark(bookmarkRequestDTO));

		assertThat(thrown).isInstanceOf(RequestFailedException.class)
			.hasMessageContaining("존재하지 않는 사용자입니다");
	}

	@Test
	@DisplayName("북마크 상태 변경 실패 테스트 - 공고 없음")
	void testBookmarkPostNotFound() {
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
		when(postRepository.findById(anyInt())).thenReturn(Optional.empty());

		Throwable thrown = catchThrowable(() -> bookmarkService.bookmark(bookmarkRequestDTO));

		assertThat(thrown).isInstanceOf(RequestFailedException.class)
			.hasMessageContaining("존재하지 않는 공고입니다");
	}

	@Test
	@DisplayName("북마크 여부 확인 테스트")
	void testIsBookmarked() {
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
		when(bookmarkRepository.findByPostAndUser(any(Post.class), any(User.class))).thenReturn(Optional.of(bookmark));

		Boolean result = bookmarkService.isBookmarked(user.getUsername(), post.getPostId());

		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("북마크 여부 확인 실패 테스트 - 사용자 없음")
	void testIsBookmarkedUserNotFound() {
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

		Throwable thrown = catchThrowable(() -> bookmarkService.isBookmarked(user.getUsername(), post.getPostId()));

		assertThat(thrown).isInstanceOf(RequestFailedException.class)
			.hasMessageContaining("존재하지 않는 사용자입니다");
	}

	@Test
	@DisplayName("북마크 여부 확인 실패 테스트 - 공고 없음")
	void testIsBookmarkedPostNotFound() {
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
		when(postRepository.findById(anyInt())).thenReturn(Optional.empty());

		Throwable thrown = catchThrowable(() -> bookmarkService.isBookmarked(user.getUsername(), post.getPostId()));

		assertThat(thrown).isInstanceOf(RequestFailedException.class)
			.hasMessageContaining("존재하지 않는 공고입니다");
	}
}