package com.knowget.knowgetbackend.domain.user.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserSignInDTO {

	@Size(min = 3, max = 255)
	private String username;

	@Size(min = 3, max = 255)
	private String password;

}
