package com.knowget.knowgetbackend.domain.comment.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.knowget.knowgetbackend.global.entity.Comment;
import com.knowget.knowgetbackend.global.entity.SuccessCase;
import com.knowget.knowgetbackend.global.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CommentRepositoryTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private CommentRepository commentRepository;

	private User user;
	private SuccessCase successCase;
	private Comment comment1;
	private Comment comment2;

	@BeforeEach
	public void setUp() {
		user = User.builder()
			.username("testuser")
			.password("password")
			.prefLocation("Seoul")
			.prefJob("Engineer")
			.build();
		entityManager.persist(user);

		successCase = SuccessCase.builder()
			.user(user)
			.title("Success Story")
			.content("This is a success story.")
			.build();
		entityManager.persist(successCase);

		comment1 = Comment.builder()
			.successCase(successCase)
			.user(user)
			.content("This is the first comment.")
			.build();
		comment2 = Comment.builder()
			.successCase(successCase)
			.user(user)
			.content("This is the second comment.")
			.build();

		entityManager.persist(comment1);
		entityManager.persist(comment2);
		entityManager.flush();
	}

	@Test
	@DisplayName("특정 성공 사례에 대한 댓글 조회 테스트 - findBySuccessCaseIdOrderByCreatedDateAsc")
	public void testFindBySuccessCaseIdOrderByCreatedDateAsc() {
		// When
		List<Comment> comments = commentRepository.findBySuccessCaseIdOrderByCreatedDateAsc(successCase.getCaseId());

		// Then
		assertThat(comments).hasSize(2);
		assertThat(comments.get(0)).isEqualTo(comment1);
		assertThat(comments.get(1)).isEqualTo(comment2);
	}

	@Test
	@DisplayName("성공 사례에 대한 댓글이 없는 경우 테스트 - findBySuccessCaseIdOrderByCreatedDateAsc")
	public void testFindBySuccessCaseIdOrderByCreatedDateAscNoComments() {
		// Given
		SuccessCase newSuccessCase = SuccessCase.builder()
			.user(user)
			.title("Another Success Story")
			.content("This is another success story.")
			.build();
		entityManager.persist(newSuccessCase);
		entityManager.flush();

		// When
		List<Comment> comments = commentRepository.findBySuccessCaseIdOrderByCreatedDateAsc(newSuccessCase.getCaseId());

		// Then
		assertThat(comments).isEmpty();
	}
}