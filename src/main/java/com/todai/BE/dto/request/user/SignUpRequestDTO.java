package com.todai.BE.dto.request.user;

import com.todai.BE.entity.Gender;
import com.todai.BE.entity.UserType;

import java.time.LocalDate;

public record SignUpRequestDTO(
        String username,
        String password,
        String name,
        UserType userType,
        LocalDate birthdate,
        Gender gender

) {
}
