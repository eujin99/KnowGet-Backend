package com.knowget.knowgetbackend.domain.bookmark.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.knowget.knowgetbackend.global.entity.Bookmark;
import com.knowget.knowgetbackend.global.entity.Post;
import com.knowget.knowgetbackend.global.entity.User;

public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {

	Optional<Bookmark> findByPostAndUser(Post post, User user);

	List<Bookmark> findByUser(User user);

}
