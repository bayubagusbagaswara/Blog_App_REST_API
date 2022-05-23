package com.blog.rest.api.exception;

import com.blog.rest.api.payload.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.io.Serial;

public class ResponseEntityErrorException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -3156815846745801694L;

    private final transient ResponseEntity<ApiResponse> apiResponse;

    public ResponseEntityErrorException(ResponseEntity<ApiResponse> apiResponse) {
        this.apiResponse = apiResponse;
    }

    public ResponseEntity<ApiResponse> getApiResponse() {
        return apiResponse;
    }
}
