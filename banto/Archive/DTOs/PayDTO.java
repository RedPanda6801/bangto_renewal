package com.example.banto.Archive.DTOs;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayDTO {
	private List<Integer> cartPks;
	private Integer usingCashBack;
}
