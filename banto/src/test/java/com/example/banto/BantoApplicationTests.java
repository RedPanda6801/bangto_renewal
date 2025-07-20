package com.example.banto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;

@SpringBootTest
class BantoApplicationTests {
	
	@AfterEach
	public void after() {
		System.out.println("테스트 종료");
	}
	
	@BeforeEach
	public void before() {
		System.out.println("테스트 시작");
	}
	
	@Test
	@Transactional
	void successTest() {
		System.out.println("성공 메소드 - input : 1");
		int id = 1;
		// Data
		
		// When
		
		// Then
		//Assertions.assertThat().isNotNull();
	}
	
	@Test
	@Transactional
	void failedTest() {
		System.out.println("실패 메소드 - input : 2");
		int id = 2;
		
		// Data
		
		// When
		
		// Then
		//Assertions.assertThat().isNotNull();
	}
}
