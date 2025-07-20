package com.example.banto.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.banto.DAOs.CartDAO;
import com.example.banto.DTOs.CartDTO;
import com.example.banto.DTOs.ResponseDTO;

@Service
public class CartService {
	@Autowired
	CartDAO cartDAO;

	public void addCart(CartDTO dto) throws Exception{
		try {
			cartDAO.addCart(dto);
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO readCart() throws Exception{
		try {
			return cartDAO.readCart();
		}catch(Exception e) {
			throw e;
		}
	}
	
	public void modifyCart(CartDTO dto) throws Exception{
		try {
			cartDAO.modifyCart(dto);
		}catch(Exception e) {
			throw e;
		}
	}
	
	public void deleteCart(CartDTO dto) throws Exception{
		try {
			cartDAO.deleteCart(dto);
		}catch(Exception e) {
			throw e;
		}
	}
}
