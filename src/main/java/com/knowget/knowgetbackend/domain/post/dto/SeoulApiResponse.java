package com.knowget.knowgetbackend.domain.post.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.knowget.knowgetbackend.global.entity.Post;

import lombok.Data;

/**
 * 공공일자리 API 매핑
 */
@Data
public class SeoulApiResponse {

	@JsonProperty("GetJobInfo")
	private GetJobInfo getJobInfo;

	@Data
	public static class GetJobInfo {
		private List<Post> row; // 일자리 정보 목록
	}

}
