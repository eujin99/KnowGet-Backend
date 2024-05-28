package com.knowget.knowgetbackend.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.knowget.knowgetbackend.global.entity.Post;

public interface PostRepository extends JpaRepository<Post, String> {
}
