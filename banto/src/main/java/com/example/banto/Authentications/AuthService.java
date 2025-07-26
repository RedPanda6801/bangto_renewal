package com.example.banto.Authentications;

import com.example.banto.Exceptions.AuthenticationException;
import com.example.banto.Exceptions.ForbiddenException;
import com.example.banto.Users.UserRepository;
import com.example.banto.Users.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private void checkLogin(Authentication authentication){
        if(authentication == null || !authentication.isAuthenticated()){
            throw new AuthenticationException("로그인이 필요합니다.");
        }
    }

    public Users authToUser(Authentication authentication) {
        // 1. 로그인 정보 확인
        checkLogin(authentication);
        // 2. 유저 조회 및 반환
        return userRepository.findById(Long.parseLong(authentication.getName()))
                .orElseThrow(()-> new ForbiddenException("권한이 없습니다.(유저)"));
    }

    public void authToAdmin(Authentication authentication) {
        // 1. 로그인 정보 확인
        checkLogin(authentication);
        // 2. 관리자 권한 확인
        if(authentication.getAuthorities().stream().noneMatch(auth ->
            auth.getAuthority().equals("ROLE_ADMIN") ||
            auth.getAuthority().equals("ADMIN")
        )){
            throw new ForbiddenException("관리자 권한이 없습니다.");
        }
    }

    public void authToSeller(Authentication authentication) {
        // 1. 로그인 정보 확인
        checkLogin(authentication);
        // 2. 판매자 권한 확인
        if(authentication.getAuthorities().stream().noneMatch(auth ->
            auth.getAuthority().equals("ROLE_SELLER") ||
            auth.getAuthority().equals("SELLER")
        )){
            throw new ForbiddenException("판매자 권한이 없습니다.");
        }
    }

    public void authToSellerOrAdmin(Authentication authentication) {
        // 1. 로그인 정보 확인
        checkLogin(authentication);
        // 2. 관리자 권한 확인
        if(authentication.getAuthorities().stream().noneMatch(auth ->
            auth.getAuthority().equals("ROLE_SELLER") ||
            auth.getAuthority().equals("SELLER") ||
            auth.getAuthority().equals("ROLE_ADMIN") ||
            auth.getAuthority().equals("ADMIN")
        )){
            throw new ForbiddenException("판매자 권한이 없습니다.");
        }
    }

}
