package com.knowget.knowgetbackend.domain.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.knowget.knowgetbackend.domain.user.SignInResponse;
import com.knowget.knowgetbackend.domain.user.dto.UserSignInDTO;
import com.knowget.knowgetbackend.domain.user.dto.UserSignUpDTO;
import com.knowget.knowgetbackend.domain.user.repository.UserRepository;
import com.knowget.knowgetbackend.global.config.security.TokenProvider;
import com.knowget.knowgetbackend.global.entity.User;
import com.knowget.knowgetbackend.global.exception.InvalidPasswordException;
import com.knowget.knowgetbackend.global.exception.RequestFailedException;
import com.knowget.knowgetbackend.global.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenProvider tokenProvider;

	/**
	 * 사용자로부터 입력받은 ID의 중복여부 확인
	 *
	 * @param username 사용자 ID
	 * @return 중복 여부 (True or False)
	 * @author Jihwan
	 */
	@Override
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
	 * 사용자로부터 입력받은 값들로 회원가입을 진행
	 *
	 * @param userSignUpDTO 사용자가 입력한 ID, 이름, 비밀번호, 전화번호, 이메일
	 * @return 회원가입 성공여부에 따른 결과 메시지
	 * @author Jihwan
	 */
	@Override
	@Transactional
	public String register(UserSignUpDTO userSignUpDTO) {
		try {
			if (userSignUpDTO == null) {
				throw new IllegalArgumentException("회원정보가 입력되지 않았습니다");
			}
			User user = User.builder()
				.username(userSignUpDTO.getUsername())
				.password(passwordEncoder.encode(userSignUpDTO.getPassword()))
				.prefLocation(userSignUpDTO.getPrefLocation())
				.prefJob(userSignUpDTO.getPrefJob())
				.build();
			userRepository.save(user);
			return userSignUpDTO.getUsername() + "님 가입을 환영합니다.";
		} catch (Exception e) {
			throw new RequestFailedException("[Error] 회원가입에 실패했습니다 : " + e.getMessage());
		}
	}

	/**
	 * 사용자 로그인
	 *
	 * @param userSignInDTO 사용자가 입력한 ID, 비밀번호
	 * @return 로그인 성공여부에 따른 결과 메시지
	 * @throws UserNotFoundException    사용자가 존재하지 않을 때 발생하는 예외
	 * @throws InvalidPasswordException 비밀번호가 일치하지 않을 때 발생하는 예외
	 * @author Jihwan
	 */
	@Override
	@Transactional
	public SignInResponse login(UserSignInDTO userSignInDTO) {
		try {
			User user = userRepository.findByUsername(userSignInDTO.getUsername())
				.orElseThrow(() -> new InvalidPasswordException("입력하신 정보로 가입된 사용자가 없습니다"));
			if (passwordEncoder.matches(userSignInDTO.getPassword(), user.getPassword())) {
				String token = tokenProvider.createToken(String.format("%s:%s", user.getUsername(), "User"));
				return new SignInResponse(user.getUsername(), "User", token);
			} else {
				throw new InvalidPasswordException("아이디 혹은 비밀번호가 일치하지 않습니다");
			}
		} catch (Exception e) {
			throw new RequestFailedException("[Error] 로그인에 실패했습니다 : " + e.getMessage());
		}
	}

	// public void logout(String token) {
	// 	tokenProvider.invalidateToken(token);
	// }

}
