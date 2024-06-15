package com.knowget.knowgetbackend.domain.user.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserSignUpDTO {

	@Size(min = 3, max = 255)
	@NotEmpty(message = "사용자 아이디는 필수항목 입니다.")
	private String username;

	@Size(min = 3, max = 255)
	@NotEmpty(message = "비밀번호는 필수항목 입니다.")
	private String password;

	@NotEmpty(message = "지역은 필수항목 입니다.")
	private String prefLocation;

	@NotEmpty(message = "직업은 필수항목 입니다.")
	private String prefJob;

}
