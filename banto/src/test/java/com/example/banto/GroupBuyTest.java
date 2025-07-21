package com.example.banto;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;

@SpringBootTest
class GroupBuyTest {
	@Autowired
	GroupBuyRepository groupBuyRepository;
	
	
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
		// When
		// 시작 날짜 조회 후 날짜 중복 확인
		Optional<GroupBuys> eventList = groupBuyRepository.findLatest();
		// Then
		System.out.println(eventList.get().getEndDate());
		Assertions.assertThat(eventList).get();
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
