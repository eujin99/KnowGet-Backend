package com.knowget.knowgetbackend.global.entity;

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
@Table(name = "image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer imageId;

	@Column(name = "image_url", nullable = false, columnDefinition = "TEXT")
	private String imageUrl;

	@JoinColumn(name = "job_guide_id", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	private JobGuide jobGuide;

	@Builder
	public Image(String imageUrl, JobGuide jobGuide) {
		this.imageUrl = imageUrl;
		this.jobGuide = jobGuide;
	}

}

