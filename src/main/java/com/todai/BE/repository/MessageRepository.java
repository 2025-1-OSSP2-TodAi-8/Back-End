package com.todai.BE.repository;

import com.todai.BE.entity.Message;
import com.todai.BE.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findBySharing_Owner(User owner);
    Optional<Message> findByIdAndSharing_Owner(UUID messageId, User owner);
}
