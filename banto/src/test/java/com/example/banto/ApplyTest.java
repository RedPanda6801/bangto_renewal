package com.example.banto;

import java.util.List;

import com.example.banto.SellerAuths.SellerAuths;
import com.example.banto.SellerAuths.SellerAuthRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;

@SpringBootTest
class ApplyTest {
    @Autowired
    SellerAuthRepository applyRepository;


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
        String busiNum = "123-1231-123123";
        // 시작 날짜 조회 후 날짜 중복 확인
        List<SellerAuths> applyOpt = applyRepository.findByBusiNum(busiNum);
        // Then
        for(SellerAuths apply : applyOpt){
            System.out.println(apply.getId());
        }
        Assertions.assertThat(applyOpt).isEmpty();
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
