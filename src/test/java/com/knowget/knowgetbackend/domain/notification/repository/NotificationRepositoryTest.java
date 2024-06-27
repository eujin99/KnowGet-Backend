package com.knowget.knowgetbackend.domain.notification.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.knowget.knowgetbackend.domain.post.repository.PostRepository;
import com.knowget.knowgetbackend.domain.user.repository.UserRepository;
import com.knowget.knowgetbackend.global.entity.Notification;
import com.knowget.knowgetbackend.global.entity.Post;
import com.knowget.knowgetbackend.global.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class NotificationRepositoryTest {
	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PostRepository postRepository;

	private User user;
	private Post post;

	@BeforeEach
	void setUp() {
		user = userRepository.save(User.builder()
			.username("testuser")
			.password("password")
			.prefLocation("Seoul")
			.prefJob("Engineer")
			.role("USER")
			.build());

		post = postRepository.save(Post.builder()
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
			.build());

		notificationRepository.save(Notification.builder()
			.user(user)
			.post(post)
			.content("Test Notification 1")
			.build());

		Notification readNotification = Notification.builder()
			.user(user)
			.post(post)
			.content("Test Notification 2")
			.build();
		readNotification.updateIsRead();
		notificationRepository.save(readNotification);
	}

	@Test
	@DisplayName("사용자에 대한 알림을 최신순으로 조회")
	void testFindByUserOrderBySentDateDesc() {
		List<Notification> notifications = notificationRepository.findByUserOrderBySentDateDesc(user);

		assertThat(notifications).hasSize(2);
		assertThat(notifications.get(0).getContent()).isEqualTo("Test Notification 2");
		assertThat(notifications.get(1).getContent()).isEqualTo("Test Notification 1");
	}

	@Test
	@DisplayName("사용자의 읽지 않은 알림 개수 조회 - countUnreadNotificationsByUsername")
	void testCountUnreadNotificationsByUsername() {
		Integer unreadCount = notificationRepository.countUnreadNotificationsByUsername(user.getUsername());

		assertThat(unreadCount).isEqualTo(1);
	}

	@Test
	@DisplayName("사용자의 읽지 않은 알림 개수 조회 - countByUserAndIsReadIsFalse")
	void testCountByUserAndIsReadIsFalse() {
		Integer unreadCount = notificationRepository.countByUserAndIsReadIsFalse(user);

		assertThat(unreadCount).isEqualTo(1);
	}
}