package com.example.banto.Entitys;

import com.example.banto.DTOs.WalletDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Wallets {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="CASH")
    private Integer cash;

    @Column(name="CASH_BACK")
    private Integer cashBack;

    @OneToOne
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinColumn(name = "OWNER_PK")
    private Users user;

    public static Wallets toEntity(WalletDTO dto) {
        return Wallets.builder()
                .id(dto.getId())
                .cash((dto.getCash() != null) ? dto.getCash() : 0)
                .cashBack((dto.getCashBack() != null) ? dto.getCashBack() : 0)
                .user(dto.getUser())
                .build();
    }
}
