package com.knowget.knowgetbackend.global.entity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BookmarkTest {
	private Bookmark bookmark;
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

		bookmark = Bookmark.builder()
			.user(user)
			.post(post)
			.build();
	}

	@Test
	@DisplayName("Bookmark 엔티티 생성 테스트")
	public void testCreateBookmark() {
		// Given
		User user = this.user;
		Post post = this.post;

		// When
		Bookmark bookmark = Bookmark.builder()
			.user(user)
			.post(post)
			.build();

		// Then
		assertThat(bookmark.getUser()).isEqualTo(user);
		assertThat(bookmark.getPost()).isEqualTo(post);
		assertThat(bookmark.getIsBookmarked()).isTrue();
	}

	@Test
	@DisplayName("Bookmark 엔티티 기본 생성자 테스트")
	public void testDefaultConstructor() {
		// When
		Bookmark bookmark = new Bookmark();

		// Then
		assertThat(bookmark).isNotNull();
	}

	@Test
	@DisplayName("Bookmark 엔티티 북마크 상태 업데이트 테스트")
	public void testUpdateBookmark() {
		// Given
		Boolean initialStatus = bookmark.getIsBookmarked();

		// When
		bookmark.updateBookmark();

		// Then
		assertThat(bookmark.getIsBookmarked()).isEqualTo(!initialStatus);
	}
}