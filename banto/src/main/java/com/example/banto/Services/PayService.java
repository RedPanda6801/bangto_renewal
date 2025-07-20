package com.example.banto.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.banto.DAOs.PayDAO;
import com.example.banto.DTOs.PayDTO;
import com.example.banto.DTOs.ResponseDTO;
import com.example.banto.DTOs.SoldItemDTO;

@Service
public class PayService {
	@Autowired
	PayDAO payDAO;

	public void payCart(PayDTO dto) throws Exception{
		try {
			payDAO.payCart(dto);
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getPayList(Integer year, Integer page) throws Exception{
		try {
			return payDAO.getPayList(year, page);
		}catch(Exception e) {
			throw e;
		}
	}

	public ResponseDTO getPayListForRoot(Integer userId, Integer year, Integer page) throws Exception{
		try {
			if(userId == null) {
				throw new Exception("권한 없음");
			}
			else {
				return payDAO.getPayListForRoot(userId, year, page);
			}
		}catch(Exception e) {
			throw e;
		}
	}
	
	public void modifySoldItem(SoldItemDTO dto) throws Exception{
		try {
			payDAO.modifySoldItem(dto);
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getSoldList(Integer storeId, Integer page) throws Exception{
		try {
			return payDAO.getSoldList(storeId, page);
		}catch(Exception e) {
			throw e;
		}
	}

	public ResponseDTO getSoldListForRoot(Integer storeId, Integer page) throws Exception{
		try {
			return payDAO.getSoldListForRoot(storeId, page);
		}catch(Exception e) {
			throw e;
		}
	}
}
