package com.todai.BE.entity;

import com.todai.BE.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "sharing_notification")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SharingNotification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "sharing_notification_id", nullable = false)
    private UUID id;

    @Column(name = "state")
    private ShareState state; //matched(accept) 또는 rejected(reject)

    //어떤 공유 요청에 대한 알림인지
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sharing_id", nullable = false)
    private Sharing sharing;

    // 알림 수신자 (보호자)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Column(name = "is_read")
    private Boolean isRead; // 읽음 여부
}

