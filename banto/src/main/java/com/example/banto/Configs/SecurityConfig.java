package com.example.banto.Configs;

import java.util.List;

import com.example.banto.JWTs.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SecurityConfig {
	@Autowired
    JwtTokenFilter jwtTokenFilter;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration();
	    
	    configuration.setAllowedOrigins(List.of("http://localhost:3000")); // 특정 Origin 허용
	    configuration.setAllowedMethods(List.of("GET", "POST")); // 허용할 HTTP 메서드
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
						new AntPathRequestMatcher("/api/apply/modify"),
						new AntPathRequestMatcher("/api/apply/get-list/**"),
						new AntPathRequestMatcher("/api/apply/get-info/**"),
						new AntPathRequestMatcher("/api/manager/**"),
						new AntPathRequestMatcher("/api/pay/get-user-info/**"),
						new AntPathRequestMatcher("/api/pay/get-store-info/**"),
						new AntPathRequestMatcher("/api/seller/delete/**"),
						new AntPathRequestMatcher("/api/wallet/manager")
						).hasAuthority("ADMIN")
				// 판매자만 허용된 URL
				.requestMatchers(
						new AntPathRequestMatcher("/api/item/add-item"),
						new AntPathRequestMatcher("/api/item/modify"),
						new AntPathRequestMatcher("/api/item/option/modify"),
						new AntPathRequestMatcher("/api/pay/modify"),
						new AntPathRequestMatcher("/api/pay/get-my-store-info/**"),
						new AntPathRequestMatcher("/api/qna/store/get-list/**"),
						new AntPathRequestMatcher("/api/qna/add-answer"),
						new AntPathRequestMatcher("/api/store/**"),
						new AntPathRequestMatcher("/api/seller/get-info"),
						new AntPathRequestMatcher("/api/seller/delete-me"),
						new AntPathRequestMatcher("/api/group-item/delete"),
						new AntPathRequestMatcher("/api/group-item/modify"),
						new AntPathRequestMatcher("/api/group-item/add"),
						new AntPathRequestMatcher("/api/group-pay/store/get-list")
						).hasAuthority("SELLER")
				// 구매자만 허용된 URL
				.requestMatchers(
						new AntPathRequestMatcher("/api/apply")
						).hasAuthority("BUYER")
				// 관리자, 판매자 둘 다에게 허용된 URL
				.requestMatchers(
						new AntPathRequestMatcher("/api/group-buy/get-list")
						).hasAnyAuthority("ADMIN", "SELLER")
				// 관리자, 구매자 둘 다에게 허용된 URL
				.requestMatchers(
						new AntPathRequestMatcher("/api/qna/delete"),
						new AntPathRequestMatcher("/api/comment/delete")
						).hasAnyAuthority("ADMIN", "BUYER")
				// 판매자, 구매자 둘 다에게 허용된 URL
				.requestMatchers(
						new AntPathRequestMatcher("/api/apply/my-info"),
						new AntPathRequestMatcher("/api/cart/**"),
						new AntPathRequestMatcher("/api/favorite/**"),
						new AntPathRequestMatcher("/api/qna/my-list/**"),
						new AntPathRequestMatcher("/api/qna/add"),
						new AntPathRequestMatcher("/api/pay"),
						new AntPathRequestMatcher("/api/pay/get-info/**"),
						new AntPathRequestMatcher("/api/qna/get-detail"),
						new AntPathRequestMatcher("/api/user/modify"),
						new AntPathRequestMatcher("/api/user/delete-me"),
						new AntPathRequestMatcher("/api/wallet/my"),
						new AntPathRequestMatcher("/api/comment/write"),
						new AntPathRequestMatcher("/api/comment/get-my/**")
						).hasAnyAuthority("SELLER", "BUYER")
				// 관리자, 판매자, 구매자 셋 다에게 허용된 URL
				.requestMatchers(
						new AntPathRequestMatcher("/api/user/get-info")
						).hasAnyAuthority("ADMIN", "SELLER", "BUYER")
				// 모두에게 허용된 URL
				.requestMatchers(
						new AntPathRequestMatcher("/api/group-buy/current-event"),
						new AntPathRequestMatcher("/api/group-buy/item/current-list"),
						new AntPathRequestMatcher("/api/group-item/event/get-list"),
						new AntPathRequestMatcher("/api/item/get-all-list/**"),
						new AntPathRequestMatcher("/api/item/get-itemlist/**"),
						new AntPathRequestMatcher("/api/item/get-detail/**"),
						new AntPathRequestMatcher("/api/sign"),
						new AntPathRequestMatcher("/api/login"),
						new AntPathRequestMatcher("/api/user/get-sns-signed/**"),
						//new AntPathRequestMatcher("/user/get-info"),
						new AntPathRequestMatcher("/api/comment/item/**"),
						new AntPathRequestMatcher("/api/comment/get/**"),
						new AntPathRequestMatcher("/api/item/get-by-title/**"),
						new AntPathRequestMatcher("/api/item/get-by-store-name/**"),
						new AntPathRequestMatcher("/api/item/get-by-category/**"),
						new AntPathRequestMatcher("/api/item/get-filtered-list/**"),
						new AntPathRequestMatcher("/api/item/get-recommend-list"),
						new AntPathRequestMatcher("/api/qna/item/get-list/**")
						).permitAll()
				// 그 외 모든 요청 허용
				.anyRequest().permitAll()
				)
				.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
				.csrf(csrf -> csrf.disable())
				.build();
	}
}
