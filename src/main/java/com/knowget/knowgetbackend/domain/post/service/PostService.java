package com.knowget.knowgetbackend.domain.post.service;

import java.util.List;

import com.knowget.knowgetbackend.domain.post.dto.PostResponseDTO;
import com.knowget.knowgetbackend.global.entity.Post;

/**
 * 게시글 서비스 인터페이스.
 */
public interface PostService {

	Integer fetchPosts(int startIndex, int endIndex);

	void notifyUsers(Post post);

	/**
	 * 게시글 목록 저장.
	 *
	 * @param postRequestDTOs 저장할 게시글 요청 DTO 목록.
	 * @author 윾진
	 */
	// void savePosts(List<PostRequestDTO> postRequestDTOs);

	/**
	 * 모든 게시글 조회.
	 *
	 * @return 모든 게시글 응답 DTO 목록.
	 * @author 윾진
	 */
	List<PostResponseDTO> getAllPosts();

	PostResponseDTO getPostById(Integer postId);

	// 근무지로 필터링된 게시글 반환
	List<PostResponseDTO> getPostsByLocation(String gu);

	// 모집 직종 코드로 필터링된 게시글 반환
	List<PostResponseDTO> getPostsByJobCode(String code);

}
