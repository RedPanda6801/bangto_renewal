package com.example.banto.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.banto.DAOs.AuthDAO;
import com.example.banto.DTOs.PayDTO;
import com.example.banto.DTOs.ResponseDTO;
import com.example.banto.DTOs.SoldItemDTO;
import com.example.banto.JWTs.JwtUtil;
import com.example.banto.Services.PayService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api")
public class PayController {
	@Autowired
	PayService payService;

	// 장바구니 결제(배송비까지 생각해서 금액 지불해야 함)
	@PostMapping("/pay")
	public ResponseEntity payCart(@RequestBody PayDTO dto) {
		try {
			payService.payCart(dto);
			return ResponseEntity.ok().body("장바구니 결제 완료");
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 개인 결제내역 확인
	@GetMapping("/pay/get-info/{year}/{page}")
	public ResponseEntity getMyPay(@PathVariable("year") Integer year, @PathVariable("page") Integer page) {
		try {
			ResponseDTO soldItemList = payService.getPayList(year, page);
			return ResponseEntity.ok().body(soldItemList);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 구매자 결제내역 확인(관리자)
	@GetMapping("/pay/get-user-info/{userId}/{year}/{page}")
	public ResponseEntity getUserPay( @PathVariable("userId") Integer userId,@PathVariable("year") Integer year, @PathVariable("page") Integer page) {
		try {
			ResponseDTO soldItemList = payService.getPayListForRoot(userId, year, page);
			return ResponseEntity.ok().body(soldItemList);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 판매물품 처리(판매자)
	// 배송중, 배송완료 처리
	// id, deliverInfo 필요
	@PostMapping("/pay/modify")
	public ResponseEntity modifySoldItem(@RequestBody SoldItemDTO dto) {
		try {
			payService.modifySoldItem(dto);
			return ResponseEntity.ok().body("구매/판매물품 처리 완료");
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 매장 판매내역 확인(판매자)
	@GetMapping("/pay/get-my-store-info/{storeId}/{page}")
	public ResponseEntity getMySold(@PathVariable("storeId") Integer storeId, @PathVariable("page") Integer page) {
		try {
			ResponseDTO soldItemList = payService.getSoldList(storeId, page);
			return ResponseEntity.ok().body(soldItemList);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 매장 판매내역 확인(관리자)
	@GetMapping("/pay/get-store-info/{storeId}/{page}")
	public ResponseEntity getStoreSold(@PathVariable("storeId") Integer storeId, @PathVariable("page") Integer page) {
		try {

			ResponseDTO soldItemList = payService.getSoldListForRoot(storeId, page);
			return ResponseEntity.ok().body(soldItemList);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
