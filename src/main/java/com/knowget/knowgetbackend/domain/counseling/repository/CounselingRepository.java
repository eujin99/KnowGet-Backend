package com.knowget.knowgetbackend.domain.counseling.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.knowget.knowgetbackend.global.entity.Counseling;

public interface CounselingRepository extends JpaRepository<Counseling, Integer> {
}
