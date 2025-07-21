package com.example.banto.JWTs;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.banto.Configs.EnvConfig;
import com.example.banto.Users.UserRepository;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtil {
	@Autowired
	EnvConfig envConfig;
	//토큰 유효 시간 : 30분
	long expireTime = 1000 * 60 * 30;
	@Autowired
	UserRepository userRepository;

    /*public JwtUtil(UserRepository userRepository) {  // ✅ 생성자로 주입
        this.userRepository = userRepository;
    }

    public Optional<Users> getUserById(Integer id) {
        return userRepository.findById(id);
    }*/
	
	//토큰 발급(이메일 파라미터 필요, 토큰 문자열 반환)
	public String generateToken(Integer userId) {
		String SECRET_KEY = envConfig.get("JWT_SECRET");
		Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
		return Jwts.builder()
                .setSubject(Integer.toString(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
	}
	
	//토큰 분석(토큰 파라미터 필요, 토큰 내부의 이메일 반환)
	public String parseToken(String token) {
        try {
        	String SECRET_KEY = envConfig.get("JWT_SECRET");
        	Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
            return Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException e) {
            return null; // 유효하지 않은 토큰
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
	
	//토큰 만료 여부 확인
	public boolean isTokenExpired(String token) {
        try {
        	String SECRET_KEY = envConfig.get("JWT_SECRET");
            Date expiration = Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
            return expiration.before(new Date()); // 현재 시간보다 이전이면 만료됨
        } catch (ExpiredJwtException e) {
            return true; // 예외가 발생하면 만료된 것
        } catch (JwtException e) {
            return false; // 잘못된 토큰
        }
    }
	
	//토큰 추출 후 분석
	public String validateToken(HttpServletRequest request) throws Exception {
		String token = extractToken(request);
		String userId = parseToken(token);
		if(userId == null) {
			if(isTokenExpired(token)) {
				throw new Exception("만료된 토큰입니다.");
			} else {
				throw new Exception("유효하지 않은 토큰입니다.");
			}
		} else {
			return userId;
		}
	}
	
//	//userPk로 토큰 검증
//	public Boolean validateTokenById(HttpServletRequest request) throws Exception {
//		try {
//			String userId = validateToken(request);
//			
//			Optional<Users> user = userRepository.findById(Integer.parseInt(userId));
//			if(user.isEmpty()) {
//				throw new Exception("존재하지 않는 회원의 id입니다.");
////			} else if(!user.get().getEmail().equals(tokenEmail)) {
////				throw new Exception("토큰에 담긴 이메일과 사용자의 이메일이 일치하지 않습니다.");
//			}
//			else {
//				return true;
//			}
//		} catch(Exception e) {
//			throw e;
//		}
//	}
}
