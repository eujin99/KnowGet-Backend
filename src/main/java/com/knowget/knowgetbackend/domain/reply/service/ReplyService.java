package com.knowget.knowgetbackend.domain.reply.service;

import java.util.List;

import com.knowget.knowgetbackend.domain.reply.dto.ReplyDeleteDTO;
import com.knowget.knowgetbackend.domain.reply.dto.ReplyRequestDTO;
import com.knowget.knowgetbackend.domain.reply.dto.ReplyResponseDTO;
import com.knowget.knowgetbackend.domain.reply.dto.ReplyUpdateDTO;

public interface ReplyService {

	List<ReplyResponseDTO> findReplies(Integer commentId);

	String saveReply(ReplyRequestDTO replyRequestDTO);

	String updateReply(ReplyUpdateDTO replyUpdateDTO);

	String deleteReply(ReplyDeleteDTO replyDeleteDTO);
	
}
