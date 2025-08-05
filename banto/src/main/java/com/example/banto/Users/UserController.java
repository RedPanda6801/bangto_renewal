package com.example.banto.Users;


import com.example.banto.JWTs.RefreshToken;
import com.example.banto.Utils.PageDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	
	// 회원가입 기능
	@PostMapping("/sign")
	public ResponseEntity<?> sign(@Valid @RequestBody UserDTO dto) {
		userService.sign(dto);
		return ResponseEntity.ok().body("회원가입 성공");
	}
	// 로그인 기능
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginDTO dto,
	   @CookieValue(value="cart_id", required=false) String guestCartId,
	   HttpServletResponse response) {
		String token = userService.login(dto, guestCartId, response);
		return ResponseEntity.ok().body(token);
	}
	// 내정보 조회
	@GetMapping("/user")
	public ResponseEntity<?> getUser() {
		UserDTO user = userService.getUser();
		return ResponseEntity.ok().body(user);
	}
	// 유저 단일 조회(관리자)
	@GetMapping("/admin/user/{userId}")
	public ResponseEntity<?> getUserForAdmin(@PathVariable("userId") Long userId) {
		UserDTO user = userService.getUserForAdmin(userId);
		return ResponseEntity.ok().body(user);
	}
	// 유저 전체 정보 조회(관리자)
	@GetMapping("/admin/user/get-list/{page}")
	public ResponseEntity<?> getUserListForAdmin(@PathVariable("page") Integer page) {
		PageDTO useList = userService.getUserListForAdmin(page);
		return ResponseEntity.ok().body(useList);
	}
	// 내정보 수정
	@PutMapping("/user")
	public ResponseEntity<?> updateUser(@RequestBody UserDTO dto) {
		userService.update(dto);
		return ResponseEntity.ok().body("회원수정 완료");
	}
	// 회원탈퇴
	@DeleteMapping("/user")
	public ResponseEntity<?> deleteUser() {
		userService.delete();
		return ResponseEntity.ok().body("회원탈퇴 완료");
	}
	// SNS 회원가입 여부 확인
	// 상황에 따라 삭제 확인
	@GetMapping("/user/sns-sign/{email}")
	public ResponseEntity<?> isSnsSigned(@PathVariable("email") String email) {
		boolean isSnsChecked = userService.isSnsSigned(email);
		return ResponseEntity.ok().body(isSnsChecked);
	}
}
