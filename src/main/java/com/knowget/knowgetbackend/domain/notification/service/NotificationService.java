package com.knowget.knowgetbackend.domain.notification.service;

import java.util.List;

import com.knowget.knowgetbackend.domain.notification.dto.NotificationResponseDTO;
import com.knowget.knowgetbackend.global.entity.Post;
import com.knowget.knowgetbackend.global.entity.User;

public interface NotificationService {

	void sendNotification(User user, Post post);

	List<NotificationResponseDTO> getNotifications(String username);

	void markAsRead(Integer notificationId);

	void deleteNotification(Integer notificationId);

	Integer getUnreadNotificationCount(String username);

}
