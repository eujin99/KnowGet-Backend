package com.knowget.knowgetbackend.domain.bookmark.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.knowget.knowgetbackend.global.entity.Bookmark;
import com.knowget.knowgetbackend.global.entity.Post;
import com.knowget.knowgetbackend.global.entity.User;

public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {

	@Query("SELECT b FROM Bookmark b WHERE b.post = :post AND b.user = :user")
	Optional<Bookmark> findByPostAndUser(Post post, User user);

}
