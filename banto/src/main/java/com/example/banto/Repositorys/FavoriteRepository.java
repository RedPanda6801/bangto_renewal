package com.example.banto.Repositorys;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.banto.Entitys.Favorites;

public interface FavoriteRepository extends JpaRepository<Favorites, Integer>{
	@Query("SELECT f FROM Favorites f WHERE f.user.id = :userId AND f.item.id = :itemId")
	Optional<Favorites> findByUserAndItem(@Param("userId") Integer userId, @Param("itemId") Integer itemId);
	
	@Query("SELECT f FROM Favorites f WHERE f.user.id = :userId")
	Page<Favorites> findAllByUserId(@Param("userId") Integer userId, Pageable page);
}
