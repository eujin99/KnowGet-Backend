package com.knowget.knowgetbackend.domain.notification.service;

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

import com.knowget.knowgetbackend.domain.notification.dto.NotificationResponseDTO;
import com.knowget.knowgetbackend.domain.notification.repository.NotificationRepository;
import com.knowget.knowgetbackend.domain.user.repository.UserRepository;
import com.knowget.knowgetbackend.global.entity.Notification;
import com.knowget.knowgetbackend.global.entity.Post;
import com.knowget.knowgetbackend.global.entity.User;
import com.knowget.knowgetbackend.global.exception.RequestFailedException;

class NotificationServiceImplTest {
	@Mock
	private NotificationRepository notificationRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private NotificationServiceImpl notificationService;

	private User user;
	private Post post;
	private Notification notification;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		user = User.builder()
			.username("testuser")
			.build();
		post = Post.builder()
			.joSj("Test Job")
			.workPararBassAdresCn("Test Address")
			.jobcodeNm("Test Job Code")
			.build();
		notification = Notification.builder()
			.user(user)
			.post(post)
			.content("Test Notification")
			.build();
	}

	@Test
	@DisplayName("알림 전송 테스트 - sendNotification")
	void testSendNotification() {
		when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

		notificationService.sendNotification(user, post);

		verify(notificationRepository, times(1)).save(any(Notification.class));
	}

	@Test
	@DisplayName("읽지 않은 알림 개수 조회 테스트 - getUnreadNotificationCount")
	void testGetUnreadNotificationCount() {
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
		when(notificationRepository.countByUserAndIsReadIsFalse(any(User.class))).thenReturn(5);

		Integer unreadCount = notificationService.getUnreadNotificationCount("testuser");

		assertThat(unreadCount).isEqualTo(5);
		verify(userRepository, times(1)).findByUsername(anyString());
		verify(notificationRepository, times(1)).countByUserAndIsReadIsFalse(any(User.class));
	}

	@Test
	@DisplayName("읽지 않은 알림 개수 조회 실패 테스트 - 사용자 없음")
	void testGetUnreadNotificationCountUserNotFound() {
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

		assertThrows(RequestFailedException.class, () -> notificationService.getUnreadNotificationCount("unknownuser"));
		verify(userRepository, times(1)).findByUsername(anyString());
	}

	@Test
	@DisplayName("모든 알림 조회 테스트 - getNotifications")
	void testGetNotifications() {
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
		when(notificationRepository.findByUser(any(User.class))).thenReturn(List.of(notification));

		List<NotificationResponseDTO> notifications = notificationService.getNotifications("testuser");

		assertThat(notifications).hasSize(1);
		verify(userRepository, times(1)).findByUsername(anyString());
		verify(notificationRepository, times(1)).findByUser(any(User.class));
	}

	@Test
	@DisplayName("모든 알림 조회 실패 테스트 - 알림 없음")
	void testGetNotificationsNotFound() {
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
		when(notificationRepository.findByUser(any(User.class))).thenReturn(List.of());

		assertThrows(RequestFailedException.class, () -> notificationService.getNotifications("testuser"));
		verify(userRepository, times(1)).findByUsername(anyString());
		verify(notificationRepository, times(1)).findByUser(any(User.class));
	}

	@Test
	@DisplayName("알림 읽음 처리 테스트 - markAsRead")
	void testMarkAsRead() {
		when(notificationRepository.findById(anyLong())).thenReturn(Optional.of(notification));

		String result = notificationService.markAsRead(1L);

		assertThat(result).isEqualTo("알림을 읽음으로 변경하였습니다");
		verify(notificationRepository, times(1)).findById(anyLong());
		verify(notificationRepository, times(1)).save(any(Notification.class));
	}

	@Test
	@DisplayName("알림 읽음 처리 실패 테스트 - 알림 없음")
	void testMarkAsReadNotFound() {
		when(notificationRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(RequestFailedException.class, () -> notificationService.markAsRead(1L));
		verify(notificationRepository, times(1)).findById(anyLong());
	}

	@Test
	@DisplayName("알림 삭제 테스트 - deleteNotification")
	void testDeleteNotification() {
		when(notificationRepository.findById(anyLong())).thenReturn(Optional.of(notification));

		String result = notificationService.deleteNotification(1L);

		assertThat(result).isEqualTo("알림을 삭제하였습니다");
		verify(notificationRepository, times(1)).findById(anyLong());
		verify(notificationRepository, times(1)).delete(any(Notification.class));
	}

	@Test
	@DisplayName("알림 삭제 실패 테스트 - 알림 없음")
	void testDeleteNotificationNotFound() {
		when(notificationRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(RequestFailedException.class, () -> notificationService.deleteNotification(1L));
		verify(notificationRepository, times(1)).findById(anyLong());
	}
}