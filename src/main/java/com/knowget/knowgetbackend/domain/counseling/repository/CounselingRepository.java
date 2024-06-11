package com.knowget.knowgetbackend.domain.counseling.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.knowget.knowgetbackend.global.entity.Counseling;
import com.knowget.knowgetbackend.global.entity.User;

public interface CounselingRepository extends JpaRepository<Counseling, Integer> {

	List<Counseling> findAllByOrderBySentDateDesc();

	List<Counseling> findAllByUserOrderBySentDate(User user);

}
