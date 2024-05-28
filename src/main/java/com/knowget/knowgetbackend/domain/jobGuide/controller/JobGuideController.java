package com.knowget.knowgetbackend.domain.jobGuide.controller;

import org.springframework.web.bind.annotation.RestController;

import com.knowget.knowgetbackend.domain.jobGuide.service.JobGuideService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class JobGuideController {

	private final JobGuideService jobGuideService;

}
