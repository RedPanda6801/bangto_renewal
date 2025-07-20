package com.example.banto.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.banto.DAOs.GroupBuyDAO;
import com.example.banto.DTOs.GroupBuyDTO;
import com.example.banto.DTOs.ResponseDTO;

@Service
public class GroupBuyService {

	@Autowired
	GroupBuyDAO groupBuyDAO;
	
	public void addEvent(GroupBuyDTO dto) throws Exception {
		try {
			if(dto.getStartDate() == null || dto.getEndDate() == null) {
				throw new Exception("필수 정보 오류");
			}
			groupBuyDAO.addEvent(dto);
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getEventList() throws Exception {
		try {
			return groupBuyDAO.getEventList();
		}catch(Exception e) {
			throw e;
		}
	}

	public ResponseDTO getChooseList() throws Exception {
		try {
			return groupBuyDAO.getChooseList();
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getCurrentEvent() throws Exception {
		try {
			return groupBuyDAO.getCurrentEvent();
		}catch(Exception e) {
			throw e;
		}
	}

	public ResponseDTO getEventListToSeller() throws Exception {
		try {
			return groupBuyDAO.getEventListToSeller();
		}catch(Exception e) {
			throw e;
		}
	}

	public void deleteEvent(GroupBuyDTO dto) throws Exception {
		try {
			if(dto.getId() == null) {
				throw new Exception("필수 정보 오류");
			}
			groupBuyDAO.deleteEvent(dto);
		}catch(Exception e) {
			throw e;
		}
	}
}
