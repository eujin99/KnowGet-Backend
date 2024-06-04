package com.knowget.knowgetbackend.domain.user.service;

import java.util.List;

import com.knowget.knowgetbackend.domain.counseling.dto.CounselingResponseDTO;
import com.knowget.knowgetbackend.domain.user.dto.JobUpdateDTO;
import com.knowget.knowgetbackend.domain.user.dto.LocationUpdateDTO;
import com.knowget.knowgetbackend.domain.user.dto.PasswordUpdateDTO;
import com.knowget.knowgetbackend.domain.user.dto.WrittenSuccessCaseDTO;
import com.knowget.knowgetbackend.global.entity.Post;

public interface MypageService {

	List<Post> getBookmarkList();

	String updatePrefLocation(LocationUpdateDTO locationUpdateDTO);

	String updatePrefJob(JobUpdateDTO jobUpdateDTO);

	String updatePassword(PasswordUpdateDTO passwordUpdateDTO);

	List<CounselingResponseDTO> getCounselingList();

	List<WrittenSuccessCaseDTO> getAllSuccessList();

}
