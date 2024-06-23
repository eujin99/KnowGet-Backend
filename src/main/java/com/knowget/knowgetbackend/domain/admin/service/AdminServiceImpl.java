package com.knowget.knowgetbackend.domain.admin.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.knowget.knowgetbackend.domain.admin.dto.AdminResponseDTO;
import com.knowget.knowgetbackend.domain.admin.dto.AdminSignupDTO;
import com.knowget.knowgetbackend.domain.user.repository.UserRepository;
import com.knowget.knowgetbackend.global.entity.User;
import com.knowget.knowgetbackend.global.exception.RequestFailedException;
import com.knowget.knowgetbackend.global.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	/**
	 * 회원 목록 조회
	 *
	 * @return List<AdminResponseDTO>
	 */
	@Override
	@Transactional(readOnly = true)
	public List<AdminResponseDTO> getAllUsers() {
		try {
			return userRepository.findByRole("USER").stream().map(AdminResponseDTO::new).collect(Collectors.toList());
		} catch (Exception e) {
			throw new RequestFailedException("[Error] 회원 목록 조회에 실패했습니다 : " + e.getMessage());
		}
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
			.orElseThrow(() -> new UserNotFoundException("[Error] 존재하지 않는 사용자입니다."));
		user.updateIsActive(isActive);
		userRepository.save(user);
		if (!isActive) {
			return "회원이 비활성화되었습니다.";
		}
		return "회원이 활성화되었습니다.";
	}

	/**
	 * 관리자 회원가입
	 *
	 * @param adminSignupDTO 관리자 회원가입 정보
	 * @return String 회원가입 완료 메시지
	 * @throws IllegalArgumentException 이미 존재하는 관리자일 경우
	 * @author Jihwan
	 */
	@Transactional
	public String register(AdminSignupDTO adminSignupDTO) {
		if (userRepository.findByUsername(adminSignupDTO.getUsername()).isPresent()) {
			throw new IllegalArgumentException("이미 존재하는 관리자입니다");
		}
		User user = User.builder()
			.username(adminSignupDTO.getUsername())
			.password(passwordEncoder.encode(adminSignupDTO.getPassword()))
			.prefLocation(adminSignupDTO.getPrefLocation())
			.prefJob(adminSignupDTO.getPrefJob())
			.role("ADMIN")
			.build();
		userRepository.save(user);
		return adminSignupDTO.getUsername() + ": 가입이 완료되었습니다";
	}

}
