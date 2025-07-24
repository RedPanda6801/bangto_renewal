package com.example.banto.Sellers;

import com.example.banto.DTOs.PageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.banto.DTOs.ResponseDTO;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class SellerController {

	private final SellerService sellerService;
	
	// 판매자 본인 조회
	@GetMapping("/seller")
	public ResponseEntity<?> getSeller() {
		SellerDTO seller = sellerService.getSeller();
		return ResponseEntity.ok().body(seller);
	}
	// 판매자 단일 조회(관리자)
	@GetMapping("/admin/seller/{userId}")
	public ResponseEntity<?> getSellerForAdmin(@PathVariable("userId") Long userId) {
		SellerDTO seller = sellerService.getSellerForAdmin(userId);
		return ResponseEntity.ok().body(seller);
	}
	// 판매자 전체 정보 조회(관리자)
	@GetMapping("/admin/seller/get-list/{page}")
	public ResponseEntity<?> getSellerListForAdmin(@PathVariable("page") Integer page) {
		PageDTO sellerList = sellerService.getSellerList(page);
		return ResponseEntity.ok().body(sellerList);
	}

	// 판매자의 블랙리스트 추가
	@PostMapping("/admin/seller/banned/{userId}")
	public ResponseEntity<?> bannedByAdmin(@PathVariable("userId") Long userId) {
		sellerService.bannedByAdmin(userId);
		return ResponseEntity.ok().body("블랙리스트 추가 완료");
	}
	// 판매자 권한 반납
	@DeleteMapping("/seller")
	public ResponseEntity<?> deleteMyself() {
		sellerService.delete();
		return ResponseEntity.ok().body("판매자 권한 반납 완료");
	}
}
