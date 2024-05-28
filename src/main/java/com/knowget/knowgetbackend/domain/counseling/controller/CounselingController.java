package com.knowget.knowgetbackend.domain.counseling.controller;

import org.springframework.web.bind.annotation.RestController;

import com.knowget.knowgetbackend.domain.counseling.service.CounselingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CounselingController {

	private final CounselingService counselingService;

}
