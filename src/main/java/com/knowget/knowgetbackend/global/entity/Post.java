package com.knowget.knowgetbackend.global.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

	@Id
	@Column(name = "post_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer postId;

	@JsonProperty("JO_REQST_NO")
	@Column(name = "jo_reqst_no", unique = true, nullable = false)
	private String joReqstNo;

	@JsonProperty("JO_REGIST_NO")
	@Column(name = "jo_regist_no", unique = true, nullable = false)
	private String joRegistNo;

	@JsonProperty("CMPNY_NM")
	@Column(name = "cmpny_nm")
	private String cmpnyNm;

	@JsonProperty("BSNS_SUMRY_CN")
	@Column(name = "bsns_sumry_cn", columnDefinition = "TEXT(1000)")
	private String bsnsSumryCn;

	@JsonProperty("RCRIT_JSSFC_CMMN_CODE_SE")
	@Column(name = "rcrit_jssfc_cmmn_code_se")
	private String rcritJssfcCmmnCodeSe;

	@JsonProperty("JOBCODE_NM")
	@Column(name = "jobcode_nm")
	private String jobcodeNm;

	@JsonProperty("RCRIT_NMPR_CO")
	@Column(name = "rcrit_nmpr_co")
	private Integer rcritNmprCo;

	@JsonProperty("ACDMCR_CMMN_CODE_SE")
	@Column(name = "acdmcr_cmmn_code_se")
	private String acdmcrCmmnCodeSe;

	@JsonProperty("ACDMCR_NM")
	@Column(name = "acdmcr_nm")
	private String acdmcrNm;

	@JsonProperty("EMPLYM_STLE_CMMN_CODE_SE")
	@Column(name = "emplym_stle_cmmn_code_se")
	private String emplymStleCmmnCodeSe;

	@JsonProperty("EMPLYM_STLE_CMMN_MM")
	@Column(name = "emplym_stle_cmmn_mm")
	private String emplymStleCmmnMm;

	@JsonProperty("WORK_PARAR_BASS_ADRES_CN")
	@Column(name = "work_parar_bass_adres_cn")
	private String workPararBassAdresCn;

	@JsonProperty("SUBWAY_NM")
	@Column(name = "subway_nm")
	private String subwayNm;

	@JsonProperty("DTY_CN")
	@Column(name = "dty_cn", columnDefinition = "TEXT")
	private String dtyCn;

	@JsonProperty("CAREER_CND_CMMN_CODE_SE")
	@Column(name = "career_cnd_cmmn_code_se")
	private String careerCndCmmnCodeSe;

	@JsonProperty("CAREER_CND_NM")
	@Column(name = "career_cnd_nm")
	private String careerCndNm;

	@JsonProperty("HOPE_WAGE")
	@Column(name = "hope_wage")
	private String hopeWage;

	@JsonProperty("RET_GRANTS_NM")
	@Column(name = "ret_grants_nm")
	private String retGrantsNm;

	@JsonProperty("WORK_TIME_NM")
	@Column(name = "work_time_nm")
	private String workTimeNm;

	@JsonProperty("WORK_TM_NM")
	@Column(name = "work_tm_nm")
	private String workTmNm;

	@JsonProperty("HOLIDAY_NM")
	@Column(name = "holiday_nm")
	private String holidayNm;

	@JsonProperty("WEEK_WORK_HR")
	@Column(name = "week_work_hr")
	private String weekWorkHr;

	@JsonProperty("JO_FEINSR_SBSCRB_NM")
	@Column(name = "jo_feinsr_sbscrb_nm")
	private String joFeinsrSbscrbNm;

	@JsonProperty("RCEPT_CLOS_NM")
	@Column(name = "rcept_clos_nm")
	private String rceptClosNm;

	@JsonProperty("RCEPT_MTH_IEM_NM")
	@Column(name = "rcept_mth_iem_nm")
	private String rceptMthIemNm;

	@JsonProperty("MODEL_MTH_NM")
	@Column(name = "model_mth_nm")
	private String modelMthNm;

	@JsonProperty("RCEPT_MTH_NM")
	@Column(name = "rcept_mth_nm")
	private String rceptMthNm;

	@JsonProperty("PRESENTN_PAPERS_NM")
	@Column(name = "presentn_papers_nm")
	private String presentnPapersNm;

	@JsonProperty("MNGR_NM")
	@Column(name = "mngr_nm")
	private String mngrNm;

	@JsonProperty("MNGR_PHON_NO")
	@Column(name = "mngr_phon_no")
	private String mngrPhonNo;

	@JsonProperty("MNGR_INSTT_NM")
	@Column(name = "mngr_instt_nm")
	private String mngrInsttNm;

	@JsonProperty("BASS_ADRES_CN")
	@Column(name = "bass_adres_cn")
	private String bassAdresCn;

	@JsonProperty("JO_SJ")
	@Column(name = "jo_sj")
	private String joSj;

	@JsonProperty("JO_REG_DT")
	@Column(name = "jo_reg_dt")
	private String joRegDt;

	@JsonProperty("GUI_LN")
	@Column(name = "gui_ln")
	private String guiLn;

	@Column(name = "gu", nullable = false)
	private String gu;

	@Column(name = "job_code", nullable = false)
	private String jobCode;

	@Builder
	public Post(String joReqstNo, String joRegistNo, String cmpnyNm, String bsnsSumryCn, String rcritJssfcCmmnCodeSe,
		String jobcodeNm, Integer rcritNmprCo, String acdmcrCmmnCodeSe, String acdmcrNm, String emplymStleCmmnCodeSe,
		String emplymStleCmmnMm, String workPararBassAdresCn, String subwayNm, String dtyCn, String careerCndCmmnCodeSe,
		String careerCndNm, String hopeWage, String retGrantsNm, String workTimeNm, String workTmNm, String holidayNm,
		String weekWorkHr, String joFeinsrSbscrbNm, String rceptClosNm, String rceptMthIemNm, String modelMthNm,
		String rceptMthNm, String presentnPapersNm, String mngrNm, String mngrPhonNo, String mngrInsttNm,
		String bassAdresCn, String joSj, String joRegDt, String guiLn, String gu, String jobCode) {
		this.joReqstNo = joReqstNo;
		this.joRegistNo = joRegistNo;
		this.cmpnyNm = cmpnyNm;
		this.bsnsSumryCn = bsnsSumryCn;
		this.rcritJssfcCmmnCodeSe = rcritJssfcCmmnCodeSe;
		this.jobcodeNm = jobcodeNm;
		this.rcritNmprCo = rcritNmprCo;
		this.acdmcrCmmnCodeSe = acdmcrCmmnCodeSe;
		this.acdmcrNm = acdmcrNm;
		this.emplymStleCmmnCodeSe = emplymStleCmmnCodeSe;
		this.emplymStleCmmnMm = emplymStleCmmnMm;
		this.workPararBassAdresCn = workPararBassAdresCn;
		this.subwayNm = subwayNm;
		this.dtyCn = dtyCn;
		this.careerCndCmmnCodeSe = careerCndCmmnCodeSe;
		this.careerCndNm = careerCndNm;
		this.hopeWage = hopeWage;
		this.retGrantsNm = retGrantsNm;
		this.workTimeNm = workTimeNm;
		this.workTmNm = workTmNm;
		this.holidayNm = holidayNm;
		this.weekWorkHr = weekWorkHr;
		this.joFeinsrSbscrbNm = joFeinsrSbscrbNm;
		this.rceptClosNm = rceptClosNm;
		this.rceptMthIemNm = rceptMthIemNm;
		this.modelMthNm = modelMthNm;
		this.rceptMthNm = rceptMthNm;
		this.presentnPapersNm = presentnPapersNm;
		this.mngrNm = mngrNm;
		this.mngrPhonNo = mngrPhonNo;
		this.mngrInsttNm = mngrInsttNm;
		this.bassAdresCn = bassAdresCn;
		this.joSj = joSj;
		this.joRegDt = joRegDt;
		this.guiLn = guiLn;
		this.gu = gu;
		this.jobCode = jobCode;
	}

}
