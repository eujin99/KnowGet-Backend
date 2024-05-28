package com.knowget.knowgetbackend.domain.bookmark.controller;

import org.springframework.web.bind.annotation.RestController;

import com.knowget.knowgetbackend.domain.bookmark.service.BookmarkServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BookmarkController {

	private final BookmarkServiceImpl bookmarkServiceImpl;

}
