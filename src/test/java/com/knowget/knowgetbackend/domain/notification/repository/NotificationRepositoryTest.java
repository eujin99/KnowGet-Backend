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

	private User user1;
	private User user2;
	private Post post;
	private Notification notification1;
	private Notification notification2;

	@BeforeEach
	public void setUp() {
		user1 = User.builder()
			.username("user1")
			.password("password")
			.prefLocation("Seoul")
			.prefJob("Engineer")
			.build();

		user2 = User.builder()
			.username("user2")
			.password("password")
			.prefLocation("Busan")
			.prefJob("Doctor")
			.build();

		post = Post.builder()
			.joReqstNo("req1")
			.joRegistNo("reg1")
			.cmpnyNm("Company")
			.bsnsSumryCn("Business Summary")
			.rcritJssfcCmmnCodeSe("code1")
			.jobcodeNm("Job Code")
			.rcritNmprCo(1)
			.acdmcrCmmnCodeSe("code2")
			.acdmcrNm("Academic Name")
			.emplymStleCmmnCodeSe("style1")
			.emplymStleCmmnMm("style2")
			.workPararBassAdresCn("Address")
			.subwayNm("Subway")
			.dtyCn("Duty")
			.careerCndCmmnCodeSe("career1")
			.careerCndNm("Career Name")
			.hopeWage("Wage")
			.retGrantsNm("Grant")
			.workTimeNm("Work Time")
			.workTmNm("Work TM")
			.holidayNm("Holiday")
			.weekWorkHr("40")
			.joFeinsrSbscrbNm("Insurance")
			.rceptClosNm("Close")
			.rceptMthIemNm("Method")
			.modelMthNm("Model")
			.rceptMthNm("Receipt")
			.presentnPapersNm("Papers")
			.mngrNm("Manager")
			.mngrPhonNo("Phone")
			.mngrInsttNm("Institution")
			.bassAdresCn("Base Address")
			.joSj("Job Subject")
			.joRegDt("2023-01-01")
			.guiLn("Guide Line")
			.gu("Gu")
			.jobCode("Job Code")
			.build();

		notification1 = Notification.builder()
			.user(user1)
			.post(post)
			.content("Notification Content 1")
			.build();

		notification2 = Notification.builder()
			.user(user1)
			.post(post)
			.content("Notification Content 2")
			.build();

		entityManager.persist(user1);
		entityManager.persist(user2);
		entityManager.persist(post);
		entityManager.persist(notification1);
		entityManager.persist(notification2);
		entityManager.flush();
	}

	@Test
	@DisplayName("특정 사용자의 알림 조회 테스트")
	public void testFindByUser() {
		// When
		List<Notification> notifications = notificationRepository.findByUser(user1);

		// Then
		assertThat(notifications).hasSize(2);
		assertThat(notifications).contains(notification1, notification2);
	}

	@Test
	@DisplayName("특정 사용자의 읽지 않은 알림 개수 조회 테스트 - Username")
	public void testCountUnreadNotificationsByUsername() {
		// When
		Integer unreadCount = notificationRepository.countUnreadNotificationsByUsername("user1");

		// Then
		assertThat(unreadCount).isEqualTo(2);
	}

	@Test
	@DisplayName("특정 사용자의 읽지 않은 알림 개수 조회 테스트 - User Entity")
	public void testCountByUserAndIsReadIsFalse() {
		// When
		Integer unreadCount = notificationRepository.countByUserAndIsReadIsFalse(user1);

		// Then
		assertThat(unreadCount).isEqualTo(2);
	}
}