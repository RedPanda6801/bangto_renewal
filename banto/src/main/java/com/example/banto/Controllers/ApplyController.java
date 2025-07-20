package com.example.banto.Controllers;

import java.util.List;

import com.example.banto.DTOs.StoreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.banto.DAOs.AuthDAO;
import com.example.banto.DTOs.ApplyDTO;
import com.example.banto.DTOs.ProcessDTO;
import com.example.banto.DTOs.ResponseDTO;
import com.example.banto.JWTs.JwtUtil;
import com.example.banto.Services.ApplyService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api")
public class ApplyController {
	@Autowired
	ApplyService applyService;
	
	// 판매자 인증 신청
	@PostMapping("/apply")
	public ResponseEntity apply(@RequestBody StoreDTO storeDTO) {
		try {
			applyService.applySellerAuth(storeDTO);
			return ResponseEntity.ok().body("판매자 인증 신청 완료");
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 판매자 인증 신청서 조회(본인)
	@GetMapping("/apply/my-info")
	public ResponseEntity getMyApply() {
		try {
			ResponseDTO apply = applyService.getAuthInfo();
			return ResponseEntity.ok().body(apply);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 판매자 인증 신청서 처리(관리자)
	@PostMapping("/manager/apply/modify")
	public ResponseEntity modifyApply(@RequestBody ProcessDTO dto) {
		try {
			applyService.modify(dto);
			return ResponseEntity.ok().body("판매자 인증 신청서 처리 완료");
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 판매자 인증 신청서 목록 조회(20개씩, 관리자)
	@GetMapping("/manager/apply/get-list/{page}")
	public ResponseEntity getApplyList(@PathVariable("page") Integer page) {
		try {
			ResponseDTO applyList = applyService.getApplyList(page);
			return ResponseEntity.ok().body(applyList);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 판매자 인증 신청서 세부 조회(관리자)
	@GetMapping("/manager/apply/get-info/{sellerAuthId}")
	public ResponseEntity getApply(HttpServletRequest request, @PathVariable("sellerAuthId") Integer sellerAuthId) {
		try {
			ResponseDTO apply = applyService.getApply(sellerAuthId);
			return ResponseEntity.ok().body(apply);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
