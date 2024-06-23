package com.knowget.knowgetbackend.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminSignupDTO {

	private String username;

	private String password;

	private String prefLocation;

	private String prefJob;

}
