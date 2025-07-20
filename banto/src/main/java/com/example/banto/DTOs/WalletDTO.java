package com.example.banto.DTOs;

import com.example.banto.Entitys.Users;
import com.example.banto.Entitys.Wallets;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalletDTO {
    private Integer id;

    private Integer cash;

    private Integer cashBack;

    private Integer userPk;

    private Users user;
    
    // 관리자가 조정할 지갑의 PK
    @JsonIgnore
    private Integer walletPk;
    
    public static WalletDTO toDTO(Wallets entity) {
        return WalletDTO.builder()
                .id(entity.getId())
                .cash(entity.getCash())
                .cashBack(entity.getCashBack())
                .user(entity.getUser())
                .build();
    }
}