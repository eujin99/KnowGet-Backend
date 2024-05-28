package com.knowget.knowgetbackend.domain.notification.controller;

import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.knowget.knowgetbackend.domain.notification.service.NotificationServiceImpl;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationServiceImpl notificationServiceImpl;

}
