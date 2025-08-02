package com.todai.BE.dto.request.user;

import com.todai.BE.entity.Gender;
import com.todai.BE.entity.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SignUpRequestDTO(
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank String name,
        @NotNull UserType userType,
        @NotNull LocalDate birthdate,
        @NotNull Gender gender,
        @NotBlank @Email String email
) {
}
