package com.example.banto.SellerAuths;

import java.time.LocalDateTime;

import com.example.banto.Enums.ApplyType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerAuthDTO {
    private Long id;
    private ApplyType auth;
    private LocalDateTime applyDate;
    private LocalDateTime signDate;
    private Long userPk;
    private String userName;
    private String storeName;
    private String busiNum;
    
   public static SellerAuthDTO toDTO(SellerAuths entity) {
        return SellerAuthDTO.builder()
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
