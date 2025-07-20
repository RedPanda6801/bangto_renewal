package com.example.banto.Repositorys;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.banto.Entitys.QNAs;

public interface QNARepository  extends JpaRepository<QNAs, Integer>{

	
	@Query("SELECT q FROM QNAs q WHERE q.user.id = :userId")
	Page<QNAs> findAllByUserId(@Param("userId") Integer userId, Pageable page);
	
	@Query("SELECT q FROM QNAs q JOIN Items i ON i.store.id = :storeId WHERE q.item.id = i.id")
	Page<QNAs> findAllByStore(@Param("storeId") Integer storeId, Pageable page);

	@Query("SELECT q FROM QNAs q WHERE q.item.id = :itemId")
	Page<QNAs> findAllByItemId(@Param("itemId") Integer itemId, Pageable page);
}

