package com.todai.BE.repository;

import com.todai.BE.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DiaryJpaRepository extends JpaRepository<Diary, UUID> {
    Optional<List<Diary>> findAllByUser_UserIdAndDateBetween(
            UUID userId,
            LocalDate start,
            LocalDate end
    );

    Optional<Diary> findByUser_UserIdAndDate(UUID userId, LocalDate date);
}
