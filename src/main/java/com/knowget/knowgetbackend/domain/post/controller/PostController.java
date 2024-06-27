package com.knowget.knowgetbackend.domain.post.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.knowget.knowgetbackend.domain.post.dto.PostResponseDTO;
import com.knowget.knowgetbackend.domain.post.service.PostService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
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
	 * 게시글 ID로 게시글을 조회
	 *
	 * @param postId 게시글 ID
	 * @return 게시글 정보
	 * @author Jihwan
	 */
	@GetMapping("/{postId}")
	public PostResponseDTO getPostById(@PathVariable Integer postId) {
		return postService.getPostById(postId);
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

	/**
	 * 스케쥴러 없이 명시적으로 Open API를 통해 일자리 정보를 가져와 저장
	 *
	 * @return HTTP 상태 코드 200 (OK)
	 * @author Jihwan
	 */
	@GetMapping("/fetch-posts")
	public ResponseEntity<Void> fetchPosts() {
		log.info("post fetching started at {}", LocalDateTime.now());
		int insertCount = postService.fetchPosts(1, 100);
		log.info("fetched {} posts at {}", insertCount, LocalDateTime.now());
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
