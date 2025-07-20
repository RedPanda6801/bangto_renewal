package com.example.banto.DTOs;

import java.time.LocalDateTime;

import com.example.banto.Entitys.DeliverType;
import com.example.banto.Entitys.Items;
import com.example.banto.Entitys.SellerAuths;
import com.example.banto.Entitys.SoldItems;
import com.example.banto.Entitys.Users;
import com.example.banto.Entitys.Wallets;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SoldItemDTO {
	private Integer id;
	private String itemName;
    private Integer amount;
    private String optionInfo;
    private Integer addPrice;
    private Integer itemPrice;
    private Integer soldPrice;
    private LocalDateTime soldDate;
    private DeliverType deliverInfo;
    private Integer itemPk;
    private Integer optionPk;
    private Users user;
    private Integer storePk;
    
    public static SoldItemDTO toDTO(SoldItems entity) {
        return SoldItemDTO.builder()
                .id(entity.getId())
                .addPrice(entity.getAddPrice())
                .amount(entity.getAmount())
                .deliverInfo(entity.getDeliverInfo())
                .itemName(entity.getItemName())
                .itemPk(entity.getItemPk())
                .itemPrice(entity.getItemPrice())
                .optionInfo(entity.getOptionInfo())
                .optionPk(entity.getOptionPk())
                .soldDate(entity.getSoldDate())
                .soldPrice(entity.getSoldPrice())
                .storePk(entity.getStorePk())
                .user(entity.getUser())
                .build();
    }
}
