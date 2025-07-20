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

import com.example.banto.DTOs.CartDTO;
import com.example.banto.DTOs.ResponseDTO;
import com.example.banto.JWTs.JwtUtil;
import com.example.banto.Services.CartService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api")
public class CartController {
	@Autowired
	CartService cartService;
	
	// 장바구니 추가
	@PostMapping("/cart/add")
	public ResponseEntity addCart(@RequestBody CartDTO dto) {
		try {
			cartService.addCart(dto);
			return ResponseEntity.ok().body("장바구니 추가 완료");
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 장바구니 조회
	@GetMapping("/cart/get-info")
	public ResponseEntity readCart() {
		try {
			ResponseDTO cartList = cartService.readCart();
			return ResponseEntity.ok().body(cartList);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 장바구니 수정
	@PostMapping("/cart/modify")
	public ResponseEntity modifyCart(@RequestBody CartDTO dto) {
		try {
			cartService.modifyCart(dto);
			return ResponseEntity.ok().body("장바구니 수정 완료");
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 장바구니 삭제
	@PostMapping("/cart/delete")
	public ResponseEntity deleteCart(@RequestBody CartDTO dto) {
		try {
			cartService.deleteCart(dto);
			return ResponseEntity.ok().body("장바구니 삭제 완료");
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
