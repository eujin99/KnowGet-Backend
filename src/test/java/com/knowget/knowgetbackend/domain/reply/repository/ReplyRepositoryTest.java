package com.knowget.knowgetbackend.domain.reply.repository;

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
import com.knowget.knowgetbackend.global.entity.Reply;
import com.knowget.knowgetbackend.global.entity.SuccessCase;
import com.knowget.knowgetbackend.global.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ReplyRepositoryTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private ReplyRepository replyRepository;

	private User user;
	private SuccessCase successCase;
	private Comment comment;
	private Reply reply1;
	private Reply reply2;

	@BeforeEach
	public void setUp() {
		user = User.builder()
			.username("testuser")
			.password("password")
			.prefLocation("Seoul")
			.prefJob("Engineer")
			.build();

		successCase = SuccessCase.builder()
			.user(user)
			.title("Test Success Case")
			.content("This is a test success case content")
			.build();

		comment = Comment.builder()
			.successCase(successCase)
			.user(user)
			.content("This is a comment")
			.build();

		reply1 = Reply.builder()
			.comment(comment)
			.user(user)
			.content("This is a reply 1")
			.build();

		reply2 = Reply.builder()
			.comment(comment)
			.user(user)
			.content("This is a reply 2")
			.build();

		entityManager.persist(user);
		entityManager.persist(successCase);
		entityManager.persist(comment);
		entityManager.persist(reply1);
		entityManager.persist(reply2);
		entityManager.flush();
	}

	@Test
	@DisplayName("특정 댓글에 대한 모든 답글 조회 테스트 - findAllByCommentIdOrderByCreatedDateAsc")
	public void testFindAllByCommentIdOrderByCreatedDateAsc() {
		List<Reply> replies = replyRepository.findAllByCommentIdOrderByCreatedDateAsc(comment.getCommentId());
		assertThat(replies).hasSize(2);
		assertThat(replies).containsExactly(reply1, reply2);
	}

	@Test
	@DisplayName("특정 댓글에 대한 답글이 없는 경우 테스트 - findAllByCommentIdOrderByCreatedDateAsc")
	public void testFindAllByCommentIdOrderByCreatedDateAscNoReplies() {
		List<Reply> replies = replyRepository.findAllByCommentIdOrderByCreatedDateAsc(-1);
		assertThat(replies).isEmpty();
	}
}