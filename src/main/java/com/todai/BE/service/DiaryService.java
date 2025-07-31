package com.todai.BE.service;

import com.todai.BE.dto.request.diary.CreateDiaryDto;
import com.todai.BE.dto.response.diary.EmotionResponseDto;
import com.todai.BE.dto.response.diary.EmotionsResponseDto;
import com.todai.BE.entity.Diary;
import com.todai.BE.entity.User;
import com.todai.BE.repository.DiaryRepository;
import com.todai.BE.common.exception.CustomException;
import com.todai.BE.common.exception.ErrorCode;
import com.todai.BE.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class DiaryService {
    @Autowired
    private DiaryRepository diaryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AudioStorageService audioStorageService;

    public List<EmotionsResponseDto> getMonthEmotion(UUID userId, YearMonth yearMonth) {
        LocalDate start = yearMonth.atDay(1);
        LocalDate end   = yearMonth.atEndOfMonth();

        List<Diary> diaries = diaryRepository.findAllByUser_UserIdAndDateBetween(userId, start, end).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DIARY));

        return diaries.stream()
                .map(diary -> {
                    List<Double> em = diary.getEmotion();
                    int maxIdx = IntStream.range(0, em.size())
                            .reduce((i, j) -> em.get(i) >= em.get(j) ? i : j)
                            .orElse(0);
                    String label;
                    switch (maxIdx) {
                        case 0: label = "행복"; break;
                        case 1: label = "슬픔"; break;
                        case 2: label = "화남"; break;
                        case 3: label = "놀람"; break;
                        case 4: label = "공포"; break;
                        case 5: label = "혐오"; break;
                        default: label = "알수없음";
                    }
                    return new EmotionsResponseDto(
                            diary.getDate().toString(),
                            label
                    );
                })
                .collect(Collectors.toList());
    }

    public List<EmotionsResponseDto> getYearEmotion(UUID userId, Year year) {
        LocalDate start = year.atDay(1);
        LocalDate end = year.atMonth(12).atEndOfMonth();
        List<Diary> diaries = diaryRepository.findAllByUser_UserIdAndDateBetween(userId, start, end).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DIARY));

        return diaries.stream()
                .map(diary -> {
                    List<Double> emotion = diary.getEmotion();
                    String label = mapToLabel(emotion);

                    return new EmotionsResponseDto(
                            diary.getDate().toString(),
                            label
                    );
                })
                .collect(Collectors.toList());
    }

    public EmotionResponseDto getDayEmotion(UUID userId, LocalDate date) {
        Diary diary = diaryRepository.findByUser_UserIdAndDate(userId, date).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DIARY));
        List<Double> emotion = diary.getEmotion();

        String label = mapToLabel(emotion);

        return new EmotionResponseDto(label, emotion, diary.getSummary());
    }

    public EmotionResponseDto recordDiary(UUID userId, CreateDiaryDto diaryDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        LocalDate date = diaryDto.date();
        Optional<Diary> optionalDiary = diaryRepository.findByUser_UserIdAndDate(userId, diaryDto.date());
        Diary diary = optionalDiary.orElseGet(() -> {
            Diary d = new Diary();
            d.setUser(user);
            d.setDate(date);
            return d;
        });

        MultipartFile newFile = diaryDto.audioFile();
        if (newFile != null && !newFile.isEmpty()) {
            String oldPath = diary.getAudioPath();
            if (oldPath != null && !oldPath.isEmpty()) {
                try {
                    Files.deleteIfExists(Paths.get(oldPath));
                } catch (IOException e) {
                    throw new CustomException(ErrorCode.SERVER_ERROR_DELETE_FILE);
                }
            }
        }

        diary.setEmotion(diaryDto.emotion());
        diary.setSummary(diaryDto.summary());
        diary.setMarking(false);

        String audioPath = audioStorageService.storeAudio(userId, date, diaryDto.audioFile());
        diary.setAudioPath(audioPath);

        Diary saved = diaryRepository.save(diary);

        String label = mapToLabel(saved.getEmotion());

        return new EmotionResponseDto(label, saved.getEmotion(), saved.getSummary());
    }

    private String mapToLabel(List<Double> emotion) {
        int idx = IntStream.range(0, emotion.size())
                .reduce((i, j) -> emotion.get(i) >= emotion.get(j) ? i : j)
                .orElse(0);

        return switch (idx) {
            case 0 -> "행복";
            case 1 -> "슬픔";
            case 2 -> "화남";
            case 3 -> "놀람";
            case 4 -> "공포";
            case 5 -> "혐오";
            default -> "알수없음";
        };
    }

    public Resource getAudio(UUID userId, LocalDate date) {
        Diary diary = diaryRepository
                .findByUser_UserIdAndDate(userId, date)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DIARY));

        String audioPath = diary.getAudioPath();
        if (audioPath == null || audioPath.isBlank()) {
            throw new CustomException(ErrorCode.NOT_FOUND_AUDIO);
        }

        try {
            Path file = Paths.get(audioPath);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new CustomException(ErrorCode.NOT_FOUND_AUDIO);
            }
        } catch (MalformedURLException e) {
            throw new CustomException(ErrorCode.SERVER_ERROR_FILE_READ);
        }
    }
}
