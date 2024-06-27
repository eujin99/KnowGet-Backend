package com.knowget.knowgetbackend.domain.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class UserInfoDTO {

	private String username;
	private String prefLocation;
	private String prefJob;

	@Builder
	public UserInfoDTO(String username, String prefLocation, String prefJob) {
		this.username = username;
		this.prefLocation = prefLocation;
		this.prefJob = prefJob;
	}

}
