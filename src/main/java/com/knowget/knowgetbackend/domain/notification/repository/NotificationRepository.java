package com.knowget.knowgetbackend.domain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.knowget.knowgetbackend.global.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}
