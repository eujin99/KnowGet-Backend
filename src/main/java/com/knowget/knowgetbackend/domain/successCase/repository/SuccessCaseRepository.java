package com.knowget.knowgetbackend.domain.successCase.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.knowget.knowgetbackend.global.entity.SuccessCase;

public interface SuccessCaseRepository extends JpaRepository<SuccessCase, Integer> {
	// SuccessCase 검색 - By Using "Keyword"
	List<SuccessCase> findByTitleContaining(String keyword);
}
