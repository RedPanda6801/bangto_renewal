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
import com.example.banto.Stores.StoreDTO;
import com.example.banto.Services.StoreService;

@Controller
@RequestMapping("/api")
public class StoreController {
	@Autowired
	StoreService storeService;
	
	// 매장 추가
	@PostMapping("/store/add")
	public ResponseEntity addStore(@RequestBody StoreDTO dto) throws Exception {
		try {
			storeService.create(dto);
			return ResponseEntity.ok().body(null);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 내 매장 전체 조회
	@GetMapping("/store/get-list")
	public ResponseEntity getStoreList() throws Exception {
		try {

			ResponseDTO stores = storeService.getMyStores();
			return ResponseEntity.ok().body(stores);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 내 매장 세부 조회
	@GetMapping("/store/get-detail/{storeId}")
	public ResponseEntity getStoreDetail(@PathVariable("storeId") Integer storeId) throws Exception {
		try {
			ResponseDTO store = storeService.getStoreDetail(storeId);
			return ResponseEntity.ok().body(store);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// 매장 수정
	@PostMapping("/store/modify")
	public ResponseEntity modifyStore(@RequestBody StoreDTO dto) throws Exception {
		try {
			storeService.modify(dto);
			return ResponseEntity.ok().body(null);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// 매장 삭제
	@PostMapping("/store/delete")
	public ResponseEntity deleteStore(@RequestBody StoreDTO dto) throws Exception {
		try {
			storeService.delete(dto);
			return ResponseEntity.ok().body(null);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 매장 전체 조회(관리자)
	@GetMapping("/manager/store/get-list/{page}")
	public ResponseEntity getStoreListByRoot(@PathVariable("page") Integer page) throws Exception {
		try {
			ResponseDTO stores = storeService.getMyStoresByRoot(page);
			return ResponseEntity.ok().body(stores);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 관리자 매장 수정
	@PostMapping("/manager/store/modify")
	public ResponseEntity modifyStoreByRoot(@RequestBody StoreDTO dto) throws Exception {
		try {
			storeService.modifyForRoot(dto);
			return ResponseEntity.ok().body(null);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 매장 삭제
	@PostMapping("/manager/store/delete")
	public ResponseEntity deleteStoreByRoot(@RequestBody StoreDTO dto) throws Exception {
		try {
			storeService.deleteByRoot(dto);
			return ResponseEntity.ok().body(null);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
