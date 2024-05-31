package com.knowget.knowgetbackend.domain.admin.controller;

import org.springframework.web.bind.annotation.RestController;

import com.knowget.knowgetbackend.domain.admin.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;

}
