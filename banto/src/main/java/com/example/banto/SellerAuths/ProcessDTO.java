package com.example.banto.SellerAuths;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProcessDTO {
    @NotNull
	private Long sellerAuthPk;
    @NotNull
    private String process;
}
