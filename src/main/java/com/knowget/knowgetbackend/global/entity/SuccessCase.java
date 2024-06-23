package com.knowget.knowgetbackend.global.entity;

import org.hibernate.annotations.ColumnDefault;

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
@Table(name = "success_case")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SuccessCase extends BaseTime {

	@Id
	@Column(name = "case_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer caseId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "content", nullable = false, columnDefinition = "TEXT")
	private String content;

	@Column(name = "is_approved", nullable = false)
	@ColumnDefault("0")
	private Integer isApproved;

	@Builder
	public SuccessCase(User user, String title, String content) {
		this.user = user;
		this.title = title;
		this.content = content;
		this.isApproved = 0;
	}

	public void updateTitle(String title) {
		this.title = title;
	}

	public void updateContent(String content) {
		this.content = content;
	}

	public void updateIsApproved(Integer isApproved) {
		this.isApproved = isApproved;
	}

}
