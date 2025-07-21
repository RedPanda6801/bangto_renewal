package com.example.banto.Services;

import com.example.banto.Stores.StoreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.banto.DAOs.ApplyDAO;
import com.example.banto.SellerAuths.ProcessDTO;
import com.example.banto.DTOs.ResponseDTO;

@Service
public class ApplyService {
	@Autowired
	ApplyDAO applyDAO;
	
	public ResponseDTO getAuthInfo() throws Exception{
		try {
			return applyDAO.getAuthInfo();
		}catch(Exception e) {
			throw e;
		}
	}
	
	public void applySellerAuth(StoreDTO storeDTO) throws Exception {
		try {
			if(storeDTO.getName() == null || storeDTO.getBusiNum() == null){
				throw new Exception("필수 정보 오류");
			}
			applyDAO.applySellerAuth(storeDTO);
		}catch(Exception e) {
			throw e;
		}
	}
	
	public void modify(ProcessDTO dto) throws Exception {
		try {
			if(dto.getSellerAuthPk() == null || dto.getProcess() == null){
				throw new Exception("필수 정보 오류");
			}
			applyDAO.modify(dto);
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getApplyList(Integer page) throws Exception {
		try {
			return applyDAO.getApplyList(page);
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getApply(Integer userId) throws Exception {
		try {
			return applyDAO.getApply(userId);
		}catch(Exception e) {
			throw e;
		}
	}
}
