package com.knowget.knowgetbackend.domain.post.service;

import org.springframework.stereotype.Service;

import com.knowget.knowgetbackend.domain.post.repository.PostServiceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

	PostServiceRepository postServiceRepository;

}
