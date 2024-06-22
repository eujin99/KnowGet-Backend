package com.knowget.knowgetbackend.global.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

	private String username;
	private String role;
	private String accessToken;
	private String refreshToken;

}
