package com.knowget.knowgetbackend.domain.bookmark.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
	 * @param joRegistNo 구인공고 등록번호
	 * @return 북마크 상태 변경 결과 메시지
	 * @apiNote 북마크 상태 변경 결과 메시지 예시: 북마크 상태가 변경되었습니다 : [북마크=true]
	 * @author Jihwan
	 */
	@PostMapping("/{joRegistNo}")
	public ResponseEntity<ResultMessageDTO> bookmark(@PathVariable("joRegistNo") String joRegistNo) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		BookmarkRequestDTO bookmarkRequestDTO = BookmarkRequestDTO.builder()
			.joRegistNo(joRegistNo)
			.username(username)
			.build();
		String message = bookmarkService.bookmark(bookmarkRequestDTO);
		return ResponseEntity.ok(new ResultMessageDTO(message));
	}

}
