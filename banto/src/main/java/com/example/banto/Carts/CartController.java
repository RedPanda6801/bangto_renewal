package com.example.banto.Carts;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class CartController {

	private final CartService cartService;
	
	// 장바구니 수정 및 추가
	@PostMapping("/cart/upsert")
	public ResponseEntity<?> upsertCart(@Valid @RequestBody CartDTO dto, HttpServletRequest request) {
		cartService.upsert(dto, request);
		return ResponseEntity.ok().body("장바구니 추가 완료");
	}
	
	// 장바구니 조회
	@GetMapping("/cart")
	public ResponseEntity<?> getCart(HttpServletRequest request) {
		List<CartDTO> cartList = cartService.getCart(request);
		return ResponseEntity.ok().body(cartList);
	}

	
	// 장바구니 삭제
	@DeleteMapping("/cart/delete/{optionId}")
	public ResponseEntity<?> deleteCart(@PathVariable("optionId") Long optionId, HttpServletRequest request) {
		cartService.delete(optionId, request);
		return ResponseEntity.ok().body("장바구니 삭제 완료");
	}
}
