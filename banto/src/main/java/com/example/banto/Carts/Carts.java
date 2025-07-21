package com.example.banto.Carts;

import com.example.banto.Items.Items;
import com.example.banto.Options.Options;
import com.example.banto.Users.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Carts {
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="AMOUNT", nullable=false)
	private Integer amount;

	private Integer totalPrice;

	@ManyToOne
	@JoinColumn(name="BUYER_PK")
	private Users user;
	
	@ManyToOne
	@JoinColumn(name="ITEM_PK")
	private Items item;
	
	@ManyToOne
	@JoinColumn(name="OPTION_PK")
	private Options option;
}
