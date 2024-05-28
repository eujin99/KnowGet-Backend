package com.knowget.knowgetbackend.domain.answer.service;

import org.springframework.stereotype.Service;

import com.knowget.knowgetbackend.domain.answer.repository.AnswerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

	private final AnswerRepository answerRepository;

}
