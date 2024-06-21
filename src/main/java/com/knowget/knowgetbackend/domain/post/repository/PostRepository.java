package com.knowget.knowgetbackend.domain.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.knowget.knowgetbackend.global.entity.Post;

/**
 * 게시글 엔티티에 대한 JPA 리포지토리.
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

	List<Post> findAllByOrderByPostIdDesc();

	// 근무지 (구)로 게시글 검색
	List<Post> findByWorkPararBassAdresCnContainingOrderByPostIdDesc(String gu);

	// 모집 직종 코드로 게시글 검색
	List<Post> findByRcritJssfcCmmnCodeSeOrderByPostIdDesc(String code);

	Boolean existsByJoRegistNo(String joRegistNo);

}
