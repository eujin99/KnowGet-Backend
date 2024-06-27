package com.knowget.knowgetbackend.domain.post.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.knowget.knowgetbackend.global.entity.Post;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class PostRepositoryTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private PostRepository postRepository;

	private Post post1;
	private Post post2;

	@BeforeEach
	public void setUp() {
		post1 = Post.builder()
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

		post2 = Post.builder()
			.joReqstNo("req2")
			.joRegistNo("reg2")
			.cmpnyNm("Company B")
			.bsnsSumryCn("Business Summary B")
			.rcritJssfcCmmnCodeSe("code2")
			.jobcodeNm("Job Code B")
			.rcritNmprCo(2)
			.acdmcrCmmnCodeSe("code3")
			.acdmcrNm("Academic Name B")
			.emplymStleCmmnCodeSe("style3")
			.emplymStleCmmnMm("style4")
			.workPararBassAdresCn("Busan")
			.subwayNm("Subway B")
			.dtyCn("Duty B")
			.careerCndCmmnCodeSe("career2")
			.careerCndNm("Career Name B")
			.hopeWage("Wage B")
			.retGrantsNm("Grant B")
			.workTimeNm("Work Time B")
			.workTmNm("Work TM B")
			.holidayNm("Holiday B")
			.weekWorkHr("40")
			.joFeinsrSbscrbNm("Insurance B")
			.rceptClosNm("Close B")
			.rceptMthIemNm("Method B")
			.modelMthNm("Model B")
			.rceptMthNm("Receipt B")
			.presentnPapersNm("Papers B")
			.mngrNm("Manager B")
			.mngrPhonNo("Phone B")
			.mngrInsttNm("Institution B")
			.bassAdresCn("Base Address B")
			.joSj("Job Subject B")
			.joRegDt("2023-02-01")
			.guiLn("Guide Line B")
			.gu("Seocho")
			.jobCode("Job Code B")
			.build();

		entityManager.persist(post1);
		entityManager.persist(post2);
		entityManager.flush();
	}

	@Test
	@DisplayName("게시글 전체 최신순 조회 테스트")
	public void testFindAllByOrderByPostIdDesc() {
		List<Post> posts = postRepository.findAllByOrderByPostIdDesc();
		assertThat(posts).hasSize(2);
		assertThat(posts).containsExactly(post2, post1);
	}

	@Test
	@DisplayName("구 기준 게시글 검색 테스트")
	public void testFindByWorkPararBassAdresCnContainingOrderByPostIdDesc() {
		List<Post> posts = postRepository.findByWorkPararBassAdresCnContainingOrderByPostIdDesc("Seoul");
		assertThat(posts).hasSize(1);
		assertThat(posts).contains(post1);
	}

	@Test
	@DisplayName("모집 직종 코드로 게시글 검색 테스트")
	public void testFindByRcritJssfcCmmnCodeSeOrderByPostIdDesc() {
		List<Post> posts = postRepository.findByRcritJssfcCmmnCodeSeOrderByPostIdDesc("code1");
		assertThat(posts).hasSize(1);
		assertThat(posts).contains(post1);
	}

	@Test
	@DisplayName("JoRegistNo 존재 여부 확인 테스트")
	public void testExistsByJoRegistNo() {
		Boolean exists = postRepository.existsByJoRegistNo("reg1");
		assertTrue(exists);
	}
}