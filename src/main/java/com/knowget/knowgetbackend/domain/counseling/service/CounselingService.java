package com.knowget.knowgetbackend.domain.counseling.service;

import java.util.List;

import com.knowget.knowgetbackend.domain.counseling.dto.CounselingRequestDTO;
import com.knowget.knowgetbackend.domain.counseling.dto.CounselingResponseDTO;

public interface CounselingService {

	List<CounselingResponseDTO> getAllCounseling();

	CounselingResponseDTO getCounselingById(Integer id);

	String saveCounseling(CounselingRequestDTO counselingRequestDTO);
}
