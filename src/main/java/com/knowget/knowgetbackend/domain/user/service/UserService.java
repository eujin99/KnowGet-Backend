package com.knowget.knowgetbackend.domain.user.service;

import com.knowget.knowgetbackend.domain.user.SignInResponse;
import com.knowget.knowgetbackend.domain.user.dto.UserSignInDTO;
import com.knowget.knowgetbackend.domain.user.dto.UserSignUpDTO;

public interface UserService {

	boolean checkUsername(String username);

	String register(UserSignUpDTO userSignupDTO);

	SignInResponse login(UserSignInDTO userSignInDTO);

}
