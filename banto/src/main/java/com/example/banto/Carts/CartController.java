package com.example.banto.Carts;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CartController {

	private final CartService cartService;
	
	// 장바구니 수정 및 추가
	@PostMapping("/cart/upsert")
	public ResponseEntity<?> upsertCart(@Valid @RequestBody CartDTO dto, HttpServletRequest request, HttpServletResponse response) {
		cartService.upsert(dto, request, response);
		return ResponseEntity.ok().body("장바구니 추가 완료");
	}
	
	// 장바구니 조회
	@GetMapping("/cart")
	public ResponseEntity<?> getCart(HttpServletRequest request, HttpServletResponse response) {
		List<CartDTO> cartList = cartService.getCart(request, response);
		return ResponseEntity.ok().body(cartList);
	}

	
	// 장바구니 삭제
	@DeleteMapping("/cart/delete/{optionId}")
	public ResponseEntity<?> deleteCart(@PathVariable("optionId") Long optionId, HttpServletRequest request, HttpServletResponse response) {
		cartService.delete(optionId, request, response);
		return ResponseEntity.ok().body("장바구니 삭제 완료");
	}
}
