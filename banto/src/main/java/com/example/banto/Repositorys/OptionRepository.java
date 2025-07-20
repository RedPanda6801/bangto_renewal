package com.example.banto.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.banto.Entitys.Options;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OptionRepository extends JpaRepository<Options, Integer> {
}
