package com.example.banto.Repositorys;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.banto.Entitys.SellerAuths;

@Repository
public interface ApplyRepository extends JpaRepository<SellerAuths, Integer> {
	@Query("SELECT s FROM SellerAuths s WHERE s.user.id = :userId and s.auth = 'Processing'")
	public Optional<SellerAuths> findProcessingAuthByUserId(@Param("userId") Integer userId);
	
	//@Query("SELECT s FROM SellerAuths s WHERE s.user.id = :userId and s.auth = 'Accepted'")
	//public Optional<SellerAuths> findAcceptedAuthByUserId(@Param("userId") Integer userId);
	
	@Query("SELECT s FROM SellerAuths s WHERE s.user.id = :userId")
	public List<SellerAuths> findAllByUserId(@Param("userId") Integer userId);

	public List<SellerAuths> findByBusiNum(String busiNum);
	
	//@Query("SELECT new com.example.banto.DTOs.ApplyDTO(s.id, s.auth, s.applyDate, s.signDate, (SELECT u.id FROM s.user u)) FROM SellerAuths s")
	//public List<ApplyDTO> findAllSellerAuths();
	
	//@Query("SELECT s.id, s.auth, s.applyDate, s.signDate, (SELECT u.id FROM s.user u) FROM SellerAuths s")
	//Page<ApplyDTO> getItemsByStoreId(Pageable pageable);
	
	//@Query("SELECT new com.example.banto.DTOs.ApplyDTO(s.id, s.auth, s.applyDate, s.signDate, (SELECT u.id FROM s.user u)) FROM SellerAuths s WHERE (SELECT u.id FROM s.user u) = :userId")
	//public List<ApplyDTO> findAllSellerAuthsById(@Param("userId") Integer userId);
}
