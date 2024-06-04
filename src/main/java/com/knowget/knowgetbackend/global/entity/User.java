package com.knowget.knowgetbackend.global.entity;

import org.hibernate.annotations.ColumnDefault;

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
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;

	@Column(name = "username", nullable = false, unique = true)
	private String username;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "pref_location", nullable = false)
	private String prefLocation;

	@Column(name = "pref_job", nullable = false, columnDefinition = "TINYINT(1)")
	private Short prefJob;

	@Column(name = "is_active", nullable = false)
	@ColumnDefault("true")
	private Boolean isActive;

	@Builder
	public User(String username, String password, String prefLocation, Short prefJob) {
		this.username = username;
		this.password = password;
		this.prefLocation = prefLocation;
		this.prefJob = prefJob;
		this.isActive = true;
	}

	public void updatePassword(String newPassword) {
		this.password = newPassword;
	}

	public void updatePrefLocation(String prefLocation) {
		this.prefLocation = prefLocation;
	}

	public void updatePrefJob(Short prefJob) {
		this.prefJob = prefJob;
	}

	public void updateIsActive() {
		this.isActive = false;
	}

}
