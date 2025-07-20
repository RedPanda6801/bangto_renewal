package com.example.banto.Controllers;

import java.util.List;

import com.example.banto.DAOs.AuthDAO;
import com.example.banto.JWTs.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.banto.DTOs.GroupBuyDTO;
import com.example.banto.DTOs.GroupBuyItemDTO;
import com.example.banto.DTOs.ResponseDTO;
import com.example.banto.Services.GroupBuyItemService;

@Controller
@RequestMapping("/api")
public class GroupBuyItemController {
	@Autowired
	GroupBuyItemService groupBuyItemService;

	// 이벤트의 공동 구매 물건 조회
	@GetMapping("/group-item/event/get-list/{eventId}")
	public ResponseEntity getCurrentEvent(@PathVariable("eventId") Integer eventId) throws Exception {
		try {
			ResponseDTO event = groupBuyItemService.getCurrentItemList(eventId);
			return ResponseEntity.ok().body(event);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// 공동 구매 이벤트에 물건 추가(판매자만) - 옵션 별로 추가해야 함
	@PostMapping("/group-item/add")
	public ResponseEntity addGroupItem(@RequestBody GroupBuyItemDTO groupBuyItemDTO) throws Exception{
		try{
			groupBuyItemService.addItem(groupBuyItemDTO);
			return ResponseEntity.ok().body(null);
		}catch(Exception e){
			return  ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// 공동 구매 이벤트에 올린 물건 수정(판매자만)
	@PostMapping("/group-item/modify")
	public ResponseEntity modifyGroupItem(@RequestBody GroupBuyItemDTO groupBuyItemDTO) throws Exception{
		try{
			groupBuyItemService.modifyGroupItem(groupBuyItemDTO);
			return ResponseEntity.ok().body(null);
		}catch(Exception e){
			return  ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// 공동 구매 이벤트에 올린 물건 수정(판매자만)
	@PostMapping("/group-item/delete")
	public ResponseEntity deleteGroupItem(@RequestBody GroupBuyItemDTO groupBuyItemDTO) throws Exception{
		try{
			groupBuyItemService.deleteGroupItem(groupBuyItemDTO);
			return ResponseEntity.ok().body(null);
		}catch(Exception e){
			return  ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
