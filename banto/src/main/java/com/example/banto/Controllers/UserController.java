package com.example.banto.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.banto.DAOs.AuthDAO;
import com.example.banto.DTOs.LoginDTO;
import com.example.banto.DTOs.ResponseDTO;
import com.example.banto.DTOs.UserDTO;
import com.example.banto.JWTs.JwtUtil;
import com.example.banto.Services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api")
public class UserController {
	@Autowired
	UserService userService;
	
	// 회원가입 기능
	@PostMapping("/sign")
	public ResponseEntity sign(@RequestBody UserDTO dto) {
		try {
			userService.sign(dto);
			return ResponseEntity.ok().body(null);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 로그인 기능
	@PostMapping("/login")
	public ResponseEntity login(@RequestBody UserDTO dto) {
		try {
			ResponseDTO token = userService.login(dto);
			return ResponseEntity.ok().body(token);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 내정보 조회
	@GetMapping("/user/get-info")
	public ResponseEntity getUser() {
		try {
			ResponseDTO user = userService.getUser();
			return ResponseEntity.ok().body(user);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	// 내정보 수정
	@PostMapping("/user/modify")
	public ResponseEntity modifyUser(@RequestBody UserDTO dto) {
		try {
			userService.modifyUser(dto);
			return ResponseEntity.ok().body(null);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 회원탈퇴
	@PostMapping("/user/delete-me")
	public ResponseEntity deleteMyself() {
		try {
			userService.deleteMyself();
			return ResponseEntity.ok().body("회원탈퇴 완료");
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 유저 전체 정보 조회(관리자)
	@GetMapping("/manager/user/get-list/{page}")
	public ResponseEntity getUserListManager(@PathVariable("page") Integer page) {
		try {
			ResponseDTO user = userService.getUserListForRoot(page);
			return ResponseEntity.ok().body(user);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	// 유저 단일 조회(관리자)
	@GetMapping("/manager/user/get-info/{userId}")
	public ResponseEntity getUserManager(@PathVariable("userId") Integer userId) {
		try {
			ResponseDTO user = userService.getUserForRoot(userId);
			return ResponseEntity.ok().body(user);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	// 유저 단일 수정(관리자)
	@PostMapping("/manager/user/modify/{userId}")
	public ResponseEntity modifyUserManager(@PathVariable("userId") Integer userId, @RequestBody UserDTO dto) {
		try {
			userService.modifyUserForRoot(userId, dto);
			return ResponseEntity.ok().body(null);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	// 유저 단일 삭제(관리자)
	@PostMapping("/manager/user/delete/{userId}")
	public ResponseEntity deleteUser(@PathVariable("userId") Integer userId) {
		try {
			userService.deleteUser(userId);
			return ResponseEntity.ok().body("회원 추방 완료");
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	// SNS 회원가입 여부 확인
	@GetMapping("/user/get-sns-signed/{email}")
	public ResponseEntity isSnsSigned(@PathVariable("email") String email) {
		try {
			ResponseDTO isSnsSigned = userService.isSnsSigned(email);
			return ResponseEntity.ok().body(isSnsSigned);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
