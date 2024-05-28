package com.knowget.knowgetbackend.domain.reply.service;

import org.springframework.stereotype.Service;

import com.knowget.knowgetbackend.domain.reply.repository.ReplyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

	private final ReplyRepository replyRepository;

}
