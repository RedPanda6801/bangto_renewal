package com.example.banto.DTOs;

import com.example.banto.Entitys.GroupBuyItems;
import com.example.banto.Entitys.GroupBuys;
import com.example.banto.Entitys.Items;
import com.example.banto.Entitys.Options;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupBuyItemDTO {

	private Integer id;

	private Integer limitPerBuyer;

	private Integer maxAmount;

	private Integer itemPk;

	private Integer eventPk;

	private Integer optionPk;
	
	private GroupBuys event;
	
	private Integer nowAmount;

	private Integer sellerPk;

	private Items item;
	
	private Options option;
	
	public static GroupBuyItemDTO toDTO(GroupBuyItems entity) {
		return GroupBuyItemDTO.builder()
				.id(entity.getId())
				.limitPerBuyer(entity.getLimitPerBuyer())
				.nowAmount(entity.getNowAmount())
				.maxAmount(entity.getMaxAmount())
				.item(entity.getItem())
				.sellerPk(entity.getSellerPk())
				.option(entity.getOption())
				.itemPk(entity.getItem().getId())
				.build();
	}
}
