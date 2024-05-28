package com.knowget.knowgetbackend.domain.bookmark.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.knowget.knowgetbackend.global.entity.Bookmark;

public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {
}
