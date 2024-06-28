package com.knowget.knowgetbackend.domain.answer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.knowget.knowgetbackend.global.entity.Answer;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {

    Optional<Answer> findByCounselingCounselingId(Integer counselingId);

}
