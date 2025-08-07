package com.example.banto.JWTs;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import com.example.banto.Exceptions.CustomExceptions.AuthenticationException;
import com.example.banto.Exceptions.CustomExceptions.TokenCreationException;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.banto.Configs.EnvConfig;

import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
public class JwtUtil {
	@Autowired
	private final EnvConfig envConfig;
	private final RefreshTokenRepository refreshTokenRepository;
	//토큰 유효 시간 : 30분
	long expireTime = 1000 * 60 * 30;
	
	//토큰 발급(이메일 파라미터 필요, 토큰 문자열 반환)
	public String generateToken(Long userId, String userRole) {
		try{
			String SECRET_KEY = envConfig.get("JWT_SECRET");
			Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
			return Jwts.builder()
				.claims(Map.of("id", userId, "role", userRole))
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + expireTime))
				.signWith(key)
				.compact();
		}catch (Exception e){
			throw  new TokenCreationException("토큰 발급에 실패했습니다.");
		}
	}
	
	//Http 요청에서 헤더에 있는 토큰 추출
	public String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7); // "Bearer " 이후의 토큰 값만 추출
        }
        return null;
    }

	//토큰 분석(토큰 파라미터 필요, 토큰 내부의 이메일 반환)
	public Claims parseToken(String token) {
		try {
			// 1. 토큰 파싱
			String SECRET_KEY = envConfig.get("JWT_SECRET");
			Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
			return Jwts.parser()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (ExpiredJwtException e){
			// 2. 예외 발생시켜 밖에서 예외 처리
			throw e;
		} catch (JwtException e) {
			// 3. 토큰 위조 시
			throw new AuthenticationException("잘못된 토큰입니다.");
		}
	}

	public Claims handleExpiredToken(ExpiredJwtException e, HttpServletResponse response){
		// 만료 시 (parseCliamsJws에서 예외처리)
		// 1. token의 id와 role 가져오기
		Long userId = Long.parseLong(e.getClaims().get("id").toString());
		String userRole = e.getClaims().get("role").toString();
		// 2. redis의 refreshtoken에서 만료 확인
		RefreshToken refreshToken = refreshTokenRepository.findById(userId)
			.orElseThrow(() -> new AuthenticationException("만료된 토큰입니다. 재로그인 하세요."));
		// 3. 새 토큰으로 생성해서 refresh 토큰에 추가
		String newToken = generateToken(userId, userRole);
		refreshToken.setJwtToken(newToken);
		refreshTokenRepository.save(refreshToken);
		// 4. response의 헤더에 추가
		response.addHeader("Authorization", "Bearer" + newToken);
		// 5. 다시 토큰 parsing
		return parseToken(newToken);
	}
}
