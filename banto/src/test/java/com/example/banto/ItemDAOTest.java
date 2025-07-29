package com.example.banto;

import java.util.Optional;

import com.example.banto.Options.Options;
import com.example.banto.Boards.Qnas.QNAs;
import com.example.banto.Options.OptionRepository;
import com.example.banto.Boards.Qnas.QNARepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.banto.Items.Items;
import com.example.banto.Stores.Stores;
import com.example.banto.Items.ItemRepository;
import com.example.banto.Stores.StoreRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
class ItemDAOTest {
	@Autowired
	StoreRepository storeRepository;
	@Autowired
	ItemRepository itemRepository;
	@Autowired
	OptionRepository optionRepository;
	@Autowired
	QNARepository qnaRepository;
	@AfterEach
	public void after() {
		System.out.println("아이템 테스트 종료");
	}
	
	@BeforeEach
	public void before() {
		System.out.println("아이템 테스트 시작");
	}
	
	@Test
	@Transactional
	void getStoreSuccessTest() {
		System.out.println("성공 메소드 - input : 1");
		int id = 1;
		
		// 인증 유효 확인
		Optional<Stores> store = storeRepository.findById(id);
		
		Assertions.assertThat(store.get().getItems()).isNotNull();
	}
	@Test
	@Transactional
	void getStorefailedTest() {
		System.out.println("실패 메소드 - input : 2");
		int id = 2;
		
		// 인증 유효 확인
		Optional<Stores> store = storeRepository.findById(id);
		
		Assertions.assertThat(store.get().getItems()).isNotNull();
	}

	@Test
	@Transactional
	void optionDeleteTest() {
		int id = 2;

		// 인증 유효 확인
		Optional<Options> option = optionRepository.findById(id);
		optionRepository.delete(option.get());

	}
	
	@Test
	@Transactional
	void get20ItemTest() {
		int page = 1;
		
		Pageable pageable = PageRequest.of(page-1, 20, Sort.by("id").ascending());
		Page<Items>items = itemRepository.findAll(pageable);
		
		Assertions.assertThat(items.getContent().size()).isEqualTo(20);
	}

	@Test
	@Transactional
	void getQNAByItem() {
		System.out.println("성공 메소드 - input : 1");
		int id = 1;
		Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
		Page<QNAs> qnaList = qnaRepository.findAllByItemId(id,pageable);
		for(QNAs qna : qnaList){
			System.out.println(qna.getQContent());
		}
		Assertions.assertThat(qnaList).isNotNull();
	}
}
