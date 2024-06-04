package com.knowget.knowgetbackend.global.entity;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bookmark")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark {

	@Id
	@Column(name = "bookmark_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer bookmarkId;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "post_id", nullable = false)
	private Post post;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "is_bookmarked", nullable = false)
	@ColumnDefault("true")
	private Boolean isBookmarked;

	@Builder
	public Bookmark(Post post, User user) {
		this.post = post;
		this.user = user;
		this.isBookmarked = true;
	}

	public void updateBookmark() {
		this.isBookmarked = !this.isBookmarked;
	}

}
