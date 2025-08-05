package com.example.banto.Payments;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SoldItemsRepository extends JpaRepository<SoldItems, Long> {

    @Query("SELECT si FROM SoldItems si WHERE si.option.item.store.id = :storeId")
    Page<SoldItems> findAllByStoreId(Long storeId, Pageable pageable);
}