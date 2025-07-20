package com.example.banto.DTOs;

import com.example.banto.Entitys.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupItemPayDTO {

	private Integer id;
	
	private Integer amount;
	
	private Integer pay;
	
	private DeliverType deliverInfo;

	private String itemTitle;

	private String address;

	private String optionInfo;

	private LocalDateTime soldDate;

	private Integer optionPk;

	private Integer itemPk;

	private Integer storePk;

	private Integer groupItemPk;
	@JsonIgnore
	private Users user;
	@JsonIgnore
	private Items item;

	private ArrayList<Integer> payIdList;
	
	public static GroupItemPayDTO toDTO(GroupItemPays entity) {
		return GroupItemPayDTO.builder()
				.id(entity.getId())
				.amount(entity.getAmount())
				.pay(entity.getPay())
				.deliverInfo(entity.getDeliverInfo())
				.itemTitle(entity.getItemTitle())
				.optionInfo(entity.getOptionInfo())
				.soldDate(entity.getSoldDate())
				.groupItemPk(entity.getGroupItemPk())
				.item(entity.getItem())
				.user(entity.getUser())
				.address(entity.getAddress())
				.build();
	}
}
