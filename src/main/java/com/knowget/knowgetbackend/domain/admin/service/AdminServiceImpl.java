package com.knowget.knowgetbackend.domain.admin.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.knowget.knowgetbackend.domain.admin.dto.AdminResponseDTO;
import com.knowget.knowgetbackend.domain.admin.repository.AdminRepository;
import com.knowget.knowgetbackend.domain.user.exception.UserNotFoundException;
import com.knowget.knowgetbackend.domain.user.repository.UserRepository;
import com.knowget.knowgetbackend.global.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

	private final AdminRepository adminRepository;
	private final UserRepository userRepository;

	/** 회원 목록 조회
	 *
	 * @return List<AdminResponseDTO>
	 */
	@Override
	@Transactional(readOnly = true)
	public List<AdminResponseDTO> getAllUsers() {

		return userRepository.findAll().stream().map(AdminResponseDTO::new).collect(Collectors.toList());
	}

	/** 회원 상태 변경
	 *
	 * @param id
	 * @param isActive
	 * @return String 회원 상태
	 */
	@Override
	@Transactional
	public String updateIsActive(Integer id, Boolean isActive) {
		User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("[ERROR] 존재하지 않는 사용자입니다."));
		user.updateIsActive(isActive);
		if (isActive == false) {
			return "회원이 비활성화되었습니다.";
		}
		return "회원이 활성화되었습니다.";

	}

}
