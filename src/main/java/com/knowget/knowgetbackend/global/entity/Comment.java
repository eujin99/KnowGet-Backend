package com.knowget.knowgetbackend.global.entity;

import java.util.Set;

import com.knowget.knowgetbackend.global.common.BaseTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTime {

	@Id
	@Column(name = "comment_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer commentId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "case_id", nullable = false)
	private SuccessCase successCase;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "content", nullable = false, columnDefinition = "TEXT")
	private String content;

	@OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Reply> replies;

	@Builder
	public Comment(SuccessCase successCase, User user, String content) {
		this.successCase = successCase;
		this.user = user;
		this.content = content;
	}

	public void updateContent(String content) {
		this.content = content;
	}

}
