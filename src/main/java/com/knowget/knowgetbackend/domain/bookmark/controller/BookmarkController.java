package com.knowget.knowgetbackend.domain.bookmark.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.knowget.knowgetbackend.domain.bookmark.dto.BookmarkRequestDTO;
import com.knowget.knowgetbackend.domain.bookmark.service.BookmarkService;
import com.knowget.knowgetbackend.global.dto.ResultMessageDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/bookmark")
@RequiredArgsConstructor
public class BookmarkController {

	private final BookmarkService bookmarkService;

	/**
	 * 북마크 상태 변경
	 *
	 * @param postId 구인공고 ID
	 * @return 북마크 상태 변경 결과 메시지
	 * @apiNote 북마크 상태 변경 결과 메시지 예시: 북마크 상태가 변경되었습니다 : [북마크=true]
	 * @author Jihwan
	 */
	@PostMapping("/{postId}")
	public ResponseEntity<ResultMessageDTO> bookmark(@PathVariable("postId") Integer postId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			return ResponseEntity.ok(new ResultMessageDTO("로그인이 필요합니다"));
		}
		String username = authentication.getName();
		BookmarkRequestDTO bookmarkRequestDTO = BookmarkRequestDTO.builder()
			.postId(postId)
			.username(username)
			.build();
		String message = bookmarkService.bookmark(bookmarkRequestDTO);
		return ResponseEntity.ok(new ResultMessageDTO(message));
	}

	/**
	 * 북마크 여부 확인
	 *
	 * @param postId 구인공고 ID
	 * @return 북마크 여부 (True or False)
	 * @author Jihwan
	 */
	@GetMapping("/{postId}")
	public ResponseEntity<Boolean> isBookmarked(@PathVariable("postId") Integer postId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			return ResponseEntity.ok(false);
		}
		String username = authentication.getName();
		Boolean isBookmarked = bookmarkService.isBookmarked(username, postId);
		return ResponseEntity.ok(isBookmarked);
	}

}
