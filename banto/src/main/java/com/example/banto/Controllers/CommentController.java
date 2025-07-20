package com.example.banto.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.example.banto.DTOs.CommentDTO;
import com.example.banto.DTOs.ResponseDTO;
import com.example.banto.JWTs.JwtUtil;
import com.example.banto.Services.CommentService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api")
public class CommentController {
	@Autowired
	CommentService commentService;
	
	// 후기 작성
	@PostMapping(path = "/comment/write", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity writeComment(@RequestPart("dto") CommentDTO dto, @RequestPart(name = "files", required = false) List<MultipartFile> files) {
		try {
			commentService.writeComment(dto, files);
			return ResponseEntity.ok().body("후기 작성 완료");
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 물품별 후기 목록 조회
	@GetMapping("/comment/item/{itemId}/{page}")
	public ResponseEntity getItemComment(@PathVariable("itemId") Integer itemId, @PathVariable("page") Integer page) {
		try {
			ResponseDTO comments = commentService.getItemComment(itemId, page);
			return ResponseEntity.ok().body(comments);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 후기 세부 조회
	@GetMapping("/comment/get/{commentId}")
	public ResponseEntity getComment(@PathVariable("commentId") Integer commentId) {
		try {
			ResponseDTO comment = commentService.getComment(commentId);
			return ResponseEntity.ok().body(comment);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 내가 작성한 후기 목록 조회
	@GetMapping("/comment/get-my/{page}")
	public ResponseEntity getMyComment(@PathVariable("page") Integer page) {
		try {
			ResponseDTO comments = commentService.getMyComment(page);
			return ResponseEntity.ok().body(comments);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// 후기 삭제
	@PostMapping("/comment/delete")
	public ResponseEntity deleteComment(@RequestPart("dto") CommentDTO dto) {
		try {
			commentService.deleteComment(dto);
			return ResponseEntity.ok().body("후기 작성 완료");
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
