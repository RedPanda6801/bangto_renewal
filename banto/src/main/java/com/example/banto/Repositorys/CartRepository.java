package com.example.banto.Repositorys;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.banto.Entitys.Carts;

public interface CartRepository extends JpaRepository<Carts, Integer> {
	@Query("SELECT c FROM Carts c WHERE c.user.id = :userId")
	public List<Carts> findAllByUserId(@Param("userId") Integer userId);

	//@Query("SELECT c FROM Carts c WHERE c.item.id = :itemId")
	//public List<Carts> findAllByItemId(@Param("itemId") Integer itemId);
}
