package com.example.banto.Users;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.banto.DTOs.ResponseDTO;

import java.util.ArrayList;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	
	// 회원가입 기능
	@PostMapping("/sign")
	public ResponseEntity<?> sign(@Valid @RequestBody UserDTO dto) {
		userService.sign(dto);
		return ResponseEntity.ok().body(null);
	}
	// 로그인 기능
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginDTO dto) {
		String token = userService.login(dto);
		return ResponseEntity.ok().body(
			new ResponseDTO(token, null)
		);
	}
	// 내정보 조회
	@GetMapping("/user")
	public ResponseEntity<?> getUser() {
		UserDTO user = userService.getUser();
		return ResponseEntity.ok().body(
			new ResponseDTO(user, null)
		);
	}
	// 유저 단일 조회(관리자)
	@GetMapping("/user/admin/{userId}")
	public ResponseEntity<?> getUserForAdmin(@PathVariable("userId") Long userId) {
		UserDTO user = userService.getUserForAdmin(userId);
		return ResponseEntity.ok().body(
			new ResponseDTO(user, null)
		);
	}
	// 유저 전체 정보 조회(관리자)
	@GetMapping("/user/admin/get-list/{page}")
	public ResponseEntity<?> getUserListForAdmin(@PathVariable("page") Integer page) {
			ArrayList<UserDTO> user = userService.getUserListForAdmin(page);
			return ResponseEntity.ok().body(
				new ResponseDTO(user, null));
	}
	// 내정보 수정
	@PutMapping("/user")
	public ResponseEntity<?> updateUser(@RequestBody UserDTO dto) {
		userService.update(dto);
		return ResponseEntity.ok().body(null);
	}
	// 회원탈퇴
	@Delete("/user")
	public ResponseEntity<?> deleteUser() {
		try {
			userService.delete();
			return ResponseEntity.ok().body("회원탈퇴 완료");
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
