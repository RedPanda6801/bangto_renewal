package com.example.banto.DTOs;

import com.example.banto.Entitys.Items;
import com.example.banto.Entitys.Options;
import com.example.banto.Entitys.Users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDTO {
	private Integer cartPk;
	private Items item;
	private Integer amount;
	private Options option;
	private Integer optionPk;
	private Users user;
	private Integer totalPrice;
}
