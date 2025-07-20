package com.example.banto.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.banto.DAOs.UserDAO;
import com.example.banto.DTOs.LoginDTO;
import com.example.banto.DTOs.ResponseDTO;
import com.example.banto.DTOs.UserDTO;

@Service
public class UserService {
	@Autowired
	UserDAO userDAO;
	
	public void sign(UserDTO dto) throws Exception {
		// validation
		if(dto.getEmail() == null || dto.getName() == null || (!dto.getSnsAuth() && dto.getPw() == null)) {
			throw new Exception("입력 오류");
		}
		try {
			userDAO.sign(dto);
		}catch(Exception e) {
			throw e;
		}
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
