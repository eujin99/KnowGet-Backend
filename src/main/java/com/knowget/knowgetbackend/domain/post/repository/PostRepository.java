package com.knowget.knowgetbackend.domain.post.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.knowget.knowgetbackend.global.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {

	@Query("SELECT p FROM Post p WHERE p.postId = :postId")
	Optional<Post> findByPostId(@Param("postId") String postId);

}
