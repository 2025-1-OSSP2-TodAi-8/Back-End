package com.todai.todai.domain.user.dto.request;

import com.todai.todai.domain.user.entity.Gender;
import com.todai.todai.domain.user.entity.UserType;

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
