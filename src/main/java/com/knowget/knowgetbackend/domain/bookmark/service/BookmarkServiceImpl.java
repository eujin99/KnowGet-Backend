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

	/**
	 * 북마크 상태 변경
	 *
	 * @param bookmarkRequestDTO 북마크 요청 DTO
	 * @return 북마크 상태 변경 결과 메시지
	 * @throws UserNotFoundException  사용자를 찾을 수 없는 경우
	 * @throws PostNotFoundException  공고를 찾을 수 없는 경우
	 * @throws RequestFailedException 요청 처리 중 오류가 발생한 경우
	 * @apiNote 북마크 상태 변경 결과 메시지 예시: 북마크 상태가 변경되었습니다 : [북마크=true]
	 * @author Jihwan
	 */
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
				return "북마크 상태가 변경되었습니다 : [북마크=" + result.getIsBookmarked() + "]";
			} else {
				bookmark.updateBookmark();
				bookmarkRepository.save(bookmark);
				return "북마크 상태가 변경되었습니다 : [북마크=" + bookmark.getIsBookmarked() + "]";
			}
		} catch (Exception e) {
			throw new RequestFailedException("[Error] 북마크에 실패하였습니다 : " + e.getMessage());
		}
	}

	/**
	 * 북마크 여부 확인
	 *
	 * @param username 사용자 ID
	 * @param postId   구인공고 ID
	 * @return 북마크 여부 (True or False)
	 */
	@Override
	@Transactional(readOnly = true)
	public Boolean isBookmarked(String username, Integer postId) {
		try {
			User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다"));
			Post post = postRepository.findById(postId)
				.orElseThrow(() -> new PostNotFoundException("존재하지 않는 공고입니다"));
			Bookmark bookmark = bookmarkRepository.findByPostAndUser(post, user)
				.orElse(null);
			return bookmark != null && bookmark.getIsBookmarked();
		} catch (Exception e) {
			throw new RequestFailedException("[Error] 북마크 여부 확인에 실패하였습니다 : " + e.getMessage());
		}
	}

}
