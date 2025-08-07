package com.example.banto.SellerAuths;

import com.example.banto.Utils.PageDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SellerAuthController {

	private final SellerAuthService sellerAuthService;
	
	// 판매자 인증 신청
	@PostMapping("/seller-auth")
	public ResponseEntity<?> apply(@Valid @RequestBody SellerAuthDTO sellerAuthDTO) {
		sellerAuthService.apply(sellerAuthDTO);
		return ResponseEntity.ok().body("판매자 인증 신청 완료");
	}
	
	// 판매자 인증 신청서 조회(본인)
	@GetMapping("/seller-auth/get-list")
	public ResponseEntity<?> getApplyList() {
		List<SellerAuthDTO> applyList = sellerAuthService.getSellerAuthList();
		return ResponseEntity.ok().body(applyList);
	}
	
	// 판매자 인증 신청서 처리(관리자)
	@PutMapping("/admin/seller-auth/process")
	public ResponseEntity<?> processApply(@Valid @RequestBody ProcessDTO dto) {
		sellerAuthService.process(dto);
		return ResponseEntity.ok().body("인증서 처리 완료");
	}
	
	// 판매자 인증 신청서 목록 조회(20개씩, 관리자)
	@GetMapping("/admin/seller-auth/get-list/{page}")
	public ResponseEntity<?> getApplyList(@PathVariable("page") Integer page) {
		PageDTO applyList = sellerAuthService.getApplyListForAdmin(page);
		return ResponseEntity.ok().body(applyList);
	}
	
	// 판매자 인증 신청서 세부 조회(관리자)
	@GetMapping("/admin/seller-auth/{sellerAuthId}")
	public ResponseEntity<?> getApplyForAdmin(@PathVariable("sellerAuthId") Long sellerAuthId) {
		SellerAuthDTO sellerAuthDTO = sellerAuthService.getApplyForAdmin(sellerAuthId);
		return ResponseEntity.ok().body(sellerAuthDTO);
	}
}
