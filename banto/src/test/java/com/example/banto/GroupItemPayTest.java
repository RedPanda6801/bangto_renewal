package com.example.banto;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;

@SpringBootTest
class GroupItemPayTest {
	@Autowired
	GroupBuyPayRepository groupBuyPayRepository;
	
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
		
		int userId = 1521;
		// When
		// 시작 날짜 조회 후 날짜 중복 확인
		List<GroupItemPays> groupPays = groupBuyPayRepository.findByUserId(userId);
		// Then
		for(GroupItemPays groupPayment : groupPays){
			groupPayment.setUser(null);
			groupBuyPayRepository.save(groupPayment);
		}
		Assertions.assertThat(groupPays);
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
