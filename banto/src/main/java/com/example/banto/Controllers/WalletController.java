package com.example.banto.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.banto.DTOs.ResponseDTO;
import com.example.banto.Services.WalletService;

@Controller
@RequestMapping("/api")
public class WalletController {
	@Autowired
	WalletService walletService;
	
	// 내 지갑 조회
	@GetMapping("/wallet/my/get-info")
	public ResponseEntity getWallet() {
		try {
			ResponseDTO wallet = walletService.getMyWallet();
			return ResponseEntity.ok().body(wallet);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 사용자 지갑 조회(관리자)
	@GetMapping("/wallet/manager/get-info/{userId}")
	public ResponseEntity getWalletByManager(@PathVariable("userId") Integer userId) {
		try {
			ResponseDTO wallet = walletService.getMyWalletForRoot(userId);
			return ResponseEntity.ok().body(wallet);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 캐시 충전(본인)
	// cash 필요
	@PostMapping("/wallet/my/charge")
	public ResponseEntity chargeCash(@RequestBody WalletDTO dto) {
		try {
			walletService.chargeMyWallet(dto);
			return ResponseEntity.ok().body("캐시 충전 완료");
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}	
	
	// 지갑 수정(관리자)
	// 캐시, 캐시백을 추가하는 게 아니라 수치를 수정하는 기능
	// walletPk와, cash 혹은 cashBack 필요
	@PostMapping("/wallet/manager/modify")
	public ResponseEntity modifyWallet(@RequestBody WalletDTO dto) {
		try {
			walletService.modifyWallet(dto);
			return ResponseEntity.ok().body("지갑 조정 완료");
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}	
}
