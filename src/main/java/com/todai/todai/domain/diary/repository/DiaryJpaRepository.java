package com.todai.todai.domain.diary.repository;

import com.todai.todai.domain.diary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DiaryJpaRepository extends JpaRepository<Diary, Long> {
    Optional<List<Diary>> findAllByUser_UserIdAndDateBetween(
            Long userId,
            LocalDate start,
            LocalDate end
    );

    Optional<Diary> findByUser_UserIdAndDate(Long userId, LocalDate date);
}
