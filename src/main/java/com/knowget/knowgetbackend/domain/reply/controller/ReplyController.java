package com.knowget.knowgetbackend.domain.reply.controller;

import org.springframework.web.bind.annotation.RestController;

import com.knowget.knowgetbackend.domain.reply.service.ReplyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReplyController {

	private final ReplyService replyService;

}
