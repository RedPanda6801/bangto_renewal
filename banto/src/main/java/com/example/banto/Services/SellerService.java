package com.example.banto.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.banto.DAOs.SellerDAO;
import com.example.banto.DTOs.ResponseDTO;
import com.example.banto.DTOs.SellerDTO;

@Service
public class SellerService {
	@Autowired
	SellerDAO sellerDAO;
	
	public ResponseDTO getSellerInfo() throws Exception{
		try {
			return sellerDAO.findSeller();
		}catch(Exception e) {
			throw e;
		}
	}

	public ResponseDTO getSellerInfoForRoot(int userId) throws Exception{
		try {
			return sellerDAO.findSellerForRoot(userId);
		}catch(Exception e) {
			throw e;
		}
	}
	
	public void deleteMyself() throws Exception{
		try {
			sellerDAO.deleteMyself();
		}catch(Exception e) {
			throw e;
		}
	}
	
	public void deleteSeller(Integer userId) throws Exception{
		try {
			if(userId == null){
				throw new Exception("필수 정보 오류");
			}
			sellerDAO.deleteSeller(userId);
		}catch(Exception e) {
			throw e;
		}
	}

	public ResponseDTO getSellerList(int page) throws Exception{
		try {
			return sellerDAO.getSellerList(page);
		}catch(Exception e) {
			throw e;
		}
	}
}
