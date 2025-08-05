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
	


}
