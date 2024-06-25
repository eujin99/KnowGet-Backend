package com.knowget.knowgetbackend.domain.notification.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.*;

import java.util.Arrays;
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
			.password("password")
			.prefLocation("Seoul")
			.prefJob("Engineer")
			.role("USER")
			.build();

		post = Post.builder()
			.joReqstNo("REQ123")
			.joRegistNo("REG123")
			.cmpnyNm("CompanyName")
			.bsnsSumryCn("Business Summary")
			.rcritJssfcCmmnCodeSe("RCR123")
			.jobcodeNm("JobCode")
			.rcritNmprCo(10)
			.acdmcrCmmnCodeSe("ACD123")
			.acdmcrNm("Academic Name")
			.emplymStleCmmnCodeSe("EMP123")
			.emplymStleCmmnMm("EMP MM")
			.workPararBassAdresCn("Address")
			.subwayNm("SubwayName")
			.dtyCn("Duty Content")
			.careerCndCmmnCodeSe("CAR123")
			.careerCndNm("Career Name")
			.hopeWage("1000000")
			.retGrantsNm("Retirement Grants")
			.workTimeNm("Work Time")
			.workTmNm("Work TM")
			.holidayNm("Holiday")
			.weekWorkHr("40")
			.joFeinsrSbscrbNm("Insurance")
			.rceptClosNm("Receipt Close")
			.rceptMthIemNm("Receipt Method Item")
			.modelMthNm("Model Method")
			.rceptMthNm("Receipt Method")
			.presentnPapersNm("Presentation Papers")
			.mngrNm("Manager Name")
			.mngrPhonNo("Manager Phone")
			.mngrInsttNm("Manager Institute")
			.bassAdresCn("Base Address")
			.joSj("Job Subject")
			.joRegDt("2023-01-01")
			.guiLn("Guide Line")
			.gu("GU")
			.jobCode("Job Code")
			.build();

		notification = Notification.builder()
			.user(user)
			.post(post)
			.content("Test Notification")
			.build();
	}

	@Test
	@DisplayName("사용자에게 알림을 전송")
	void testSendNotification() {
		when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

		notificationService.sendNotification(user, post);

		verify(notificationRepository, times(1)).save(any(Notification.class));
	}

	@Test
	@DisplayName("읽지 않은 알림 개수 조회 - 성공 시나리오")
	void testGetUnreadNotificationCount() {
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
		when(notificationRepository.countByUserAndIsReadIsFalse(any(User.class))).thenReturn(5);

		Integer unreadCount = notificationService.getUnreadNotificationCount("testuser");

		assertThat(unreadCount).isEqualTo(5);
	}

	@Test
	@DisplayName("읽지 않은 알림 개수 조회 - 실패 시나리오")
	void testGetUnreadNotificationCountFailure() {
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

		assertThrows(RequestFailedException.class, () -> {
			notificationService.getUnreadNotificationCount("nonexistentuser");
		});
	}

	@Test
	@DisplayName("사용자의 모든 알림 조회 - 성공 시나리오")
	void testGetNotifications() {
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
		when(notificationRepository.findByUserOrderBySentDateDesc(any(User.class)))
			.thenReturn(Arrays.asList(notification));

		List<NotificationResponseDTO> notifications = notificationService.getNotifications("testuser");

		assertThat(notifications).hasSize(1);
		assertThat(notifications.get(0).getContent()).isEqualTo("Test Notification");
	}

	@Test
	@DisplayName("사용자의 모든 알림 조회 - 실패 시나리오")
	void testGetNotificationsFailure() {
		when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

		assertThrows(RequestFailedException.class, () -> {
			notificationService.getNotifications("nonexistentuser");
		});
	}

	@Test
	@DisplayName("알림을 읽음 처리 - 성공 시나리오")
	void testMarkAsRead() {
		when(notificationRepository.findById(anyLong())).thenReturn(Optional.of(notification));
		when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

		String result = notificationService.markAsRead(1L);

		assertThat(result).isEqualTo("알림을 읽음으로 변경하였습니다");
		assertThat(notification.getIsRead()).isTrue();
	}

	@Test
	@DisplayName("알림을 읽음 처리 - 실패 시나리오")
	void testMarkAsReadFailure() {
		when(notificationRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(RequestFailedException.class, () -> {
			notificationService.markAsRead(1L);
		});
	}

	@Test
	@DisplayName("알림 삭제 - 성공 시나리오")
	void testDeleteNotification() {
		when(notificationRepository.findById(anyLong())).thenReturn(Optional.of(notification));

		String result = notificationService.deleteNotification(1L);

		assertThat(result).isEqualTo("알림을 삭제하였습니다");
		verify(notificationRepository, times(1)).delete(any(Notification.class));
	}

	@Test
	@DisplayName("알림 삭제 - 실패 시나리오")
	void testDeleteNotificationFailure() {
		when(notificationRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(RequestFailedException.class, () -> {
			notificationService.deleteNotification(1L);
		});
	}
}