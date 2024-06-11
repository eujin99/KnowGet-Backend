package com.knowget.knowgetbackend.domain.post.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.knowget.knowgetbackend.domain.post.dto.PostResponseDTO;
import com.knowget.knowgetbackend.domain.post.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

	private final PostService postService;

	/**
	 * 모든 저장된 게시글을 조회.
	 *
	 * @return 저장된 모든 게시글 목록.
	 * @author 윾진
	 */
	@GetMapping
	public List<PostResponseDTO> getAllPosts() {
		return postService.getAllPosts();
	}

	/**
	 * 근무지 (구)로 필터링된 게시글을 조회.
	 *
	 * @param gu 근무지 구 이름 (예: 용산구).
	 * @return 근무지 (구)로 필터링된 게시글 목록.
	 * @author 윾진
	 */
	@GetMapping("/search-by-location")
	public List<PostResponseDTO> getPostsByLocation(@RequestParam String gu) {
		return postService.getPostsByLocation(gu);
	}

	/**
	 * 모집 직종 코드로 필터링된 게시글을 조회.
	 *
	 * @param code 모집 직종 코드.
	 * @return 모집 직종 코드로 필터링된 게시글 목록.
	 * @author 윾진
	 */
	@GetMapping("/search-by-job-code")
	public List<PostResponseDTO> getPostsByJobCode(@RequestParam String code) {
		return postService.getPostsByJobCode(code);
	}
	
}
