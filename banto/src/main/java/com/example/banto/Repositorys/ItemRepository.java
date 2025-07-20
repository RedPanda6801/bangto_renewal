package com.example.banto.Repositorys;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.banto.Entitys.CategoryType;
import com.example.banto.Entitys.Items;

public interface ItemRepository extends JpaRepository<Items, Integer> {
	
	@Query("SELECT i FROM Items i WHERE i.store.id = :storeId")
	Page<Items> getItemsByStoreId(@Param("storeId") Integer storeId, Pageable pageable); 
	
	@Query("SELECT i FROM Items i WHERE i.title LIKE %:title%")
	Page<Items> getItemsByTitle(@Param("title") String title, Pageable pageable);
	
	@Query("SELECT i FROM Items i WHERE i.store.name LIKE %:storeName%")
	Page<Items> getItemsByStoreName(@Param("storeName") String storeName, Pageable pageable);
	
	@Query("SELECT i FROM Items i WHERE i.category = :category")
	Page<Items> getItemsByCategory(@Param("category") CategoryType category, Pageable pageable);
	
	@Query("SELECT i FROM Items i WHERE (:title IS NULL OR i.title LIKE %:title%) AND (:storeName IS NULL OR i.store.name LIKE %:storeName%) AND (:category IS NULL OR i.category = :category)")
	Page<Items> getFilterdItemList(@Param("title") String title, @Param("storeName") String storeName, @Param("category") CategoryType category, Pageable pageable);
	
	@Query("SELECT i FROM Items i ORDER BY SIZE(i.favorites) DESC")
	Page<Items> getItemsOrderByFavoritesSizeDesc(Pageable pageable);
}
