package com.knowget.knowgetbackend.domain.answer.controller;

import org.springframework.web.bind.annotation.RestController;

import com.knowget.knowgetbackend.domain.answer.service.AnswerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AnswerController {

	private final AnswerService answerService;

}
