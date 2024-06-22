package com.knowget.knowgetbackend.domain.fileTransfer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.knowget.knowgetbackend.global.entity.Image;
import com.knowget.knowgetbackend.global.entity.JobGuide;

public interface FileTransferRepository extends JpaRepository<Image, Integer> {
	Optional<Image> findByJobGuide(JobGuide jobGuide);

	List<Image> findAllByJobGuide(JobGuide jobGuide);
}
