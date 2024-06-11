package com.knowget.knowgetbackend.global.entity;

import java.time.LocalDate;

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

	@Column(name = "reg_num", nullable = false)
	private String regNum;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "content", nullable = false, columnDefinition = "TEXT")
	private String content;

	@Column(name = "location", nullable = false)
	private String location;

	@Column(name = "code", nullable = false)
	private String code;

	@Column(name = "reg_date", nullable = false)
	private LocalDate regDate;

	@Builder
	public Post(String regNum, String title, String content, String location, String code, LocalDate regDate) {
		this.regNum = regNum;
		this.title = title;
		this.content = content;
		this.location = location;
		this.code = code;
		this.regDate = regDate;
	}

}
