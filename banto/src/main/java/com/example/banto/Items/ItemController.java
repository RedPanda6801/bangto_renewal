package com.example.banto.Items;

import java.util.List;


import com.example.banto.Utils.PageDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ItemController {

	private final ItemService itemService;

	@PostMapping("/admin/item/add/test-data")
	public ResponseEntity<?> setTestDataWithFixtureMonkey(){
		itemService.setTestData();
		return ResponseEntity.ok().body("Create Success");
	}

	// 단일 물건 세부 조회
	@GetMapping("/item/get-detail/{itemId}")
	public ResponseEntity<?> getItemDetail(@PathVariable("itemId") Long itemId) {
		ItemDTO item = itemService.getItemDetail(itemId);
		return ResponseEntity.ok().body(item);
	}

	// 물품 전체 조회 (메인 페이지 조회)
	@GetMapping("/item/get-list/{page}")
	public ResponseEntity<?> getItemList(@PathVariable("page") Integer page) {
		PageDTO itemList = itemService.getItemList(page);
		return ResponseEntity.ok().body(itemList);
	}
	
	// 추천 상품 20개 조회
/*	@GetMapping("/item/get-list/recommend")
	public ResponseEntity<?> getRecommendItemList() {
		PageDTO itemList = itemService.getRecommendItemList();
		return ResponseEntity.ok().body(itemList);
	}*/

	// 필터링 검색
	// 물건 이름, 매장 이름, 카테고리, 가격 범위
	@GetMapping("/item/get-list/filtered")
	public ResponseEntity<?> getFilteredItemList(@ModelAttribute SearchDTO dto) {
		PageDTO itemList = itemService.getFilteredItemList(dto);
		return ResponseEntity.ok().body(itemList);
	}

	/*
	// 매장 별 물건 조회(20개 씩)
	@GetMapping("/item/get-list/store/{storeId}/{page}")
	public ResponseEntity<?> getItemList(@PathVariable("storeId") Long storeId, @PathVariable("page") Integer page) {
			PageDTO itemList = itemService.getItemListByStore(storeId, page);
			return ResponseEntity.ok().body(itemList);
	}

	// 매장이름 별 물건 조회(20개 씩)
	@GetMapping("/item/get-list/store-name/{storeName}/{page}")
	public ResponseEntity<?> getItemListByStoreName(@PathVariable("storeName") String storeName, @PathVariable("page") Integer page) throws Exception{
		PageDTO itemList = itemService.getItemListByStoreName(storeName, page);
		return ResponseEntity.ok().body(itemList);
	}

	// 물품 검색어 별 물건 조회(20개 씩)
	@GetMapping("/item/get-list/title/{title}/{page}")
	public ResponseEntity<?> getItemListByTitle(@PathVariable("title") String title, @PathVariable("page") Integer page) {
		PageDTO itemList = itemService.getItemListByTitle(title, page);
		return ResponseEntity.ok().body(itemList);
	}

	// 카테고리 별 물건 조회(20개 씩)
	@GetMapping("/item/get-by-category/{category}/{page}")
	public ResponseEntity<?> getItemListByCategory(@PathVariable("category") String category, @PathVariable("page") Integer page) throws Exception{
		try {
			ResponseDTO itemList = itemService.getItemListByCategory(category, page);
			return ResponseEntity.ok().body(itemList);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	 */

	// 물건 추가
	@PostMapping(path = "/seller/item/add-item", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> addItem(@Valid @RequestPart("dto") ItemDTO itemDTO, @RequestPart(name = "files", required = false) List<MultipartFile> files) {
		itemService.addItem(itemDTO, files);
		return ResponseEntity.ok().body("물건 추가에 성공했습니다.");
	}

	// 물건 수정
	@PutMapping(path = "/seller/item/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> updateItem(@RequestPart("dto") ItemDTO itemDTO, @RequestPart(name = "files", required = false) List<MultipartFile> files) {
			itemService.updateItem(itemDTO, files);
			return ResponseEntity.ok().body("물건 수정에 성공했습니다.");
	}

	// 아이템 삭제
	@PostMapping("/seller/item/delete")
	public ResponseEntity<?> deleteItem(@RequestBody ItemDTO itemDTO) {
		itemService.deleteItem(itemDTO);
		return ResponseEntity.ok().body("물건 삭제에 성공했습니다.");
	}
}
