package com.knowget.knowgetbackend.domain.admin.service;

import java.util.List;

import com.knowget.knowgetbackend.domain.admin.dto.AdminResponseDTO;
import com.knowget.knowgetbackend.domain.admin.dto.AdminSignupDTO;

public interface AdminService {
	List<AdminResponseDTO> getAllUsers();

	String updateIsActive(Integer id, Boolean isActive);

	String register(AdminSignupDTO adminSignupDTO);

}
