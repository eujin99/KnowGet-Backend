package com.knowget.knowgetbackend.domain.admin.service;

import org.springframework.stereotype.Service;

import com.knowget.knowgetbackend.domain.admin.repository.AdminRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

	private final AdminRepository adminRepository;

}
