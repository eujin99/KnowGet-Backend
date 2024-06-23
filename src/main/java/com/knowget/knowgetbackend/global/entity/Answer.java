package com.knowget.knowgetbackend.global.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.knowget.knowgetbackend.global.common.BaseTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "answer")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends BaseTime {

	@Id
	@Column(name = "answer_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer answerId;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "counseling_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Counseling counseling;

	@Column(name = "content", nullable = false, columnDefinition = "TEXT")
	private String content;

	@Builder
	public Answer(Counseling counseling, String content) {
		this.counseling = counseling;
		this.content = content;
	}

	public void updateContent(String content) {
		this.content = content;
	}

}
