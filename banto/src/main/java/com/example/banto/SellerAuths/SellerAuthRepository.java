package com.example.banto.SellerAuths;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.banto.SellerAuths.SellerAuths;

@Repository
public interface SellerAuthRepository extends JpaRepository<SellerAuths, Long> {
	@Query("SELECT s FROM SellerAuth s WHERE s.user.id == :userId AND s.auth = 'Processing'")
	public Optional<SellerAuths> findProcessingByUserId(@Param("userId") Long userId);

	public List<SellerAuths> findAllByUserId(Long userId);

	public List<SellerAuths> findByBusiNum(String busiNum);
}
