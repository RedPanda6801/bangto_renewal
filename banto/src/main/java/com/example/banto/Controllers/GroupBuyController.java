package com.example.banto.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.banto.DAOs.AuthDAO;
import com.example.banto.DTOs.GroupBuyDTO;
import com.example.banto.DTOs.ResponseDTO;
import com.example.banto.JWTs.JwtUtil;
import com.example.banto.Services.GroupBuyService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api")
public class GroupBuyController {
	@Autowired
	GroupBuyService groupBuyService;
	
	// 현재 공동 구매 기간 조회 (무권한)
	@GetMapping("/group-buy/current-event")
	public ResponseEntity getCurrentEvent() throws Exception {
		try {
			ResponseDTO event = groupBuyService.getCurrentEvent();
			return ResponseEntity.ok().body(event);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 기간 추가 (관리자)
	@PostMapping("/manager/group-buy/add")
	public ResponseEntity addEvent(@RequestBody GroupBuyDTO groupBuyDTO) throws Exception {
		try {
			groupBuyService.addEvent(groupBuyDTO);
			return ResponseEntity.ok().body(null);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 관리자 OR 판매자만 조회 가능(전체 조회)
	@GetMapping("/group-buy/get-list")
	public ResponseEntity getEventList() throws Exception {
		try {
			ResponseDTO eventList = groupBuyService.getEventList();
			return ResponseEntity.ok().body(eventList);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// 물건 추가 가능한 날짜 조회 (판매자만)
	@GetMapping("/group-buy/get-choose-list")
	public ResponseEntity getChooseList() throws Exception {
		try {
			ResponseDTO eventList = groupBuyService.getChooseList();
			return ResponseEntity.ok().body(eventList);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// 올린 물건이 있는 이벤트만 조회 (판매자)
	@GetMapping("/group-buy/seller/get-list")
	public ResponseEntity getEventListToSeller() throws Exception {
		try {
			ResponseDTO eventList = groupBuyService.getEventListToSeller();
			return ResponseEntity.ok().body(eventList);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// 기간 추가 (관리자)
	@PostMapping("/manager/group-buy/delete")
	public ResponseEntity deleteEvent(@RequestBody GroupBuyDTO groupBuyDTO) throws Exception {
		try {
			groupBuyService.deleteEvent(groupBuyDTO);
			return ResponseEntity.ok().body(null);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
