package com.knowget.knowgetbackend.domain.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.knowget.knowgetbackend.domain.user.dto.UserSignUpDTO;
import com.knowget.knowgetbackend.domain.user.repository.UserRepository;
import com.knowget.knowgetbackend.global.entity.User;
import com.knowget.knowgetbackend.global.exception.RequestFailedException;
import com.knowget.knowgetbackend.global.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	/**
	 * 사용자로부터 입력받은 ID의 중복여부 확인
	 *
	 * @param username 사용자 ID
	 * @return 중복 여부 (True or False)
	 * @author Jihwan
	 */
	@Transactional(readOnly = true)
	public boolean checkUsername(String username) {
		try {
			if (username == null) {
				throw new IllegalArgumentException("아이디가 입력되지 않았습니다");
			}
			return userRepository.checkUsername(username);
		} catch (Exception e) {
			throw new RequestFailedException("[Error] 아이디 중복확인에 실패했습니다 : " + e.getMessage());
		}
	}

	/**
	 * 사용자 회원가입
	 *
	 * @param userSignUpDTO 사용자 회원가입 정보 (ID, 비밀번호, 선호지역, 선호직종)
	 * @return 회원가입 성공여부에 따른 결과 메시지
	 * @throws IllegalArgumentException 이미 존재하는 사용자일 경우
	 * @author Jihwan
	 */
	@Transactional
	public String register(UserSignUpDTO userSignUpDTO) {
		if (userRepository.findByUsername(userSignUpDTO.getUsername()).isPresent()) {
			throw new IllegalArgumentException("이미 존재하는 사용자입니다");
		}
		User user = User.builder()
			.username(userSignUpDTO.getUsername())
			.password(passwordEncoder.encode(userSignUpDTO.getPassword()))
			.prefLocation(userSignUpDTO.getPrefLocation())
			.prefJob(userSignUpDTO.getPrefJob())
			.role("USER")
			.build();
		userRepository.save(user);
		return userSignUpDTO.getUsername() + "님 가입을 환영합니다.";
	}

	public User findByUsername(String username) {
		return userRepository.findByUsername(username)
			.orElseThrow(() -> new UserNotFoundException(username + "와(과) 일치하는 사용자를 찾을 수 없습니다"));
	}

}
