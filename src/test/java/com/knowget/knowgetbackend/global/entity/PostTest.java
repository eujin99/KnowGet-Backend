package com.knowget.knowgetbackend.global.entity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostTest {
	private Post post;

	@BeforeEach
	public void setUp() {
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
	}

	@Test
	@DisplayName("Post 엔티티 생성 테스트")
	public void testCreatePost() {
		// Given
		String joReqstNo = "REQ123";
		String joRegistNo = "REG123";
		String cmpnyNm = "CompanyName";
		String bsnsSumryCn = "Business Summary";
		String rcritJssfcCmmnCodeSe = "RCR123";
		String jobcodeNm = "JobCode";
		Integer rcritNmprCo = 10;
		String acdmcrCmmnCodeSe = "ACD123";
		String acdmcrNm = "Academic Name";
		String emplymStleCmmnCodeSe = "EMP123";
		String emplymStleCmmnMm = "EMP MM";
		String workPararBassAdresCn = "Address";
		String subwayNm = "SubwayName";
		String dtyCn = "Duty Content";
		String careerCndCmmnCodeSe = "CAR123";
		String careerCndNm = "Career Name";
		String hopeWage = "1000000";
		String retGrantsNm = "Retirement Grants";
		String workTimeNm = "Work Time";
		String workTmNm = "Work TM";
		String holidayNm = "Holiday";
		String weekWorkHr = "40";
		String joFeinsrSbscrbNm = "Insurance";
		String rceptClosNm = "Receipt Close";
		String rceptMthIemNm = "Receipt Method Item";
		String modelMthNm = "Model Method";
		String rceptMthNm = "Receipt Method";
		String presentnPapersNm = "Presentation Papers";
		String mngrNm = "Manager Name";
		String mngrPhonNo = "Manager Phone";
		String mngrInsttNm = "Manager Institute";
		String bassAdresCn = "Base Address";
		String joSj = "Job Subject";
		String joRegDt = "2023-01-01";
		String guiLn = "Guide Line";
		String gu = "GU";
		String jobCode = "Job Code";

		// When
		Post post = Post.builder()
			.joReqstNo(joReqstNo)
			.joRegistNo(joRegistNo)
			.cmpnyNm(cmpnyNm)
			.bsnsSumryCn(bsnsSumryCn)
			.rcritJssfcCmmnCodeSe(rcritJssfcCmmnCodeSe)
			.jobcodeNm(jobcodeNm)
			.rcritNmprCo(rcritNmprCo)
			.acdmcrCmmnCodeSe(acdmcrCmmnCodeSe)
			.acdmcrNm(acdmcrNm)
			.emplymStleCmmnCodeSe(emplymStleCmmnCodeSe)
			.emplymStleCmmnMm(emplymStleCmmnMm)
			.workPararBassAdresCn(workPararBassAdresCn)
			.subwayNm(subwayNm)
			.dtyCn(dtyCn)
			.careerCndCmmnCodeSe(careerCndCmmnCodeSe)
			.careerCndNm(careerCndNm)
			.hopeWage(hopeWage)
			.retGrantsNm(retGrantsNm)
			.workTimeNm(workTimeNm)
			.workTmNm(workTmNm)
			.holidayNm(holidayNm)
			.weekWorkHr(weekWorkHr)
			.joFeinsrSbscrbNm(joFeinsrSbscrbNm)
			.rceptClosNm(rceptClosNm)
			.rceptMthIemNm(rceptMthIemNm)
			.modelMthNm(modelMthNm)
			.rceptMthNm(rceptMthNm)
			.presentnPapersNm(presentnPapersNm)
			.mngrNm(mngrNm)
			.mngrPhonNo(mngrPhonNo)
			.mngrInsttNm(mngrInsttNm)
			.bassAdresCn(bassAdresCn)
			.joSj(joSj)
			.joRegDt(joRegDt)
			.guiLn(guiLn)
			.gu(gu)
			.jobCode(jobCode)
			.build();

		// Then
		assertThat(post.getJoReqstNo()).isEqualTo(joReqstNo);
		assertThat(post.getJoRegistNo()).isEqualTo(joRegistNo);
		assertThat(post.getCmpnyNm()).isEqualTo(cmpnyNm);
		assertThat(post.getBsnsSumryCn()).isEqualTo(bsnsSumryCn);
		assertThat(post.getRcritJssfcCmmnCodeSe()).isEqualTo(rcritJssfcCmmnCodeSe);
		assertThat(post.getJobcodeNm()).isEqualTo(jobcodeNm);
		assertThat(post.getRcritNmprCo()).isEqualTo(rcritNmprCo);
		assertThat(post.getAcdmcrCmmnCodeSe()).isEqualTo(acdmcrCmmnCodeSe);
		assertThat(post.getAcdmcrNm()).isEqualTo(acdmcrNm);
		assertThat(post.getEmplymStleCmmnCodeSe()).isEqualTo(emplymStleCmmnCodeSe);
		assertThat(post.getEmplymStleCmmnMm()).isEqualTo(emplymStleCmmnMm);
		assertThat(post.getWorkPararBassAdresCn()).isEqualTo(workPararBassAdresCn);
		assertThat(post.getSubwayNm()).isEqualTo(subwayNm);
		assertThat(post.getDtyCn()).isEqualTo(dtyCn);
		assertThat(post.getCareerCndCmmnCodeSe()).isEqualTo(careerCndCmmnCodeSe);
		assertThat(post.getCareerCndNm()).isEqualTo(careerCndNm);
		assertThat(post.getHopeWage()).isEqualTo(hopeWage);
		assertThat(post.getRetGrantsNm()).isEqualTo(retGrantsNm);
		assertThat(post.getWorkTimeNm()).isEqualTo(workTimeNm);
		assertThat(post.getWorkTmNm()).isEqualTo(workTmNm);
		assertThat(post.getHolidayNm()).isEqualTo(holidayNm);
		assertThat(post.getWeekWorkHr()).isEqualTo(weekWorkHr);
		assertThat(post.getJoFeinsrSbscrbNm()).isEqualTo(joFeinsrSbscrbNm);
		assertThat(post.getRceptClosNm()).isEqualTo(rceptClosNm);
		assertThat(post.getRceptMthIemNm()).isEqualTo(rceptMthIemNm);
		assertThat(post.getModelMthNm()).isEqualTo(modelMthNm);
		assertThat(post.getRceptMthNm()).isEqualTo(rceptMthNm);
		assertThat(post.getPresentnPapersNm()).isEqualTo(presentnPapersNm);
		assertThat(post.getMngrNm()).isEqualTo(mngrNm);
		assertThat(post.getMngrPhonNo()).isEqualTo(mngrPhonNo);
		assertThat(post.getMngrInsttNm()).isEqualTo(mngrInsttNm);
		assertThat(post.getBassAdresCn()).isEqualTo(bassAdresCn);
		assertThat(post.getJoSj()).isEqualTo(joSj);
		assertThat(post.getJoRegDt()).isEqualTo(joRegDt);
		assertThat(post.getGuiLn()).isEqualTo(guiLn);
		assertThat(post.getGu()).isEqualTo(gu);
		assertThat(post.getJobCode()).isEqualTo(jobCode);
	}

	@Test
	@DisplayName("Post 엔티티 기본 생성자 테스트")
	public void testDefaultConstructor() {
		// When
		Post post = new Post();

		// Then
		assertThat(post).isNotNull();
	}
}

