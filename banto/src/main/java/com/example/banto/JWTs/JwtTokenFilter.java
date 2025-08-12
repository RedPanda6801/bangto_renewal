package com.example.banto.JWTs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.banto.Exceptions.CustomExceptions.AuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter{
	private final JwtUtil jwtUtil;

	// 토큰이 있어야 하는 URL인지 확인
	public boolean requiresAuthentication(HttpServletRequest request) {
		String path = request.getRequestURI();
		// 토큰이 필요없는 모든 URL들 등록
		return !(
			// User
			path.startsWith("/api/sign")
			|| path.startsWith("/api/login")
			|| path.startsWith("/api/user/sns-sign")
			// Item
			|| path.startsWith("/api/item")
			// Cart
			|| path.startsWith("/api/cart")
			// QNA
			|| path.startsWith("/api/qna")
			// Comment
			|| path.startsWith("/api/comment")
			// Config
			|| path.equals("/index.html")
			|| path.equals("/")
			|| path.equals("/qna/detail")
			|| path.startsWith("/static")
			|| path.startsWith("/images")
			// Swagger & Test
			|| path.startsWith("/swagger-ui")
			|| path.startsWith("/v3/api-docs")
			|| path.startsWith("/test"));
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		// 1. 읽어올 request 값 확인
		if(!requiresAuthentication(request)) {
			filterChain.doFilter(request, response);
			return;
		}
		// 2. request 헤더 읽기
		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if(authorizationHeader == null) {
			throw new AuthenticationException("토큰이 필요합니다.");
		}
		// 3. 헤더 토큰 파싱
		String token = jwtUtil.extractToken(request);
		Claims claims = null;
		try{
			claims = jwtUtil.parseToken(token);
			// 만료 정책에 따른 새로운 토큰 생성 및 추가
		}catch (ExpiredJwtException e){
			claims = jwtUtil.handleExpiredToken(e, response);
		}
		// 4. 토큰이 유효하지 않으면 예외 처리
		if(claims.isEmpty() || claims.get("id") == null || claims.get("role") == null) {
			throw new AuthenticationException("유효하지 않은 토큰입니다.");
		}
		// 5. body의 값들 가져오기
		Long userId = Long.parseLong(claims.get("id").toString());
		String role = claims.get("role").toString();
		// 6. 역할 정보 확인
		List<String> roleList = new ArrayList<>();
		try {
			if(role.equals("ROLE_ADMIN")){
				roleList.add(role);
			}else{
				if(role.equals("ROLE_SELLER")){
					roleList.add(role);
				}
				// 비회원은 이미 필터에서 걸러짐
				roleList.add("ROLE_BUYER");
			}
			// 7. userId와 roleList로 auth 토큰 생성
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				userId, null, roleList.stream().map(SimpleGrantedAuthority::new).toList());
			// 8. ContextHolder에 로그인 정보 추가
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			// 9. 다음 체인으로 이동
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}



