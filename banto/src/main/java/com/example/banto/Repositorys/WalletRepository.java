package com.example.banto.Repositorys;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallets, Integer> {

	Optional<Wallets> findByUser_Id(Integer userId);

}
