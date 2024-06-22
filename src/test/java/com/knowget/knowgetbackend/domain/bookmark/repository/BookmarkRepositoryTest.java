package com.knowget.knowgetbackend.domain.bookmark.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.knowget.knowgetbackend.global.entity.Bookmark;
import com.knowget.knowgetbackend.global.entity.Post;
import com.knowget.knowgetbackend.global.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class BookmarkRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private BookmarkRepository bookmarkRepository;

	private User user;
	private Post post;
	private Bookmark bookmark;

	@BeforeEach
	public void setUp() {
		user = User.builder()
			.username("testuser")
			.password("password")
			.prefLocation("Seoul")
			.prefJob("Engineer")
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

		bookmark = Bookmark.builder()
			.user(user)
			.post(post)
			.build();
		entityManager.persist(bookmark);
		entityManager.flush();
	}

	@Test
	@DisplayName("Bookmark 엔티티 저장 및 조회 테스트 - findByPostAndUser")
	public void testFindByPostAndUser() {
		// When
		Optional<Bookmark> foundBookmark = bookmarkRepository.findByPostAndUser(post, user);

		// Then
		assertThat(foundBookmark).isPresent();
		assertThat(foundBookmark.get().getUser()).isEqualTo(user);
		assertThat(foundBookmark.get().getPost()).isEqualTo(post);
		assertThat(foundBookmark.get().getIsBookmarked()).isTrue();
	}

	@Test
	@DisplayName("특정 사용자의 Bookmark 조회 테스트 - findByUser")
	public void testFindByUser() {
		// When
		List<Bookmark> bookmarks = bookmarkRepository.findByUser(user);

		// Then
		assertThat(bookmarks).hasSize(1);
		assertThat(bookmarks.get(0).getUser()).isEqualTo(user);
		assertThat(bookmarks.get(0).getPost()).isEqualTo(post);
	}

	@Test
	@DisplayName("존재하지 않는 Bookmark 조회 테스트 - findByPostAndUser")
	public void testFindByPostAndUserNotFound() {
		// Given
		User newUser = User.builder()
			.username("newuser")
			.password("password")
			.prefLocation("Seoul")
			.prefJob("Engineer")
			.build();
		entityManager.persist(newUser);
		entityManager.flush();

		Post newPost = Post.builder()
			.joReqstNo("REQ456")
			.joRegistNo("REG456")
			.cmpnyNm("AnotherCompany")
			.bsnsSumryCn("Another Business Summary")
			.rcritJssfcCmmnCodeSe("RCR456")
			.jobcodeNm("AnotherJobCode")
			.rcritNmprCo(5)
			.acdmcrCmmnCodeSe("ACD456")
			.acdmcrNm("Another Academic Name")
			.emplymStleCmmnCodeSe("EMP456")
			.emplymStleCmmnMm("EMP MM")
			.workPararBassAdresCn("Another Address")
			.subwayNm("Another SubwayName")
			.dtyCn("Another Duty Content")
			.careerCndCmmnCodeSe("CAR456")
			.careerCndNm("Another Career Name")
			.hopeWage("2000000")
			.retGrantsNm("Another Retirement Grants")
			.workTimeNm("Another Work Time")
			.workTmNm("Another Work TM")
			.holidayNm("Another Holiday")
			.weekWorkHr("35")
			.joFeinsrSbscrbNm("Another Insurance")
			.rceptClosNm("Another Receipt Close")
			.rceptMthIemNm("Another Receipt Method Item")
			.modelMthNm("Another Model Method")
			.rceptMthNm("Another Receipt Method")
			.presentnPapersNm("Another Presentation Papers")
			.mngrNm("Another Manager Name")
			.mngrPhonNo("Another Manager Phone")
			.mngrInsttNm("Another Manager Institute")
			.bassAdresCn("Another Base Address")
			.joSj("Another Job Subject")
			.joRegDt("2023-02-01")
			.guiLn("Another Guide Line")
			.gu("Another GU")
			.jobCode("Another Job Code")
			.build();
		entityManager.persist(newPost);
		entityManager.flush();

		// When
		Optional<Bookmark> foundBookmark = bookmarkRepository.findByPostAndUser(newPost, newUser);

		// Then
		assertThat(foundBookmark).isNotPresent();
	}
}