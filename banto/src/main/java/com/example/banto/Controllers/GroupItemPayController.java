package com.example.banto.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.banto.DAOs.AuthDAO;
import com.example.banto.DTOs.GroupBuyItemDTO;
import com.example.banto.DTOs.GroupItemPayDTO;
import com.example.banto.DTOs.ResponseDTO;
import com.example.banto.JWTs.JwtUtil;
import com.example.banto.Services.GroupItemPayService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api")
public class GroupItemPayController {
	@Autowired
	GroupItemPayService groupItemPayService;
	
	// 아이템 별로 결제 내역 조회(판매자만)
	@GetMapping("/group-pay/store/get-list")
	public ResponseEntity getPayListByStore(@ModelAttribute GroupItemPayDTO dto) {
		try {
			ResponseDTO payList = groupItemPayService.getPayListByStore(dto);
			return ResponseEntity.ok().body(payList);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// 내 결제 내역 조회
	@GetMapping("/group-pay/my/get-list/{year}")
	public ResponseEntity getMyGroupPayList(@PathVariable("year") Integer year) {
		try {
			ResponseDTO payList = groupItemPayService.getMyGroupPayList(year);
			return ResponseEntity.ok().body(payList);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}


	// 결제 처리
	@PostMapping("/group-pay/pay")
	public ResponseEntity payGroupItem(@RequestBody GroupItemPayDTO dto) {
		try {
			groupItemPayService.payGroupItem(dto);
			return ResponseEntity.ok().body(null);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// 배송 처리
	@PostMapping("/group-pay/delivering-check")
	public ResponseEntity deliveringCheck(@RequestBody GroupItemPayDTO dto) {
		try {
			groupItemPayService.deliveringCheck(dto);
			return ResponseEntity.ok().body(null);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/group-pay/delivering-check-total")
	public ResponseEntity deliveringCheckTotal(@RequestBody GroupItemPayDTO dto) {
		try {
			groupItemPayService.deliveringCheckTotal(dto);
			return ResponseEntity.ok().body(null);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// 배송 완료 처리
	@PostMapping("/group-pay/delivered-check")
	public ResponseEntity deliveredCheck(@RequestBody GroupItemPayDTO dto) {
		try {
			groupItemPayService.deliveredCheck(dto);
			return ResponseEntity.ok().body(null);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
