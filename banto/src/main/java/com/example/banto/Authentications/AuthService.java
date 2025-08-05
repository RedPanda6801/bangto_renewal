package com.example.banto.Authentications;

import com.example.banto.Configs.EnvConfig;
import com.example.banto.Exceptions.AuthenticationException;
import com.example.banto.Exceptions.ForbiddenException;
import com.example.banto.Sellers.SellerRepository;
import com.example.banto.Users.UserRepository;
import com.example.banto.Users.Users;
import com.example.banto.Utils.CookieHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final EnvConfig envConfig;

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

    public String authToRedis(Authentication authentication, HttpServletRequest request, HttpServletResponse response){
        if (authentication != null && authentication.isAuthenticated()
            && !"anonymousUser".equals(authentication.getPrincipal())) {
            return authentication.getName(); // 유저의 고유 식별자 (일반적으로 username)
        } else {
            return new CookieHandler().cookieHandler(request, response);
        }
    }

    public String getUserRole(Users user){
        if(user.getEmail().equals(envConfig.get("ROOT_EMAIL"))){
            return "ROLE_ADMIN";
        }
        return sellerRepository.findByUserId(user.getId())
            .map(seller -> "ROLE_SELLER").orElse("ROLE_BUYER");
    }
}
