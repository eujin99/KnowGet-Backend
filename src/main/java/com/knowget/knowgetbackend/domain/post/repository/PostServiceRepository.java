package com.knowget.knowgetbackend.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.knowget.knowgetbackend.global.entity.Post;

@Repository
public interface PostServiceRepository extends JpaRepository<Post, Integer> {
	
}
