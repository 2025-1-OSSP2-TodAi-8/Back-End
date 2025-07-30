package com.todai.todai.domain.diary.repository;

import com.todai.todai.domain.diary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DiaryJpaRepository extends JpaRepository<Diary, Long> {
    List<Diary> findAllByUser_UserIdAndDateBetween(
            Long userId,
            LocalDate start,
            LocalDate end
    );
}
