package com.knowget.knowgetbackend.domain.comment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.knowget.knowgetbackend.global.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

	@Query("SELECT c FROM Comment c WHERE c.successCase.caseId = :caseId ORDER BY c.createdDate ASC")
	List<Comment> findBySuccessCaseIdOrderByCreatedDateAsc(@Param("caseId") Integer caseId);

}
