package com.knowget.knowgetbackend.domain.notification.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.knowget.knowgetbackend.domain.notification.dto.NotificationResponseDTO;
import com.knowget.knowgetbackend.domain.notification.repository.NotificationRepository;
import com.knowget.knowgetbackend.domain.user.repository.UserRepository;
import com.knowget.knowgetbackend.global.entity.Notification;
import com.knowget.knowgetbackend.global.entity.Post;
import com.knowget.knowgetbackend.global.entity.User;
import com.knowget.knowgetbackend.global.exception.NotificationNotFoundException;
import com.knowget.knowgetbackend.global.exception.RequestFailedException;
import com.knowget.knowgetbackend.global.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private final NotificationRepository notificationRepository;
	private final UserRepository userRepository;

	/**
	 * 사용자가 관심 가질 만한 구인공고가 등록되었을 때, 알림을 전송
	 *
	 * @param user 사용자
	 * @param post 구인공고
	 * @author Jihwan
	 * @apiNote 알림 내용 예시:
	 * [너겟 알리미] Jihwan님이 관심 가질 만한 구인공고가 등록되었습니다.
	 * 제목: {제목}
	 * 근무 지역: {근무 지역}
	 * 직종: {직종}
	 */
	@Override
	public void sendNotification(User user, Post post) {
		String message = "[너겟 알리미] " + user.getUsername() + "님이 관심 가질 만한 구인공고가 등록되었습니다.\n"
			+ "제목: " + post.getJoSj() + "\n"
			+ "근무 지역: " + post.getWorkPararBassAdresCn() + "\n"
			+ "직종: " + post.getJobcodeNm();

		Notification notification = Notification.builder()
			.user(user)
			.post(post)
			.content(message)
			.build();
		notificationRepository.save(notification);
	}

	/**
	 * 현재 로그인한 사용자의 읽지 않은 알림 개수를 반환 (알림 아이콘에 배지로 표시하기 위함)
	 *
	 * @param username 사용자 계정명
	 * @return Integer: 읽지 않은 알림 개수
	 * @throws UserNotFoundException  사용자를 찾을 수 없는 경우
	 * @throws RequestFailedException 읽지 않은 알림 개수를 불러오는데에 실패한 경우
	 * @author Jihwan
	 */
	@Override
	@Transactional(readOnly = true)
	public Integer getUnreadNotificationCount(String username) {
		try {
			User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다"));
			return notificationRepository.countByUserAndIsReadIsFalse(user);
		} catch (Exception e) {
			throw new RequestFailedException("[Error] 읽지 않은 알림 개수를 불러오는데에 실패했습니다 : " + e.getMessage());
		}
	}

	/**
	 * 현재 로그인한 사용자의 모든 알림을 반환 (알림 아이콘을 눌렀을 때, 내부에 표시하기 위함)
	 *
	 * @param username 사용자 계정명
	 * @return List: 현재 로그인한 사용자의 모든 알림 리스트
	 * @throws UserNotFoundException         사용자를 찾을 수 없는 경우
	 * @throws NotificationNotFoundException 알림이 비어있는 경우
	 * @throws RequestFailedException        알림을 불러오는데에 실패한 경우
	 */
	@Override
	@Transactional(readOnly = true)
	public List<NotificationResponseDTO> getNotifications(String username) {
		try {
			User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다"));
			List<NotificationResponseDTO> notifications = notificationRepository.findByUser(user)
				.stream()
				.map(NotificationResponseDTO::new)
				.collect(Collectors.toList());
			if (notifications.isEmpty()) {
				throw new NotificationNotFoundException("알림이 비어있습니다");
			}
			return notifications;
		} catch (Exception e) {
			throw new RequestFailedException("[Error] 알림을 불러오는데에 실패했습니다 : " + e.getMessage());
		}
	}

	/**
	 * 알림을 읽음 처리
	 *
	 * @param notificationId 읽음 처리할 알림 ID
	 * @throws NotificationNotFoundException 알림을 찾을 수 없는 경우
	 * @throws RequestFailedException        요청 처리 중 오류가 발생한 경우
	 * @author Jihwan
	 */
	@Override
	@Transactional
	public String markAsRead(Long notificationId) {
		try {
			Notification notification = notificationRepository.findById(notificationId)
				.orElseThrow(() -> new NotificationNotFoundException("존재하지 않는 알림입니다"));
			notification.updateIsRead();
			notificationRepository.save(notification);
			return "알림을 읽음으로 변경하였습니다";
		} catch (Exception e) {
			throw new RequestFailedException("[Error] 알림을 읽음으로 변경하는 데에 실패했습니다 : " + e.getMessage());
		}
	}

	/**
	 * 알림 삭제 (알림 리스트에서 버튼을 눌러서 삭제)
	 *
	 * @param notificationId 삭제할 알림 ID
	 * @throws NotificationNotFoundException 알림을 찾을 수 없는 경우
	 * @throws RequestFailedException        요청 처리 중 오류가 발생한 경우
	 * @apiNote 알림 삭제는 사용자가 직접 삭제하는 기능이므로, 삭제한 알림은 복구할 수 없음
	 * @author Jihwan
	 */
	@Override
	@Transactional
	public String deleteNotification(Long notificationId) {
		try {
			Notification notification = notificationRepository.findById(notificationId)
				.orElseThrow(() -> new NotificationNotFoundException("존재하지 않는 알림입니다"));
			notificationRepository.delete(notification);
			return "알림을 삭제하였습니다";
		} catch (Exception e) {
			throw new RequestFailedException("[Error] 알림을 삭제하는 데에 실패했습니다 : " + e.getMessage());
		}
	}

}
