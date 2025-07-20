package com.example.banto;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import com.example.banto.JWTs.JwtUtil;

import jakarta.transaction.Transactional;

@SpringBootTest
class AuthDAOTest {
	@Autowired
	JwtUtil jwtUtil;
	
	
	@Test
	@Transactional
	void test() throws Exception {
	    // MockHttpServletRequest 생성
	    MockHttpServletRequest request = new MockHttpServletRequest();
	    
	    // 헤더에 토큰 추가 (예제)
	    request.addHeader("Authorization", "Bearer my-test-token");

		String token = jwtUtil.validateToken(request);
		
		Assertions.assertThat(token).isNotNull();
	}

}
