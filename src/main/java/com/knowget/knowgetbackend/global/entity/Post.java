package com.knowget.knowgetbackend.global.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

	@Id
	@Column(name = "post_id")
	private String postId;

	@Column(name = "code", nullable = false)
	private String code;

	@Column(name = "location", nullable = false)
	private String location;

	@Column(name = "content", nullable = false, columnDefinition = "TEXT")
	private String content;

	@Column(name = "reg_date")
	private LocalDate regDate;

	@Builder
	public Post(String postId, String code, String location, String content) {
		this.postId = postId;
		this.code = code;
		this.location = location;
		this.content = content;
	}

}
