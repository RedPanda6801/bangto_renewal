package com.example.banto.Exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
    public Integer status;
    public String error;
    public String message;
    public LocalDateTime timestamp;

    public ErrorResponse(HttpStatus status, String message){
        this.status = status.value();
        this.error = status.name();
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
