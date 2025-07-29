package com.example.banto.Stores;

import com.example.banto.Utils.PageDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class StoreController {

	private final StoreService storeService;
	
	// 매장 추가
	@PostMapping("/store/insert")
	public ResponseEntity<?> addStore(@Valid @RequestBody StoreDTO dto) {
		storeService.create(dto);
		return ResponseEntity.ok().body("매장 추가에 성공했습니다.");
	}

	// 내 매장 세부 조회
	@GetMapping("/store/{storeId}")
	public ResponseEntity<?> getStoreDetail(@PathVariable("storeId") Long storeId) {
		StoreDTO store = storeService.getStoreDetail(storeId);
		return ResponseEntity.ok().body(store);
	}

	// 내 매장 전체 조회
	@GetMapping("/store/get-list/{page}")
	public ResponseEntity<?> getStoreList(@PathVariable("page") Integer page) {
		PageDTO stores = storeService.getStoreList(page);
		return ResponseEntity.ok().body(stores);
	}

	// 매장 수정
	@PutMapping("/store")
	public ResponseEntity<?> updateStore(@RequestBody StoreDTO dto) {
		storeService.update(dto);
		return ResponseEntity.ok().body("수정에 성공했습니다.");
	}

	// 매장 삭제
	@DeleteMapping("/store")
	public ResponseEntity<?> deleteStore(@RequestBody StoreDTO dto) {
		storeService.delete(dto);
		return ResponseEntity.ok().body("삭제에 성공했습니다.");
	}
	
	// 매장 전체 조회(관리자)
	@GetMapping("/admin/store/get-list/{page}")
	public ResponseEntity<?> getStoreListForAdmin(@PathVariable("page") Integer page) {
		PageDTO stores = storeService.getStoreListForAdmin(page);
		return ResponseEntity.ok().body(stores);

	}
}
