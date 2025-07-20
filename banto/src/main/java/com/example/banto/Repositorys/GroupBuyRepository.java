package com.example.banto.Repositorys;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.banto.Entitys.GroupBuys;

public interface GroupBuyRepository extends JpaRepository<GroupBuys, Integer> {
	@Query("SELECT e FROM GroupBuys e WHERE :currentDate BETWEEN e.startDate AND e.endDate")
	Optional<GroupBuys> findCurrentEvent(@Param("currentDate") LocalDateTime currentDate);

	@Query(value = "SELECT * FROM group_buys WHERE end_date > :currentDate ORDER BY start_date DESC", nativeQuery = true)
	List<GroupBuys> findAbleEvent(@Param("currentDate") LocalDateTime currentDate);

	@Query("SELECT gb FROM GroupBuys gb ORDER BY gb.startDate DESC LIMIT 1")
	Optional<GroupBuys> findLatest();

	@Query("SELECT gb FROM GroupBuys gb JOIN gb.items gi WHERE gi.sellerPk = :sellerId")
	List<GroupBuys> findAllBySellerPk(@Param("sellerId") Integer sellerId);
}
