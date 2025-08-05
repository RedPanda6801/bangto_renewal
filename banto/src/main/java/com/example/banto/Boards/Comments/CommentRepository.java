package com.example.banto.Boards.Comments;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comments, Long> {
	//@Query("SELECT c FROM Comments c WHERE c.item.id = :itemId")
	//Page<Comments> findCommentsByItemId(@Param("itemId") Long itemId, Pageable pageable);
}
