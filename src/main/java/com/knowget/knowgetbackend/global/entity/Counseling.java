package com.knowget.knowgetbackend.global.entity;

import org.hibernate.annotations.ColumnDefault;

import com.knowget.knowgetbackend.global.common.SentTime;

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
@Table(name = "counseling")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Counseling extends SentTime {

	@Id
	@Column(name = "counseling_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer counselingId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "category", nullable = false)
	private String category;

	@Column(name = "content", nullable = false, columnDefinition = "TEXT")
	private String content;

	@Column(name = "is_answered", nullable = false)
	@ColumnDefault("false")
	private Boolean isAnswered;

	@Builder
	public Counseling(User user, String category, String content) {
		this.user = user;
		this.category = category;
		this.content = content;
		this.isAnswered = false;
	}

	public void updateIsAnswered(Boolean isAnswered) {
		this.isAnswered = isAnswered;
	}

}
