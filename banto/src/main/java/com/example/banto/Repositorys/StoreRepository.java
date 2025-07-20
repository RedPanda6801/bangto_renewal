package com.example.banto.Repositorys;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.banto.DTOs.StoreDTO;
import com.example.banto.Entitys.Stores;


@Repository
public interface StoreRepository extends JpaRepository<Stores, Integer> {
	@Query("SELECT new com.example.banto.DTOs.StoreDTO(s.id, s.name, s.busiNum) FROM Stores s WHERE s.seller.id = :sellerId")
	public List<StoreDTO> findAllBySellerId(@Param("sellerId") Integer sellerId);

	@Query("SELECT s FROM Stores s WHERE s.seller.id = :sellerId")
	public List<Stores> findAllBySellerIdToEntity(@Param("sellerId") Integer sellerId);

	@Query("SELECT s FROM Stores s WHERE s.seller.user.id = :userId AND s.id = :storeId")
	public Optional<Stores> findStoreByUserId(@Param("userId") Integer userId, @Param("storeId") Integer storeId);
	
}