package com.todai.BE.entity;

import com.todai.BE.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "message")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "message_id", nullable = false)
    private UUID id;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content; //메세지 내용

    // 연결된 공유 관계
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sharing_id", nullable = false)
    private Sharing sharing;

    @Column(name = "is_read")
    private Boolean isRead; // 읽음 여부

    // 메세지 작성자
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
