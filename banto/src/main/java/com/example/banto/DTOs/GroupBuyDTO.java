package com.example.banto.DTOs;

import java.time.LocalDateTime;
import java.util.List;

import com.example.banto.Entitys.GroupBuyItems;
import com.example.banto.Entitys.GroupBuys;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupBuyDTO {

	private Integer id;
	
	private LocalDateTime startDate;

	private LocalDateTime endDate;
	
	private List<GroupBuyItems> items;
	
	public static GroupBuyDTO toDTO(GroupBuys entity) {
		return GroupBuyDTO.builder()
				.id(entity.getId())
				.startDate(entity.getStartDate())
				.endDate(entity.getEndDate())
				.build();
	}
}
