package com.example.banto.Sellers;


import com.example.banto.Users.Users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerDTO {

	private Long sellerPk;

	private String email;

	private String name;

	private String addr;

	private String phone;

	private Boolean isBanned;

	private Long userPk;

	public static SellerDTO toDTO(Sellers entity) {
		return SellerDTO.builder()
			.sellerPk(entity.getId())
			.email(entity.getUser().getEmail())
			.name(entity.getUser().getName())
			.addr(entity.getUser().getAddr())
			.phone(entity.getUser().getPhone())
			.userPk(entity.getUser().getId())
			.isBanned(entity.getIsBanned())
			.build();
	}
}
