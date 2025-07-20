package com.example.banto.DTOs;

import java.time.LocalDateTime;

import com.example.banto.Entitys.ApplyType;
import com.example.banto.Entitys.SellerAuths;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplyDTO {
    private Integer id;
    private ApplyType auth;
    private LocalDateTime applyDate;
    private LocalDateTime signDate;
    private Integer userPk;
    private String userName;
    private String storeName;
    private String busiNum;
    
   public static ApplyDTO toDTO(SellerAuths entity) {
        return ApplyDTO.builder()
                .id(entity.getId())
                .auth(entity.getAuth())
                .applyDate(entity.getApplyDate())
                .signDate(entity.getSignDate())
                .userPk(entity.getUser().getId())
                .userName(entity.getUser().getName())
                .storeName(entity.getStoreName())
                .busiNum(entity.getBusiNum())
                .build();
    }
}
