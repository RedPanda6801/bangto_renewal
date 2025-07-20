package com.example.banto.DTOs;

import java.util.List;

import com.example.banto.Entitys.Favorites;
import com.example.banto.Entitys.Items;
import com.example.banto.Entitys.SellerAuths;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavoriteDTO {
	private Integer id;
	private Integer userPk;
	private Items item;
	
	public static FavoriteDTO toDTO(Favorites entity) {
		return FavoriteDTO.builder()
				.id(entity.getId())
				.item(entity.getItem())
				.userPk(entity.getUser().getId())
				.build();
	}
}
