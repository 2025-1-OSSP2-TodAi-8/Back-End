package com.todai.BE.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseTimeEntity {
    @CreatedDate  //생성 시간 자동 저장
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate  //수정 시간 자동 저장
    private LocalDateTime updatedAt;
}
