package com.example.banto.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.banto.DTOs.ResponseDTO;
import com.example.banto.Services.SellerService;

import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class SellerController {
	@Autowired
	SellerService sellerService;
	
	// 판매자 본인 조회
	@GetMapping("/seller/get-info")
	public ResponseEntity getSeller() {
		try {
			ResponseDTO seller = sellerService.getSellerInfo();
			return ResponseEntity.ok().body(seller);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 판매자 권한 반납
	@PostMapping("/seller/delete-me")
	public ResponseEntity deleteMyself() {
		try {
			sellerService.deleteMyself();
			return ResponseEntity.ok().body("판매자 권한 반납 완료");
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 판매자 전체 정보 조회(관리자)
	@GetMapping("/manager/seller/get-list/{page}")
	public ResponseEntity getSellersForRoot(@PathVariable("page") Integer page) {
		try {
			ResponseDTO sellerList = sellerService.getSellerList(page);
			return ResponseEntity.ok().body(sellerList);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	// 판매자 단일 조회(관리자)
	@GetMapping("/manager/seller/get-info/{userId}")
	public ResponseEntity getSellerForRoot(@PathVariable("userId") Integer userId) {
		try {
			ResponseDTO seller = sellerService.getSellerInfoForRoot(userId);
			return ResponseEntity.ok().body(seller);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// 판매자 권한 박탈(관리자)
	@PostMapping("/manager/seller/delete/{userId}")
	public ResponseEntity deleteSeller(@PathVariable("userId") Integer userId) {
		try {
			sellerService.deleteSeller(userId);
			return ResponseEntity.ok().body("판매자 권한 박탈 완료");
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
