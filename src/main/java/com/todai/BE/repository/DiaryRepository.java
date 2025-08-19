package com.todai.BE.repository;

import com.todai.BE.entity.Diary;
import com.todai.BE.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DiaryRepository extends JpaRepository<Diary, UUID> {
    List<Diary> findAllByUser_UserIdAndDateBetweenOrderByDateAsc(
            UUID userId,
            LocalDate start,
            LocalDate end
    );

    Optional<Diary> findByUser_UserIdAndDate(UUID userId, LocalDate date);

    Optional<Diary> findByUser_UserId(UUID userId);

    List<Diary> findAllByUser_UserIdAndDateBetweenAndMarkingTrue(
            UUID userId,
            LocalDate start,
            LocalDate end
    );

    //최근 특정 기간 동안의 감정 데이터 조회
    List<Diary> findByUserAndDateBetweenOrderByDateDesc(User owner, LocalDate localDate, LocalDate now);



}
