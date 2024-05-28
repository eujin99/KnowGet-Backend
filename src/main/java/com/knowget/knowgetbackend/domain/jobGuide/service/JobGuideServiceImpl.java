package com.knowget.knowgetbackend.domain.jobGuide.service;

import org.springframework.stereotype.Service;

import com.knowget.knowgetbackend.domain.jobGuide.repository.JobGuideRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobGuideServiceImpl implements JobGuideService {

	private final JobGuideRepository jobGuideRepository;

}
