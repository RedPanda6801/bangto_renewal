package com.example.banto.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.banto.DAOs.GroupItemPayDAO;
import com.example.banto.DTOs.GroupBuyDTO;
import com.example.banto.DTOs.GroupBuyItemDTO;
import com.example.banto.DTOs.GroupItemPayDTO;
import com.example.banto.DTOs.ResponseDTO;

@Service
public class GroupItemPayService {
	@Autowired
	GroupItemPayDAO groupItemPayDAO;
	
	public ResponseDTO getPayListByStore(GroupItemPayDTO dto) throws Exception {
		try {
			if(dto.getStorePk() == null){
				throw new Exception("필수 정보 오류");
			}
			return groupItemPayDAO.getPayListByStore(dto);
		}catch(Exception e) {
			throw e;
		}
	}

	public void payGroupItem(GroupItemPayDTO dto) throws Exception {
		try {
			// Validation
			if(dto.getAmount() == null || dto.getItemPk() == null || dto.getAddress() == null
					|| dto.getOptionPk() == null || dto.getGroupItemPk() == null){
				throw new Exception("필수 정보 오류");
			}
			groupItemPayDAO.payGroupItem(dto);
		}catch(Exception e) {
			throw e;
		}
	}

	public ResponseDTO getMyGroupPayList(Integer year) throws Exception {
		try {
			return groupItemPayDAO.getMyGroupPayList(year);
		}catch(Exception e) {
			throw e;
		}
	}

	public void deliveringCheck(GroupItemPayDTO dto) throws Exception {
		try {
			// Validation
			if(dto.getId() == null){
				throw new Exception("필수 정보 오류");
			}
			groupItemPayDAO.deliveringCheck(dto);
		}catch(Exception e) {
			throw e;
		}
	}

	public void deliveringCheckTotal(GroupItemPayDTO dto) throws Exception {
		try {
			// Validation
			if(dto.getPayIdList() == null){
				throw new Exception("필수 정보 오류");
			}
			for(Integer id : dto.getPayIdList()) {
				if(id == null){
					throw new Exception("필수 정보 오류");
				}
			}
			groupItemPayDAO.deliveringCheckTotal(dto);
		}catch(Exception e) {
			throw e;
		}
	}

	public void deliveredCheck(GroupItemPayDTO dto) throws Exception {
		try {
			// Validation
			if(dto.getId() == null){
				throw new Exception("필수 정보 오류");
			}
			groupItemPayDAO.deliveredCheck(dto);
		}catch(Exception e) {
			throw e;
		}
	}
}
