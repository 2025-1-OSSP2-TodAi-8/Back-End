package com.todai.BE.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Types;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="diary")
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="diary_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @JdbcTypeCode(Types.ARRAY)
    @Column(
            name = "emotion",
            columnDefinition = "double precision[]",
            nullable = false
    )
    private List<Double> emotion;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name="audio_path", length = 512)
    private String audioPath;

    @Column(name="summary")
    private String summary;

    @Column(name="marking")
    private boolean marking;
}
