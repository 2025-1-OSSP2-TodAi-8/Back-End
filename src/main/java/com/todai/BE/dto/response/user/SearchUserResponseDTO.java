package com.todai.BE.dto.response.user;

import com.todai.BE.entity.User;

import java.time.LocalDate;

public record SearchUserResponseDTO (
        boolean exists,
        String name,
        LocalDate birthdate
){
    public static SearchUserResponseDTO from(User user) {
        return new SearchUserResponseDTO(true, user.getName(), user.getBirthdate());
    }

}
