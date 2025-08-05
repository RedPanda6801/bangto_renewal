package com.example.banto.Payments;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payments, Long> {
	@Query("SELECT p FROM Payments p WHERE p.user.id = :userId AND p.payDate BETWEEN :startDate AND :endDate")
	Page<Payments> findAllByUserIdAndYear(
		@Param("userId") Long userId,
		@Param("startDate") LocalDateTime startDate,
		@Param("endDate") LocalDateTime endDate,
		Pageable pageable);
}
