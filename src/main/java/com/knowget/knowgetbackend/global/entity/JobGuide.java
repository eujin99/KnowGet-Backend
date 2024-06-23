package com.knowget.knowgetbackend.global.entity;

import com.knowget.knowgetbackend.global.common.BaseTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "job_guide")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JobGuide extends BaseTime {

	@Id
	@Column(name = "guide_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer guideId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User user; // 작성자 (관리자)

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "content", nullable = false, columnDefinition = "TEXT")
	private String content;

	@Builder
	public JobGuide(User user, String title, String content) {
		this.user = user;
		this.title = title;
		this.content = content;
	}

	public void updateTitle(String title) {
		this.title = title;
	}

	public void updateContent(String content) {
		this.content = content;
	}

}
