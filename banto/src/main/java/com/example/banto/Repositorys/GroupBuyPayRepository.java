package com.example.banto.Repositorys;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.banto.Entitys.GroupItemPays;

public interface GroupBuyPayRepository extends JpaRepository<GroupItemPays, Integer> {
	
	@Query("SELECT p FROM GroupItemPays p WHERE p.item.id = :itemId")
	public List<GroupItemPays> findByItemId(@Param("itemId") Integer itemId);

	@Query("SELECT p FROM GroupItemPays p WHERE p.item.store.id = :storeId ORDER BY p.soldDate DESC")
	public List<GroupItemPays> findByStoreId(@Param("storeId") Integer storeId);

	@Query("SELECT p FROM GroupItemPays p WHERE p.user.id = :userId")
	public List<GroupItemPays> findByUserId(@Param("userId") Integer userId);

	@Query(value = "SELECT * FROM group_item_pays p WHERE p.buyer_pk = :userId AND EXTRACT(YEAR FROM p.sold_date) = :year ORDER BY p.sold_Date DESC",
			countQuery = "SELECT count(*) FROM group_item_pays p WHERE p.buyer_pk = :userId AND EXTRACT(YEAR FROM p.sold_date) = :year ORDER BY p.sold_date DESC",
			nativeQuery = true)
	public List<GroupItemPays> findByUserIdAndYear(@Param("userId") Integer userId, @Param("year") Integer year);


}
