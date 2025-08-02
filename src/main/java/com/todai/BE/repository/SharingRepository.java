package com.todai.BE.repository;

import com.todai.BE.entity.ShareState;
import com.todai.BE.entity.Sharing;
import com.todai.BE.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SharingRepository extends JpaRepository<Sharing, UUID> {
    List<Sharing> findByOwnerAndShareState(User owner, ShareState shareState);
}
