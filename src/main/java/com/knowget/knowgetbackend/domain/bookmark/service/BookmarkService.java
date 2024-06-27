package com.knowget.knowgetbackend.domain.bookmark.service;

import com.knowget.knowgetbackend.domain.bookmark.dto.BookmarkRequestDTO;

public interface BookmarkService {

	String bookmark(BookmarkRequestDTO bookmarkRequestDTO);

	Boolean isBookmarked(String username, Integer postId);

}
