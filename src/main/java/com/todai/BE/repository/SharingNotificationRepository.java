package com.todai.BE.repository;

import com.todai.BE.entity.SharingNotification;
import com.todai.BE.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SharingNotificationRepository extends JpaRepository<SharingNotification, UUID> {

    // 특정 보호자의 읽음X 상태의 알림만 최신순
    List<SharingNotification> findByReceiverAndIsReadFalseOrderByCreatedAtDesc(User receiver);
}
