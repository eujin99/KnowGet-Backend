package com.knowget.knowgetbackend.domain.user.service;

import java.util.List;

import com.knowget.knowgetbackend.domain.counseling.dto.CounselingResponseDTO;
import com.knowget.knowgetbackend.domain.post.dto.PostResponseDTO;
import com.knowget.knowgetbackend.domain.user.dto.JobUpdateDTO;
import com.knowget.knowgetbackend.domain.user.dto.LocationUpdateDTO;
import com.knowget.knowgetbackend.domain.user.dto.PasswordUpdateDTO;
import com.knowget.knowgetbackend.domain.user.dto.UserInfoDTO;
import com.knowget.knowgetbackend.domain.user.dto.WrittenSuccessCaseDTO;

public interface MypageService {

	List<PostResponseDTO> getBookmarkList();

	String updatePrefLocation(LocationUpdateDTO locationUpdateDTO);

	String updatePrefJob(JobUpdateDTO jobUpdateDTO);

	String updatePassword(PasswordUpdateDTO passwordUpdateDTO);

	List<CounselingResponseDTO> getCounselingList();

	List<WrittenSuccessCaseDTO> getAllSuccessList();

	UserInfoDTO getUserInfo(String username);

	String deactivateUser(String username);

}
