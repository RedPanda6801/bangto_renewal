package com.example.banto.Repositorys;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.banto.Entitys.SoldItems;

import java.util.List;

public interface PayRepository extends JpaRepository<SoldItems, Integer> {
	@Query(value = "SELECT * FROM sold_items s WHERE s.buyer_pk = :userId AND EXTRACT(YEAR FROM s.sold_date) = :year ORDER BY s.sold_date DESC",
		       countQuery = "SELECT count(*) FROM sold_items s WHERE s.buyer_pk = :userId AND EXTRACT(YEAR FROM s.sold_date) = :year ORDER BY s.sold_date DESC",
		       nativeQuery = true)
	public Page<SoldItems> findAllByUserIdAndYear(@Param("userId") Integer userId, @Param("year") Integer year, Pageable pageable);

	@Query("SELECT s FROM SoldItems s WHERE s.user.id = :userId")
	public List<SoldItems> findAllByUserId(@Param("userId") Integer userId);

	@Query("SELECT s FROM SoldItems s WHERE s.storePk = :storeId")
	public Page<SoldItems> findAllByStoreId(@Param("storeId") Integer storeId, Pageable pageable);
}
