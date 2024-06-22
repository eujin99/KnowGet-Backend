package com.knowget.knowgetbackend.global.entity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NotificationTest {
	private Notification notification;
	private User user;
	private Post post;

	@BeforeEach
	public void setUp() {
		user = User.builder()
			.username("testuser")
			.password("password")
			.prefLocation("Seoul")
			.prefJob("Engineer")
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
			.content("This is a notification content.")
			.build();
	}

	@Test
	@DisplayName("Notification 엔티티 생성 테스트")
	public void testCreateNotification() {
		// Given
		User user = this.user;
		Post post = this.post;
		String content = "This is a notification content.";

		// When
		Notification notification = Notification.builder()
			.user(user)
			.post(post)
			.content(content)
			.build();

		// Then
		assertThat(notification.getUser()).isEqualTo(user);
		assertThat(notification.getPost()).isEqualTo(post);
		assertThat(notification.getContent()).isEqualTo(content);
		assertThat(notification.getIsRead()).isFalse();
	}

	@Test
	@DisplayName("Notification 엔티티 기본 생성자 테스트")
	public void testDefaultConstructor() {
		// When
		Notification notification = new Notification();

		// Then
		assertThat(notification).isNotNull();
	}

	@Test
	@DisplayName("Notification 엔티티 읽음 상태 업데이트 테스트")
	public void testUpdateIsRead() {
		// When
		notification.updateIsRead();

		// Then
		assertThat(notification.getIsRead()).isTrue();
	}
}