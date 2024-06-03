package com.knowget.knowgetbackend.domain.post.controller;

import org.springframework.web.bind.annotation.RestController;

import com.knowget.knowgetbackend.domain.post.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

}
