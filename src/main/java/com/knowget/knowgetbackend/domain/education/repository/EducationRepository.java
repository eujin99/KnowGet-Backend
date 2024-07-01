package com.knowget.knowgetbackend.domain.education.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.knowget.knowgetbackend.global.entity.Education;

public interface EducationRepository extends JpaRepository<Education, Long> {
	
	List<Education> findAllByOrderByEducationIdDesc();

}
