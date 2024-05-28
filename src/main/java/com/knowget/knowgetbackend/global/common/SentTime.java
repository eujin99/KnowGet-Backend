package com.knowget.knowgetbackend.global.common;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@MappedSuperclass // Entity 클래스가 SentTime 을 상속받을 때, sentDate 를 인식할 수 있도록 하는 설정
@EntityListeners(AuditingEntityListener.class) // 자동으로 값을 넣어주도록 하는 annotation
@Getter
public abstract class SentTime {

	@CreatedDate    // 데이터 생성할 때 시간 자동 생성
	@Column(name = "sent_date")
	private LocalDateTime sentDate;

}
