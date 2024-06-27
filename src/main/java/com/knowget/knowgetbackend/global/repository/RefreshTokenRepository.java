package com.knowget.knowgetbackend.global.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.knowget.knowgetbackend.global.entity.RefreshToken;
import com.knowget.knowgetbackend.global.entity.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	Optional<RefreshToken> findByToken(String token);

	void deleteByUser(User user);

	void deleteByUser_Username(String username);

}
