package com.knowget.knowgetbackend.domain.admin.dto;

import com.knowget.knowgetbackend.global.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminResponseDTO {
	private Integer userId;
	private String userName;
	private String prefLocation;
	private Short prefJob;
	private Boolean isActive;

	public AdminResponseDTO(User user) {
		this.userId = user.getUserId();
		this.userName = user.getUsername();
		this.prefLocation = user.getPrefLocation();
		this.prefJob = user.getPrefJob();
		this.isActive = user.getIsActive();
	}
}
