// package com.knowget.knowgetbackend.domain.post.controller;
//
// import java.util.List;
//
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PatchMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// import com.knowget.knowgetbackend.domain.post.dto.PostModifyRequestDTO;
// import com.knowget.knowgetbackend.domain.post.dto.PostRequestDTO;
// import com.knowget.knowgetbackend.domain.post.exception.InvalidLoginException;
// import com.knowget.knowgetbackend.domain.post.exception.PostNotFoundException;
// import com.knowget.knowgetbackend.domain.post.service.PostService;
// import com.knowget.knowgetbackend.global.entity.Post;
//
// import lombok.RequiredArgsConstructor;
//
// @RestController
// @RequestMapping("/qna")
// @RequiredArgsConstructor
// public class PostController {
//
// 	private final PostService postService;
//
// 	/**
// 	 * 최신순으로 Q&A 리스트 조회
// 	 */
// 	@GetMapping("/all")
// 	public ResponseEntity<List<Post>> findAll() {
// 		List<Post> postList = postService.findAll();
// 		return new ResponseEntity<>(postList, HttpStatus.OK);
// 	}
//
// 	/**
// 	 * Q&A 조회
// 	 */
// 	@PostMapping("/{postIdx}")
// 	public ResponseEntity<Post> findById(@PathVariable("postIdx") Long postIdx) {
// 		Post post = postService.findById(postIdx);
// 		return new ResponseEntity<>(post, HttpStatus.OK);
// 	}
//
// 	/**
// 	 * Q&A 생성
// 	 */
// 	@PostMapping("/save")
// 	public ResponseEntity<String> save(@RequestBody PostRequestDTO postRequestDTO) {
// 		String id = SecurityContextHolder.getContext().getAuthentication().getName();
// 		postRequestDTO.setId(id);
// 		postRequestDTO.setType("qna");
//
// 		String msg = postService.save(postRequestDTO);
// 		return new ResponseEntity<>(msg, HttpStatus.OK);
// 	}
//
// 	/**
// 	 * Q&A 수정
// 	 */
// 	@PatchMapping("/{postIdx}/update")
// 	public ResponseEntity<String> update(@PathVariable("postIdx") Long postIdx,
// 		@RequestBody PostModifyRequestDTO postModifyRequestDTO) {
// 		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
// 		postModifyRequestDTO.setId(userId);
// 		String msg = postService.update(postIdx, postModifyRequestDTO);
// 		return new ResponseEntity<>(msg, HttpStatus.OK);
// 	}
//
// 	/**
// 	 * Q&A 삭제
// 	 */
// 	@DeleteMapping("/{postIdx}/delete")
// 	public ResponseEntity<String> delete(@PathVariable("postIdx") Long postIdx) {
// 		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
// 		String msg = postService.delete(postIdx, userId);
// 		return new ResponseEntity<>(msg, HttpStatus.OK);
// 	}
//
// 	@ExceptionHandler(InvalidLoginException.class)
// 	public ResponseEntity<String> handleInvalidLoginException(InvalidLoginException e) {
// 		return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
// 	}
//
// 	@ExceptionHandler(PostNotFoundException.class)
// 	public ResponseEntity<String> handleQnaNotFoundException(PostNotFoundException e) {
// 		return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
// 	}
//
// }
