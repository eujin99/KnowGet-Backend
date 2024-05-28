package com.knowget.knowgetbackend.domain.bookmark.service;

import org.springframework.stereotype.Service;

import com.knowget.knowgetbackend.domain.bookmark.repository.BookmarkRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

	private final BookmarkRepository bookmarkRepository;

}
