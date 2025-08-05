package com.example.banto.Payments;

import com.example.banto.Utils.PageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PaymentController {
	@Autowired
	private final PayService payService;

	@PostMapping("/payment")
	public ResponseEntity<?> payCart(@RequestBody PaymentDTO dto) {
		payService.payCart(dto);
		return ResponseEntity.ok().body("장바구니 결제 완료");
	}
	
	// 개인 결제내역 확인
	@GetMapping("/payment/get-list/{year}/{page}")
	public ResponseEntity<?> getMyPay(@PathVariable("year") Integer year, @PathVariable("page") Integer page) {
		PageDTO soldItemList = payService.getPaymentList(year, page);
		return ResponseEntity.ok().body(soldItemList);
	}
	
	// 구매자 결제내역 확인(관리자)
	@GetMapping("/payment/admin/get-list/{userId}/{year}/{page}")
	public ResponseEntity<?> getUserPay( @PathVariable("userId") Long userId,@PathVariable("year") Integer year, @PathVariable("page") Integer page) {
		PageDTO soldItemList = payService.getPaymentListForAdmin(userId, year, page);
		return ResponseEntity.ok().body(soldItemList);
	}
	
	// 판매물품 일괄 처리(판매자) -> 배송중, 배송완료 처리
	@PostMapping("/payment/process/{deliver}")
	public ResponseEntity<?> processPayment(@RequestBody List<SoldItemDTO> dto, @PathVariable("deliver") String deliver) {
			payService.processPayment(dto, deliver);
			return ResponseEntity.ok().body("구매/판매물품 처리 완료");
	}
	
	// 매장 판매내역 확인(판매자)
	@GetMapping("/payment/seller/get-store-list/{storeId}/{page}")
	public ResponseEntity<?> getSoldListForSeller(@PathVariable("storeId") Long storeId, @PathVariable("page") Integer page) {
			PageDTO soldItemList = payService.getSoldListForSeller(storeId, page);
			return ResponseEntity.ok().body(soldItemList);
	}
	
	// 매장 판매내역 확인(관리자)
	@GetMapping("/payment/admin/get-list/{storeId}/{page}")
	public ResponseEntity<?> getSoldListForAdmin(@PathVariable("storeId") Long storeId, @PathVariable("page") Integer page) {
		PageDTO soldItemList = payService.getSoldListForAdmin(storeId, page);
		return ResponseEntity.ok().body(soldItemList);
	}
}
