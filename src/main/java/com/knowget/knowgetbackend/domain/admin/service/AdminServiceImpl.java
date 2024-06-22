package com.knowget.knowgetbackend.domain.admin.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.knowget.knowgetbackend.domain.admin.dto.AdminResponseDTO;
import com.knowget.knowgetbackend.domain.admin.repository.AdminRepository;
import com.knowget.knowgetbackend.domain.user.repository.UserRepository;
import com.knowget.knowgetbackend.global.entity.User;
import com.knowget.knowgetbackend.global.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

	private final AdminRepository adminRepository;
	private final UserRepository userRepository;

	/**
	 * 회원 목록 조회
	 *
	 * @return List<AdminResponseDTO>
	 */
	@Override
	@Transactional(readOnly = true)
	public List<AdminResponseDTO> getAllUsers() {

		return userRepository.findAll().stream().map(AdminResponseDTO::new).collect(Collectors.toList());
	}

	/**
	 * 회원 상태 변경
	 *
	 * @param userId   사용자 ID
	 * @param isActive 변경할 사용자 상태
	 * @return String 변경된 회원 상태
	 */
	@Override
	@Transactional
	public String updateIsActive(Integer userId, Boolean isActive) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException("[ERROR] 존재하지 않는 사용자입니다."));
		user.updateIsActive(isActive);
		userRepository.save(user);
		if (!isActive) {
			return "회원이 비활성화되었습니다.";
		}
		return "회원이 활성화되었습니다.";

	}

}
