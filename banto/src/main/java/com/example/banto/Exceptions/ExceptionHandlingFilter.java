package com.example.banto.Exceptions;

import com.example.banto.Exceptions.CustomExceptions.AuthenticationException;
import com.example.banto.Exceptions.CustomExceptions.TokenCreationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ExceptionHandlingFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // 다음 필터로 전달
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            setErrorResponse(response, HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (TokenCreationException e) {
            setErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "토큰 발급에 실패했습니다.");
        } catch (RuntimeException e){
            setErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생했습니다.");
        }
    }

    private void setErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ErrorResponse errorResponse = new ErrorResponse(status, message);
        String responseBody = objectMapper.writeValueAsString(errorResponse);

        response.getWriter().write(responseBody);
    }
}
