package com.knowget.knowgetbackend.domain.jobGuide.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.knowget.knowgetbackend.global.entity.JobGuide;

public interface JobGuideRepository extends JpaRepository<JobGuide, Integer> {
}
