package com.knowget.knowgetbackend.domain.notification.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.knowget.knowgetbackend.global.entity.Notification;
import com.knowget.knowgetbackend.global.entity.Post;
import com.knowget.knowgetbackend.global.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class NotificationRepositoryTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private NotificationRepository notificationRepository;

	private User user;
	private Post post;
	private Notification notification1;
	private Notification notification2;

	@BeforeEach
	public void setUp() {
		user = User.builder()
			.username("testuser")
			.password("password")
			.prefLocation("Seoul")
			.prefJob("Engineer")
			.role("USER")
			.build();
		entityManager.persist(user);

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
		entityManager.persist(post);

		notification1 = Notification.builder()
			.user(user)
			.post(post)
			.content("Test notification 1")
			.build();

		notification2 = Notification.builder()
			.user(user)
			.post(post)
			.content("Test notification 2")
			.build();

		notification2.updateIsRead();

		entityManager.persist(notification1);
		entityManager.persist(notification2);
		entityManager.flush();
	}

	@Test
	@DisplayName("특정 사용자에 대한 알림 조회 테스트")
	public void testFindByUser() {
		// When
		List<Notification> notifications = notificationRepository.findByUser(user);

		// Then
		assertThat(notifications).hasSize(2);
		assertThat(notifications).extracting(Notification::getContent)
			.containsExactlyInAnyOrder("Test notification 1", "Test notification 2");
	}

	@Test
	@DisplayName("특정 사용자에 대한 읽지 않은 알림 수 조회 테스트")
	public void testCountUnreadNotificationsByUsername() {
		// When
		Integer unreadCount = notificationRepository.countUnreadNotificationsByUsername(user.getUsername());

		// Then
		assertThat(unreadCount).isEqualTo(1);
	}

	@Test
	@DisplayName("특정 사용자에 대한 읽지 않은 알림 수 조회 테스트 - countByUserAndIsReadIsFalse")
	public void testCountByUserAndIsReadIsFalse() {
		// When
		Integer unreadCount = notificationRepository.countByUserAndIsReadIsFalse(user);

		// Then
		assertThat(unreadCount).isEqualTo(1);
	}

	@Test
	@DisplayName("알림 저장 및 조회 테스트")
	public void testSaveAndFindById() {
		// Given
		Notification notification = Notification.builder()
			.user(user)
			.post(post)
			.content("New notification")
			.build();
		notification = notificationRepository.save(notification);

		// When
		Notification foundNotification = notificationRepository.findById(notification.getNotificationId()).orElse(null);

		// Then
		assertThat(foundNotification).isNotNull();
		assertThat(foundNotification.getContent()).isEqualTo("New notification");
	}

	@Test
	@DisplayName("알림 삭제 테스트")
	public void testDeleteNotification() {
		// Given
		Notification notification = Notification.builder()
			.user(user)
			.post(post)
			.content("Notification to delete")
			.build();
		notification = notificationRepository.save(notification);

		// When
		notificationRepository.delete(notification);
		Notification foundNotification = notificationRepository.findById(notification.getNotificationId()).orElse(null);

		// Then
		assertThat(foundNotification).isNull();
	}
}