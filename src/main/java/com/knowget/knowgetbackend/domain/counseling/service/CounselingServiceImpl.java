package com.knowget.knowgetbackend.domain.counseling.service;

import org.springframework.stereotype.Service;

import com.knowget.knowgetbackend.domain.counseling.repository.CounselingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CounselingServiceImpl implements CounselingService {

	private final CounselingRepository counselingRepository;

}
