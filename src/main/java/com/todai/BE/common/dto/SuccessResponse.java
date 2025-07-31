package com.todai.BE.common.dto;

import lombok.Getter;

@Getter
public class SuccessResponse<T> {
    private final int status;
    private final String message;
    private final T data;

    public SuccessResponse(T data) {
        this.status = 200;
        this.message = "success";
        this.data = data;
    }


}
