package com.example.banto.Entitys;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.example.banto.DTOs.ApplyDTO;
import com.example.banto.DTOs.StoreDTO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerAuths {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name="AUTH", nullable=false)
    @Enumerated(EnumType.STRING)  // Enum 값을 문자열로 저장
    private ApplyType auth;
    
    @Column(name="APPLY_DATE", nullable=false)
    private LocalDateTime applyDate;
    
    @Column(name="SIGN_DATE")
    private LocalDateTime signDate;

    @Column(name="STORE_NAME")
    private String storeName;

    @Column(name="BUSI_NUM")
    private String busiNum;

    @ManyToOne
    @JoinColumn(name="USER_PK")
    private Users user;
    
    /*public static SellerAuths toEntity(ApplyDTO dto) {
        return SellerAuths.builder()
                .id(dto.getId())
                .auth(dto.getAuth())
                .applyDate(dto.getApplyDate())
                .signDate(dto.getSignDate())
                .build();
    }*/
}
