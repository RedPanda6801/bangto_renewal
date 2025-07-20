package com.example.banto.Repositorys;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.banto.Entitys.Sellers;

public interface SellerRepository extends JpaRepository<Sellers, Integer> {

	Optional<Sellers> findByUser_Id(Integer userId);

}
