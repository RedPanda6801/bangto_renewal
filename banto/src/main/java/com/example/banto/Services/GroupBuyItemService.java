package com.example.banto.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.banto.DAOs.GroupBuyItemDAO;
import com.example.banto.DTOs.GroupBuyDTO;
import com.example.banto.DTOs.GroupBuyItemDTO;
import com.example.banto.DTOs.ResponseDTO;

@Service
public class GroupBuyItemService {
	@Autowired
	GroupBuyItemDAO groupBuyItemDAO;
	
	
	public ResponseDTO getCurrentItemList(int eventId) throws Exception {
		try {
			return groupBuyItemDAO.getCurrentItemList(eventId);
		}catch(Exception e) {
			throw e;
		}
	}

	public void addItem(GroupBuyItemDTO dto) throws Exception {
		try {
			if(dto.getItemPk() == null || dto.getLimitPerBuyer() == null
			|| dto.getMaxAmount() == null || dto.getEventPk() == null
			|| dto.getOptionPk() == null){
				throw new Exception("필수 정보 오류");
			}
			groupBuyItemDAO.addItem(dto);
		}catch(Exception e) {
			throw e;
		}
	}
	
	public void modifyGroupItem(GroupBuyItemDTO dto) throws Exception {
		try {
			if(dto.getItemPk() == null){
				throw new Exception("필수 정보 오류");
			}
			groupBuyItemDAO.modifyGroupItem(dto);
		}catch(Exception e) {
			throw e;
		}
	}

	public void deleteGroupItem(GroupBuyItemDTO dto) throws Exception {
		try {
			if(dto.getId() == null){
				throw new Exception("필수 정보 오류");
			}
			groupBuyItemDAO.deleteGroupItem(dto);
		}catch(Exception e) {
			throw e;
		}
	}
}
