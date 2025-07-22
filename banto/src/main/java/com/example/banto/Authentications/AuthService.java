package com.example.banto.Authentications;

import com.example.banto.Exceptions.AuthenticationException;
import com.example.banto.Exceptions.ForbiddenException;
import com.example.banto.Exceptions.UserNotFoundException;
import com.example.banto.Users.UserRepository;
import com.example.banto.Users.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public Users authToUser(Authentication authentication) {
        // 로그인 정보 확인
        if(authentication == null || !authentication.isAuthenticated()){
            throw new AuthenticationException("로그인이 필요합니다.");
        }
        // 권한 확인
        return userRepository.findById(Long.parseLong(authentication.getName()))
                .orElseThrow(()-> new ForbiddenException("권한이 없습니다.(유저)"));
    }

    public void authToAdmin(Authentication authentication) {
        // 1. 로그인 정보 확인
        if(authentication == null || !authentication.isAuthenticated()){
            throw new AuthenticationException("로그인이 필요합니다.");
        }
        // 2. 관리자 권한 확인
        if(authentication.getAuthorities().stream().noneMatch(auth ->
            auth.getAuthority().equals("ROLE_ADMIN") ||
            auth.getAuthority().equals("ADMIN"))
        ){
            throw new ForbiddenException("관리자 권한이 없습니다.");
        }
    }

    public void authSeller(Authentication authentication) {
        // 1. 로그인 정보 확인
        if(authentication == null || !authentication.isAuthenticated()){
            throw new AuthenticationException("로그인이 필요합니다.");
        }
        // 2. 관리자 권한 확인
        if(authentication.getAuthorities().stream().noneMatch(auth ->
            auth.getAuthority().equals("ROLE_SELLER") ||
                auth.getAuthority().equals("SELLER"))
        ){
            throw new ForbiddenException("관리자 권한이 없습니다.");
        }
    }
}
