package com.example.banto.Users;

import com.example.banto.Exceptions.DuplicateUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.banto.DTOs.ResponseDTO;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserService {

	private final UserRepository userRepository;

	UserService(UserRepository userRepository){
		this.userRepository = userRepository;
	}
	
	public void sign(UserDTO dto){
		// 회원가입 흐름
		// 1. User 존재 여부 확인 -> 존재 시 예외 처리
		userRepository.findByEmail(dto.getEmail()).ifPresent(e -> {
			throw new DuplicateUserException("이미 존재하는 이메일입니다.");
		});
		// 2. SNS 여부 확인
		if(!dto.getSnsAuth()) {
			// 2-1. SNS 계정이 아니면 pw 값 암호화 후 dto에 다시 set
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			dto.setPw(passwordEncoder.encode(dto.getPw()));
		}
		else {
			// 2-2. SNS 계정 시 snsAuth(true)
			dto.setSnsAuth(true);
		}
		// 3. DTO -> Entity
		Users user = Users.toEntity(dto);
		// 3. Repository 저장
		userRepository.save(user);
	}
	
	public ResponseDTO login(UserDTO dto) throws Exception {
		if(dto.getEmail() == null || dto.getPw() == null) {
			throw new Exception("입력 오류");
		}
		try {
			return userDAO.login(dto.getEmail(), dto.getPw());
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getUserListForRoot(Integer page) throws Exception {
		try {
				return userDAO.getUserListForRoot(page);
		}catch(Exception e) {
			throw e;
		}
	}
	public ResponseDTO getUser() throws Exception {
		try {
				return userDAO.getUser();
		}catch(Exception e) {
			throw e;
		}
	}
	public void modifyUser(UserDTO dto) throws Exception {
		try {
				userDAO.modifyUser(dto);
		}catch(Exception e) {
			throw e;
		}
	}
	public ResponseDTO getUserForRoot(Integer userId) throws Exception {
		try {
				return userDAO.getUserForRoot(userId);
		}catch(Exception e) {
			throw e;
		}
	}
	public void modifyUserForRoot(Integer userId, UserDTO dto) throws Exception {
		try {
				userDAO.modifyUserForRoot(userId, dto);
		}catch(Exception e) {
			throw e;
		}
	}
	public void deleteMyself() throws Exception {
		try {
			userDAO.deleteMyself();
		}catch(Exception e) {
			throw e;
		}
	}
	public void deleteUser(Integer userId) throws Exception {
		try {
			userDAO.deleteUser(userId);
		}catch(Exception e) {
			throw e;
		}
	}
	public ResponseDTO isSnsSigned(String email) throws Exception {
		try {
			return userDAO.isSnsSigned(email);
		}catch(Exception e) {
			throw e;
		}
	}
}
