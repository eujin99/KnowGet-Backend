package com.knowget.knowgetbackend.domain.post.service;

import org.springframework.stereotype.Service;

import com.knowget.knowgetbackend.domain.post.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

	PostRepository postRepository;

}
