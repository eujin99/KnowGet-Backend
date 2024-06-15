package com.knowget.knowgetbackend.domain.notification.dto;

import java.time.LocalDateTime;

import com.knowget.knowgetbackend.global.entity.Notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDTO {

	private Long notificationId;
	private String username;
	private Integer postId;
	private String content;
	private Boolean isRead;
	private LocalDateTime sentDate;

	public NotificationResponseDTO(Notification notification) {
		this.notificationId = notification.getNotificationId();
		this.username = notification.getUser().getUsername();
		this.postId = notification.getPost().getPostId();
		this.content = notification.getContent();
		this.isRead = notification.getIsRead();
		this.sentDate = notification.getSentDate();
	}

}
