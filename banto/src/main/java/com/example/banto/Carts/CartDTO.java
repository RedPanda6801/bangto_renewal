package com.example.banto.Carts;

import com.example.banto.Items.Items;
import com.example.banto.Options.Options;
import com.example.banto.Users.Users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDTO {
	private Long cartPk;
	private Items item;
	private Integer amount;
	private Options option;
	private Long optionPk;
	private Users user;
	private Integer totalPrice;
}
