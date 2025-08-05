package com.example.banto.SellerAuths;

import java.util.List;
import java.util.Optional;

import com.example.banto.Enums.ApplyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.banto.SellerAuths.SellerAuths;

@Repository
public interface SellerAuthRepository extends JpaRepository<SellerAuths, Long> {
	@Query("SELECT s FROM SellerAuths s WHERE s.user.id = :userId AND s.auth = :auth")
	public Optional<SellerAuths> findProcessingByUserId(@Param("userId") Long userId, @Param("auth") ApplyType auth);

	public List<SellerAuths> findAllByUserId(Long userId);

	public List<SellerAuths> findByBusiNum(String busiNum);
}
