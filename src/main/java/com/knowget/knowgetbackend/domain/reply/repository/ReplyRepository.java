package com.knowget.knowgetbackend.domain.reply.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.knowget.knowgetbackend.global.entity.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {
}
