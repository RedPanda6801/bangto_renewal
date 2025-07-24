package com.example.banto.Stores;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.banto.Stores.StoreDTO;
import com.example.banto.Stores.Stores;


@Repository
public interface StoreRepository extends JpaRepository<Stores, Long> {
	public Optional<Stores> findByBusiNum(String busiNum);

	public Page<Stores> findAllBySellerId(Long sellerId, Pageable pageable);

	public Optional<Stores> findByStoreName(String storeName);

	@Query("SELECT s FROM Stores s WHERE s.seller.id = :sellerId")
	public List<Stores> findAllBySellerIdToEntity(@Param("sellerId") Integer sellerId);

	@Query("SELECT s FROM Stores s WHERE s.seller.user.id = :userId AND s.id = :storeId")
	public Optional<Stores> findStoreByUserId(@Param("userId") Integer userId, @Param("storeId") Integer storeId);
	
}