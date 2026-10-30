package com.northwest.seamless.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor

public class ApiResponse <T> {
     private String responseMessage;

    private T responseBody;

    private boolean isSuccessful;


    public ApiResponse(boolean isSuccessful, String responseMessage) {

        this.isSuccessful = isSuccessful;

        this.responseMessage = responseMessage;

        this.responseBody = null;

    }

    public ApiResponse(boolean isSuccessful, String responseMessage, T responseBody) {

        this.responseMessage = responseMessage;

        this.isSuccessful = isSuccessful;

        this.responseBody = responseBody;

    }

    public static <T> ApiResponse<T> success(String message, T data) {

        return new ApiResponse<>(true, message, data);

    }

    public static <T> ApiResponse<T> success(String message) {

        return new ApiResponse<>(true, message);

    }

    public static <T> ApiResponse<T> error(String message) {

        return new ApiResponse<>(false, message);

    }

    public static <T> ApiResponse<T> validationError(String message, T validationErrors) {

        return new ApiResponse<>(false, message, validationErrors);

    }

}
