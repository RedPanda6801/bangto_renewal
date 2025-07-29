package com.example.banto.Boards.Qnas;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QNARepository  extends JpaRepository<QNAs, Long>{

	Page<QNAs> findAllByUserId(@Param("userId") Long userId, Pageable page);
	
	@Query("SELECT q FROM QNAs q WHERE q.option.item.store.id = :storeId")
	Page<QNAs> findAllByStoreId(@Param("storeId") Long storeId, Pageable page);

	@Query("SELECT q FROM QNAs q WHERE q.option.item.id = :itemId")
	Page<QNAs> findAllByItemId(@Param("itemId") Long itemId, Pageable page);
}

