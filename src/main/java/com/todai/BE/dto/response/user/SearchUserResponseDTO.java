package com.todai.BE.dto.response.user;

import com.todai.BE.entity.User;

public record SearchUserResponseDTO (
        boolean exists,
        String name,
        String foundUsername
){
    public static SearchUserResponseDTO from(User user) {
        return new SearchUserResponseDTO(true, user.getName(), user.getUsername());
    }

    public static SearchUserResponseDTO notFound() {
        return new SearchUserResponseDTO(false, null, null);
    }
}
