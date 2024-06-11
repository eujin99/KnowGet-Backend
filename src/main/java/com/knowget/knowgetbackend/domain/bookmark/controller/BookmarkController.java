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

	@PostMapping("/{postId}")
	public ResponseEntity<ResultMessageDTO> bookmark(@PathVariable("postId") Integer postId) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		BookmarkRequestDTO bookmarkRequestDTO = BookmarkRequestDTO.builder()
			.postId(postId)
			.username(username)
			.build();
		String message = bookmarkService.bookmark(bookmarkRequestDTO);
		return ResponseEntity.ok(new ResultMessageDTO(message));
	}

}
