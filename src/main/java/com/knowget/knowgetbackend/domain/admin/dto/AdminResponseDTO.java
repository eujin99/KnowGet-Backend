package com.knowget.knowgetbackend.domain.admin.dto;

import java.time.LocalDateTime;

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
	private String prefJob;
	private Boolean isActive;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;

	public AdminResponseDTO(User user) {
		this.userId = user.getUserId();
		this.userName = user.getUsername();
		this.prefLocation = user.getPrefLocation();
		this.prefJob = user.getPrefJob();
		this.isActive = user.getIsActive();
		this.createdDate = user.getCreatedDate();
		this.updatedDate = user.getUpdatedDate();
	}
}
