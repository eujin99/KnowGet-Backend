package com.knowget.knowgetbackend.domain.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.knowget.knowgetbackend.global.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN false ELSE true END FROM User u WHERE u.username = :username")
	boolean checkUsername(@Param("username") String username);

	Optional<User> findByUsername(String username);

	List<User> findByPrefLocationOrPrefJob(String prefLocation, String prefJob);

	List<User> findByRole(String role);

}
