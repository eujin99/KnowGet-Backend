// package com.knowget.knowgetbackend.domain.post.service;
//
// import java.util.List;
// import java.util.Optional;
//
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
//
// import com.knowget.knowgetbackend.domain.post.dto.PostModifyRequestDTO;
// import com.knowget.knowgetbackend.domain.post.dto.PostRequestDTO;
// import com.knowget.knowgetbackend.domain.post.exception.InvalidLoginException;
// import com.knowget.knowgetbackend.domain.post.exception.PostNotFoundException;
// import com.knowget.knowgetbackend.domain.post.repository.PostRepository;
// import com.knowget.knowgetbackend.domain.user.repository.UserRepository;
// import com.knowget.knowgetbackend.global.entity.Post;
// import com.knowget.knowgetbackend.global.entity.User;
//
// import lombok.RequiredArgsConstructor;
//
// @Service
// @RequiredArgsConstructor
// public class PostServiceImpl implements PostService {
//
// 	private final PostRepository postRepository;
// 	private final UserRepository userRepository;
//
// 	/**
// 	 * 최신순으로 Q&A 리스트 조회
// 	 */
// 	public List<Post> findAll() {
// 		return postRepository.findAllByOrderByWrittenTimeDesc();
// 	}
//
// 	/**
// 	 * Q&A 조회
// 	 */
// 	public Post findById(Long postIdx) {
// 		return postRepository.findById(postIdx).orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));
// 	}
//
// 	/**
// 	 * Q&A 생성
// 	 */
// 	@Transactional
// 	public String save(PostRequestDTO postRequestDTO) {
//
// 		Optional<User> generalMember = userRepository.findByUserId(postRequestDTO.getId());
// 		if (generalMember.isPresent()) {
// 			Post qna = Post.builder()
// 				.title(postRequestDTO.getTitle())
// 				.content(postRequestDTO.getContent())
// 				.generalMember(generalMember.get())
// 				.type(postRequestDTO.getType())
// 				.build();
// 			postRepository.save(qna);
// 			return "게시글이 저장되었습니다.";
// 		} else {
// 			throw new InvalidLoginException("잘못된 로그인입니다.");
// 		}
// 	}
//
// 	/**
// 	 * Q&A 수정
// 	 */
// 	@Transactional
// 	public String update(Long postIdx, PostModifyRequestDTO postModifyRequestDTO) {
// 		Optional<User> generalMember = userRepository.findByUserId(postModifyRequestDTO.getId());
// 		Post post = postRepository.findById(postIdx).orElseThrow(() -> new PostNotFoundException("존재하지 않는 게시물입니다."));
// 		if (generalMember.isPresent()) {
// 			if (post.getUser().equals(generalMember.get())) {
// 				post.update(postModifyRequestDTO.getTitle(), postModifyRequestDTO.getContent());
// 				return "게시글이 수정되었습니다.";
// 			} else {
// 				throw new InvalidLoginException("로그인 후 이용해주세요.");
// 			}
// 		} else {
// 			throw new InvalidLoginException("로그인 후 이용해주세요.");
// 		}
//
// 	}
//
// 	/**
// 	 * Q&A 삭제
// 	 */
// 	@Transactional
// 	public String delete(Long postIdx, String userId) {
// 		Post post = postRepository.findById(postIdx).orElseThrow(() -> new PostNotFoundException("존재하지 않는 게시글입니다."));
// 		Optional<User> generalMember = userRepository.findByUserId(userId);
// 		if (generalMember.isPresent()) {
// 			if (post.getUser().equals(generalMember.get())) {
// 				postRepository.delete(post);
// 				return "게시글이 삭제되었습니다.";
// 			} else {
// 				throw new InvalidLoginException("로그인 후 이용해주세요.");
// 			}
// 		} else {
// 			throw new InvalidLoginException("로그인 후 이용해주세요.");
// 		}
//
// 	}
//
// }
