package com.knowget.knowgetbackend.domain.bookmark.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.knowget.knowgetbackend.domain.bookmark.dto.BookmarkRequestDTO;
import com.knowget.knowgetbackend.domain.bookmark.repository.BookmarkRepository;
import com.knowget.knowgetbackend.domain.post.repository.PostRepository;
import com.knowget.knowgetbackend.domain.user.repository.UserRepository;
import com.knowget.knowgetbackend.global.entity.Bookmark;
import com.knowget.knowgetbackend.global.entity.Post;
import com.knowget.knowgetbackend.global.entity.User;
import com.knowget.knowgetbackend.global.exception.PostNotFoundException;
import com.knowget.knowgetbackend.global.exception.RequestFailedException;
import com.knowget.knowgetbackend.global.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

	private final BookmarkRepository bookmarkRepository;
	private final UserRepository userRepository;
	private final PostRepository postRepository;

	@Override
	@Transactional
	public String bookmark(BookmarkRequestDTO bookmarkRequestDTO) {
		try {
			User user = userRepository.findByUsername(bookmarkRequestDTO.getUsername())
				.orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다"));
			Post post = postRepository.findById(bookmarkRequestDTO.getPostId())
				.orElseThrow(() -> new PostNotFoundException("존재하지 않는 공고입니다"));
			Bookmark bookmark = bookmarkRepository.findByPostAndUser(post, user)
				.orElse(null);
			if (bookmark == null) {
				Bookmark newBookmark = Bookmark.builder()
					.post(post)
					.user(user)
					.build();
				Bookmark result = bookmarkRepository.save(newBookmark);
				return "북마크 상태가 변경되었습니다 : [BookmarkId=" + result.getBookmarkId() + "]";
			} else {
				bookmark.updateBookmark();
				return "북마크 상태가 변경되었습니다 : [BookmarkId=" + bookmark.getBookmarkId() + "]";
			}
		} catch (Exception e) {
			throw new RequestFailedException("[Error] 북마크에 실패하였습니다 : " + e.getMessage());
		}
	}

}
