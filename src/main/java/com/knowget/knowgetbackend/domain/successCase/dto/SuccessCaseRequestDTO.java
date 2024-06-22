package com.knowget.knowgetbackend.domain.successCase.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuccessCaseRequestDTO {

	private String title;
	private String content;
	private String username;

}
