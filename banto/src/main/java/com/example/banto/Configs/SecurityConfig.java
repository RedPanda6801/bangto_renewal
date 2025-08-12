package com.example.banto.Configs;

import java.util.List;

import com.example.banto.Exceptions.ExceptionHandlingFilter;
import com.example.banto.JWTs.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
	private final ExceptionHandlingFilter exceptionHandlingFilter;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration();
	    
	    configuration.setAllowedOrigins(List.of("http://localhost:3000")); // 특정 Origin 허용
	    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 허용할 HTTP 메서드
	    configuration.setAllowedHeaders(List.of("Authorization", "Content-Type")); // 허용할 헤더
	    configuration.setAllowCredentials(true); // 쿠키 및 인증 정보 포함 허용

	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration); // 특정 경로에만 적용

	    return source;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.httpBasic(AbstractHttpConfigurer::disable)
			.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(
			request -> request
			// 관리자만 허용된 URL
			.requestMatchers(
				new AntPathRequestMatcher("/api/admin/**")
			).hasRole("ADMIN")
			// 판매자만 허용된 URL
			.requestMatchers(
				new AntPathRequestMatcher("/api/seller/**"),
				new AntPathRequestMatcher("/api/store/**")
			).hasRole("SELLER")
			// 구매자만 허용된 URL
			.requestMatchers(
				new AntPathRequestMatcher("/api/apply/**")
			).hasRole("BUYER")
			// 판매자, 구매자 둘 다에게 허용된 URL
			.requestMatchers(
				new AntPathRequestMatcher("/api/payment/**")
			).hasAnyRole("SELLER", "BUYER")
			// 관리자, 판매자, 구매자 셋 다에게 허용된 URL
			.requestMatchers(
				new AntPathRequestMatcher("/api/user")
			).hasAnyRole("ADMIN", "SELLER", "BUYER")
			// 로그인한 사용자에게 허용
			.anyRequest().permitAll()
			// 그 외 모든 요청 허용
			)
			.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(exceptionHandlingFilter, JwtTokenFilter.class)
			.csrf(AbstractHttpConfigurer::disable)
			.build();
	}
}
