package com.knowget.knowgetbackend.domain.post.service;

import java.util.List;

import com.knowget.knowgetbackend.domain.post.dto.PostRequestDTO;
import com.knowget.knowgetbackend.domain.post.dto.PostResponseDTO;

/**
 * 게시글 서비스 인터페이스.
 */
public interface PostService {
	/**
	 * 게시글 목록 저장.
	 * @param postRequestDTOs 저장할 게시글 요청 DTO 목록.
	 * @author 윾진
	 */
	void savePosts(List<PostRequestDTO> postRequestDTOs);

	/**
	 * 모든 게시글 조회.
	 * @return 모든 게시글 응답 DTO 목록.
	 * @author 윾진
	 */
	List<PostResponseDTO> getAllPosts();
}
