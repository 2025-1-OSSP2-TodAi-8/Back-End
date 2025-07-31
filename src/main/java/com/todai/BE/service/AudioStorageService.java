package com.todai.BE.service;

import com.todai.BE.common.exception.CustomException;
import com.todai.BE.common.exception.ErrorCode;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class AudioStorageService {
    private final Path rootLocation;

    public AudioStorageService(@Value("${audio.storage.location:uploads/audio}") String storageLocation) {
        this.rootLocation = Paths.get(storageLocation);
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.SERVER_ERROR_INIT_FILE);
        }
    }

    public String storeAudio(UUID userId, LocalDate date, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return "";
        }
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String filename = UUID.randomUUID() + "-" + originalFilename;

        Path userDir = rootLocation
                .resolve(userId.toString())
                .resolve(date.toString());

        try {
            Files.createDirectories(userDir);

            // 실제 저장 경로
            Path destination = userDir.resolve(filename);

            try (InputStream in = file.getInputStream()) {
                Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
            }
            return destination.toString();
        } catch (IOException e) {
            throw new CustomException(ErrorCode.SERVER_ERROR_FILE_SAVE);
        }
    }
}
