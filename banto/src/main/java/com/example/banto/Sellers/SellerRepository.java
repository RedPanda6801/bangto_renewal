package com.example.banto.Sellers;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.banto.Sellers.Sellers;

public interface SellerRepository extends JpaRepository<Sellers, Long> {
	Optional<Sellers> findByUserId(Long userId);
}
