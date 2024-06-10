package com.knowget.knowgetbackend.global.entity;

import com.knowget.knowgetbackend.global.common.SentTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "batch")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Batch extends SentTime {

	@Id
	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post;

	@Column(name = "reg_num", nullable = false)
	private String regNum;

	@Builder
	public Batch(Post post, String regNum) {
		this.post = post;
		this.regNum = regNum;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

}
