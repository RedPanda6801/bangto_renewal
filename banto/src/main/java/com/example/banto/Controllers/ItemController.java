package com.example.banto.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.example.banto.Items.ItemDTO;
import com.example.banto.Options.OptionDTO;
import com.example.banto.DTOs.ResponseDTO;
import com.example.banto.Services.ItemService;

@Controller
@RequestMapping("/api")
public class ItemController {
	@Autowired
	ItemService itemService;
	
	// 물품 전체 조회
	@GetMapping("/item/get-all-list/{page}")
	public ResponseEntity getItemList(@PathVariable("page") Integer page) throws Exception{
		try {
			ResponseDTO itemList = itemService.getAllItemList(page);
			return ResponseEntity.ok().body(itemList);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 추천 상품 20개 조회
	@GetMapping("/item/get-recommend-list")
	public ResponseEntity getRecommendItemList() throws Exception{
		try {
			ResponseDTO itemList = itemService.getRecommendItemList();
			return ResponseEntity.ok().body(itemList);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 종합 검색 기능(물품 이름, 매장 이름, 카테고리 검색어별 물건 조회/가격순 정렬)
	@GetMapping("/item/get-filtered-list")
	public ResponseEntity getFilterdItemList(@ModelAttribute ItemDTO dto) throws Exception{
		try {
			ResponseDTO itemList = itemService.getFilterdItemList(dto);
			return ResponseEntity.ok().body(itemList);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 물품 검색어 별 물건 조회(20개 씩)
	@GetMapping("/item/get-by-title/{title}/{page}")
	public ResponseEntity getItemListByTitle(@PathVariable("title") String title, @PathVariable("page") Integer page) throws Exception{
		try {
			ResponseDTO itemList = itemService.getItemListByTitle(title, page);
			return ResponseEntity.ok().body(itemList);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// 매장 검색어 별 물건 조회(20개 씩)
	@GetMapping("/item/get-by-store-name/{storeName}/{page}")
	public ResponseEntity getItemListByStoreName(@PathVariable("storeName") String storeName, @PathVariable("page") Integer page) throws Exception{
		try {
			ResponseDTO itemList = itemService.getItemListByStoreName(storeName, page);
			return ResponseEntity.ok().body(itemList);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 카테고리 별 물건 조회(20개 씩)
	@GetMapping("/item/get-by-category/{category}/{page}")
	public ResponseEntity getItemListByCategory(@PathVariable("category") String category, @PathVariable("page") Integer page) throws Exception{
		try {
			ResponseDTO itemList = itemService.getItemListByCategory(category, page);
			return ResponseEntity.ok().body(itemList);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 매장 별 물건 조회(20개 씩)
	@GetMapping("/item/get-itemlist/{storeId}/{page}")
	public ResponseEntity getItemList(@PathVariable("storeId") Integer storeId, @PathVariable("page") Integer page) throws Exception{
		try {
			ResponseDTO itemList = itemService.getItemList(storeId, page);
			return ResponseEntity.ok().body(itemList);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	// 단일 물건 세부 조회
	@GetMapping("/item/get-detail/{itemId}")
	public ResponseEntity getItemDetail(@PathVariable("itemId") Integer itemId) throws Exception{
		try {
			ResponseDTO item = itemService.getItemDetail(itemId);
			return ResponseEntity.ok().body(item);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	// 물건 추가 
	@PostMapping(path = "/item/add-item", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity addItem(@RequestPart("dto") ItemDTO itemDTO, @RequestPart(name = "files", required = false) List<MultipartFile> files) throws Exception {
		try {
			itemService.addItem(itemDTO, files);
			return ResponseEntity.ok().body(null);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	// 물건 수정
	@PostMapping(path = "/item/modify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity modifyItem(@RequestPart("dto") ItemDTO itemDTO, @RequestPart(name = "files", required = false) List<MultipartFile> files) throws Exception {
		try {
			itemService.modifyItem(itemDTO, files);
			return ResponseEntity.ok().body(null);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	// 아이템 삭제
	@PostMapping("/item/delete")
	public ResponseEntity deleteItem(@RequestBody ItemDTO itemDTO) throws Exception {
		try {
			itemService.deleteItem(itemDTO);
			return ResponseEntity.ok().body(null);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	// 옵션 추가
	@PostMapping("/item/option/add")
	public ResponseEntity AddItemOption(@RequestBody OptionDTO optionDTO) throws Exception {
		try {
			System.out.println(optionDTO);
			itemService.addItemOption(optionDTO);
			return ResponseEntity.ok().body(null);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// 옵션 수정
	@PostMapping("/item/option/modify")
	public ResponseEntity modifyItemOption(@RequestBody OptionDTO optionDTO) throws Exception {
		try {
			itemService.modifyItemOption(optionDTO);
			return ResponseEntity.ok().body(null);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	// 옵션 삭제
	@PostMapping("/item/option/delete")
	public ResponseEntity deleteOption(@RequestBody OptionDTO optionDTO) throws Exception {
		try {
			itemService.deleteOption(optionDTO);
			return ResponseEntity.ok().body(null);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
