package com.todai.BE.entity;

import com.todai.BE.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "todai_sharing")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Sharing extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID sharingId;

    // 공유하는 사람 (기본 사용자)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    // 공유 관계의 보호자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shared_with_id", nullable = false)
    private User sharedWith;

    // 공개 범위 (partial, full)
    @Enumerated(EnumType.STRING)
    @Column(name = "share_range", nullable = false)
    private ShareRange shareRange = ShareRange.PARTIAL;

    // 연동 상태 (unmatched, matched, rejected)
    @Enumerated(EnumType.STRING)
    @Column(name = "share_state", nullable = false)
    private ShareState shareState = ShareState.UNMATCHED;


    //DB 업데이트용 메소드
    public void accept() {
        this.shareState = ShareState.MATCHED;
    }

    public void reject() {

        this.shareState = ShareState.REJECTED;
    }

    public void updateShareRange(ShareRange newShareRange) {
        this.shareRange = newShareRange;
    }
}
