package com.knowget.knowgetbackend.domain.notification.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.knowget.knowgetbackend.domain.notification.dto.NotificationResponseDTO;
import com.knowget.knowgetbackend.domain.notification.service.NotificationService;
import com.knowget.knowgetbackend.global.dto.ResultMessageDTO;
import com.knowget.knowgetbackend.global.exception.NotificationNotFoundException;
import com.knowget.knowgetbackend.global.exception.RequestFailedException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;

	/**
	 * 현재 로그인한 사용자의 읽지 않은 알림 개수를 반환 (알림 아이콘에 배지로 표시하기 위함)
	 *
	 * @return 읽지 않은 알림 개수
	 * @author Jihwan
	 */
	@GetMapping("/unread-count")
	public Integer getUnreadNotificationCount() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return notificationService.getUnreadNotificationCount(username);
	}

	/**
	 * 현재 로그인한 사용자의 모든 알림을 반환 (알림 아이콘을 눌렀을 때, 내부에 표시하기 위함)
	 *
	 * @return 현재 로그인한 사용자의 모든 알림 리스트
	 * @throws NotificationNotFoundException 알림이 비어있는 경우
	 * @author Jihwan
	 */
	@GetMapping("/all")
	public ResponseEntity<List<NotificationResponseDTO>> getNotifications() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		List<NotificationResponseDTO> notifications = notificationService.getNotifications(username);
		return new ResponseEntity<>(notifications, HttpStatus.OK);
	}

	/**
	 * 알림을 읽음 처리
	 *
	 * @param notificationId 읽음 처리할 알림 ID
	 * @return HTTP 상태 코드 200 (OK)
	 * @throws NotificationNotFoundException 알림을 찾을 수 없는 경우
	 * @throws RequestFailedException        요청 처리 중 오류가 발생한 경우
	 * @author Jihwan
	 */
	@PostMapping("/read/{notificationId}")
	public ResponseEntity<ResultMessageDTO> markAsRead(@PathVariable Long notificationId) {
		String message = notificationService.markAsRead(notificationId);
		return new ResponseEntity<>(new ResultMessageDTO(message), HttpStatus.OK);
	}

	/**
	 * 알림 삭제 (알림 리스트에서 버튼을 눌러서 삭제)
	 *
	 * @param notificationId 삭제할 알림 ID
	 * @return HTTP 상태 코드 200 (OK)
	 * @throws NotificationNotFoundException 알림을 찾을 수 없는 경우
	 * @throws RequestFailedException        요청 처리 중 오류가 발생한 경우
	 * @author Jihwan
	 */
	@DeleteMapping("/{notificationId}")
	public ResponseEntity<ResultMessageDTO> deleteNotification(@PathVariable Long notificationId) {
		String message = notificationService.deleteNotification(notificationId);
		return new ResponseEntity<>(new ResultMessageDTO(message), HttpStatus.OK);
	}

}
