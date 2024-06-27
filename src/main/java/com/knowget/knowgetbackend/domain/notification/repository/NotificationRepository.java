package com.knowget.knowgetbackend.domain.notification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.knowget.knowgetbackend.global.entity.Notification;
import com.knowget.knowgetbackend.global.entity.User;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

	@Query("select n from Notification n where n.user = ?1 order by n.sentDate DESC")
	List<Notification> findByUserOrderBySentDateDesc(User user);

	@Query("SELECT COUNT(n) FROM Notification n WHERE n.user.username = :username AND n.isRead = false")
	Integer countUnreadNotificationsByUsername(String username);

	Integer countByUserAndIsReadIsFalse(User user);

}
