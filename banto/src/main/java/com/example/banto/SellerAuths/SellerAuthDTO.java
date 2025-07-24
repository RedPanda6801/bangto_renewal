package com.example.banto.SellerAuths;

import java.time.LocalDateTime;

import com.example.banto.Enums.ApplyType;

import jakarta.validation.constraints.NotNull;
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

    private String userName;
    @NotNull
    private String storeName;
    @NotNull
    private String busiNum;

    public static SellerAuthDTO toSimpleDTO(SellerAuths entity){
        return SellerAuthDTO.builder()
            .id(entity.getId())
            .auth(entity.getAuth())
            .applyDate(entity.getApplyDate())
            .userName(entity.getUser().getName())
            .build();
    }

    public static SellerAuthDTO toDTO(SellerAuths entity) {
        return SellerAuthDTO.builder()
            .id(entity.getId())
            .auth(entity.getAuth())
            .applyDate(entity.getApplyDate())
            .signDate(entity.getSignDate())
            .userName(entity.getUser().getName())
            .storeName(entity.getStoreName())
            .busiNum(entity.getBusiNum())
            .build();
    }
}
