package com.knowget.knowgetbackend.global.entity;

import org.hibernate.annotations.ColumnDefault;

import com.knowget.knowgetbackend.global.common.BaseTime;

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
public class User extends BaseTime {

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

	@Column(name = "pref_job", nullable = false)
	private String prefJob;

	@Column(name = "is_active", nullable = false)
	@ColumnDefault("true")
	private Boolean isActive;

	@Column(name = "role", nullable = false)
	private String role;

	@Builder
	public User(String username, String password, String prefLocation, String prefJob, String role) {
		this.username = username;
		this.password = password;
		this.prefLocation = prefLocation;
		this.prefJob = prefJob;
		this.role = role;
		this.isActive = true;
	}

	public void updatePassword(String newPassword) {
		this.password = newPassword;
	}

	public void updatePrefLocation(String prefLocation) {
		this.prefLocation = prefLocation;
	}

	public void updatePrefJob(String prefJob) {
		this.prefJob = prefJob;
	}

	public void updateIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

}
