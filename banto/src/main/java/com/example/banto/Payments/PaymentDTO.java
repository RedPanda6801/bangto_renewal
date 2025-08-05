package com.example.banto.Payments;

import java.time.LocalDateTime;
import java.util.List;

import com.example.banto.Carts.CartDTO;
import com.example.banto.Utils.DTOMapper;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDTO {

	@NotNull
	private List<CartDTO> cartList;

	private List<SoldItemDTO> soldItemList;

	private Integer totalPrice;

	private LocalDateTime payDate;

	public static PaymentDTO toDTO(Payments payment){
		return PaymentDTO.builder()
			.soldItemList(DTOMapper.convertList(payment.getSoldItems().stream(), SoldItemDTO::toDTO))
			.payDate(payment.getPayDate())
			.totalPrice(payment.getTotalPrice())
			.build();
	}
}
