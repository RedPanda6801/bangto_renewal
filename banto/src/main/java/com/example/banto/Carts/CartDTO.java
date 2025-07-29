package com.example.banto.Carts;

import com.example.banto.Items.Items;
import com.example.banto.Options.Options;
import com.example.banto.Users.Users;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDTO {
	private Long id;
	@NotNull
	private Long optionPk;
	@NotNull
	private Integer amount;
	private Integer totalPrice;
	private String title;
	private String optionInfo;

	public static CartDTO toDTO(Options option, CartItem cartItem){
		return CartDTO.builder()
			.amount(cartItem.getAmount())
			.optionInfo(option.getOptionInfo())
			.title(option.getItem().getTitle())
			.totalPrice(option.getAddPrice() + option.getItem().getPrice())
			.build();
	}
}
