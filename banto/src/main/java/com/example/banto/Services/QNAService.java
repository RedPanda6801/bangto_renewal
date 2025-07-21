package com.example.banto.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.banto.DAOs.QNADAO;
import com.example.banto.Qnas.QNADTO;
import com.example.banto.DTOs.ResponseDTO;

@Service
public class QNAService {
	@Autowired
	QNADAO qnaDAO;
	
	public void addQNA(QNADTO qnaDTO) throws Exception {
		try {
			if(qnaDTO.getQContent() == null || qnaDTO.getQContent() == "" || qnaDTO.getItemPk() == null) {
				throw new Exception("필수 정보 오류");
			}
			else {
				qnaDAO.addQNA(qnaDTO);
			}
		}catch(Exception e) {
		 throw e;
		}
	}
	
	public void addSellerAnswer(QNADTO qnaDTO) throws Exception {
		try {
			if(qnaDTO.getAContent() == null || qnaDTO.getId() == null || qnaDTO.getItemPk() == null) {
				throw new Exception("필수 정보 오류");
			}
			else {
				qnaDAO.addSellerAnswer(qnaDTO);
			}
		}catch(Exception e) {
		 throw e;
		}
	}
	
	public ResponseDTO getMyList(Integer page) throws Exception {
		try {
			if(page == null || page < 1) {
				throw new Exception("페이지 입력 오류");
			}else {
				return qnaDAO.getMyList(page);
			}
		}catch(Exception e) {
		 throw e;
		}
	}
	
	public ResponseDTO getListByStore(QNADTO qnaDTO, Integer page) throws Exception {
		try {
			if(qnaDTO.getStorePk() == null) {
				throw new Exception("매장 정보 오류");
			}
			else if(page == null || page < 1) {
				throw new Exception("페이지 입력 오류");
			}else {
				return qnaDAO.getListByStore(qnaDTO, page);
			}
		}catch(Exception e) {
		 throw e;
		}
	}
	
	public ResponseDTO getQnaDetail(QNADTO qnaDTO) throws Exception {
		try {
			if(qnaDTO.getId() == null){
				throw new Exception("필수 정보 오류");
			}else {
				return qnaDAO.getQnaDetail(qnaDTO);
			}
		}catch(Exception e) {
		 throw e;
		}
	}

	// 관리자 + 구매자
	public void deleteQNA(QNADTO qnaDTO) throws Exception {
		try {
			if(qnaDTO.getId() == null) {
				throw new Exception("필수 정보 오류");
			} else {
				qnaDAO.deleteQNA(qnaDTO);
			}
		}catch(Exception e) {
			throw e;
		}
	}


	public ResponseDTO getListByItem(Integer itemId, Integer page) throws Exception {
		try {
			if(itemId == null) {
				throw new Exception("필수 정보 오류");
			}
			else if(page == null || page < 1) {
				throw new Exception("페이지 입력 오류");
			}else {
				return qnaDAO.getListByItem(itemId, page);
			}
		}catch(Exception e) {
			throw e;
		}
	}
}
