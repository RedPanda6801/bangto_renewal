package com.example.banto.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.banto.DTOs.FavoriteDTO;
import com.example.banto.DTOs.ItemDTO;
import com.example.banto.DTOs.QNADTO;
import com.example.banto.DTOs.ResponseDTO;
import com.example.banto.JWTs.JwtUtil;
import com.example.banto.Services.QNAService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api")
public class QNAController {
	
	@Autowired
	QNAService qnaService;
	
	// 매장 별 QNA 전체 조회(판매자)
	// 답변 대기중인 QNA 우선 조회
	@GetMapping("/qna/store/get-list/{page}")
	public ResponseEntity getListByStore(@ModelAttribute QNADTO qnaDTO, @PathVariable("page") Integer page) throws Exception{
		try {
			ResponseDTO itemList = qnaService.getListByStore(qnaDTO, page);
			return ResponseEntity.ok().body(itemList);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

//	내 QNA 조회(고객)
	@GetMapping("/qna/my-list/{page}")
	public ResponseEntity getItemList(@PathVariable("page") Integer page) throws Exception{
		try {
			ResponseDTO favoriteList = qnaService.getMyList(page);
			return ResponseEntity.ok().body(favoriteList);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
		
	// QNA 세부 조회(판매자 or 고객)
	@GetMapping("/qna/get-detail")
	public ResponseEntity getQnaDetail(@ModelAttribute QNADTO qnaDTO) throws Exception{
		try {
			ResponseDTO itemList = qnaService.getQnaDetail(qnaDTO);
			return ResponseEntity.ok().body(itemList);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// QNA 추가(고객)
	@PostMapping("/qna/add")
	public ResponseEntity getItemList(@RequestBody QNADTO qnaDTO) throws Exception{
		try {
			qnaService.addQNA(qnaDTO);
			return ResponseEntity.ok().body(null);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// QNA 답변 추가(판매자)
	@PostMapping("/qna/add-answer")
	public ResponseEntity addSellerAnswer(@RequestBody QNADTO qnaDTO) throws Exception{
		try {
			qnaService.addSellerAnswer(qnaDTO);
			return ResponseEntity.ok().body(null);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// QNA 삭제
	@PostMapping("/qna/delete")
	public ResponseEntity deleteQNA(@RequestBody QNADTO qnadto) throws Exception{
		try {
			qnaService.deleteQNA(qnadto);
			return ResponseEntity.ok().body(null);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	//	아이템별 QNA 조회
	@GetMapping("/qna/item/get-list/{itemId}/{page}")
	public ResponseEntity getListByItem(@PathVariable("itemId") Integer itemId, @PathVariable("page") Integer page) throws Exception{
		try {
			ResponseDTO favoriteList = qnaService.getListByItem(itemId, page);
			return ResponseEntity.ok().body(favoriteList);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
