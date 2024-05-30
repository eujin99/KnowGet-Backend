package com.knowget.knowgetbackend.domain.admin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.knowget.knowgetbackend.global.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
	// 사용자 이름으로 관리자 계정을 검색하는 메서드
	Optional<Admin> findByUsername(String username);
}
