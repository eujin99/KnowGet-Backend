package com.knowget.knowgetbackend.domain.documentTransfer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.knowget.knowgetbackend.global.entity.Document;
import com.knowget.knowgetbackend.global.entity.JobGuide;

public interface DocumentTransferRepository extends JpaRepository<Document, Integer> {

	Optional<Document> findByJobGuide(JobGuide jobGuide);

	List<Document> findAllByJobGuide(JobGuide jobGuide);
}
