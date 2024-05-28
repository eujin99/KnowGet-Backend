package com.knowget.knowgetbackend.domain.answer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.knowget.knowgetbackend.global.entity.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
}
