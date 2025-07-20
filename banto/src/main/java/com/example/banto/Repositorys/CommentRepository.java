package com.example.banto.Repositorys;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.banto.Entitys.Comments;

public interface CommentRepository extends JpaRepository<Comments, Integer> {
	@Query("SELECT c FROM Comments c WHERE c.item.id = :itemId")
	Page<Comments> findCommentsByItemId(@Param("itemId") Integer itemId, Pageable pageable);
	
	@Query("SELECT c FROM Comments c WHERE c.user.id = :userId")
	Page<Comments> findCommentsByUserId(@Param("userId") Integer userId, Pageable pageable);
}
