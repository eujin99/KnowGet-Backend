package com.knowget.knowgetbackend.domain.notification.service;

import org.springframework.stereotype.Service;

import com.knowget.knowgetbackend.domain.notification.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private final NotificationRepository notificationRepository;

}
