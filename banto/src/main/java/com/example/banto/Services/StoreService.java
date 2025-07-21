package com.example.banto.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.banto.DAOs.StoreDAO;
import com.example.banto.DTOs.ResponseDTO;
import com.example.banto.Stores.StoreDTO;

@Service
public class StoreService {
	@Autowired
	StoreDAO storeDAO;
	
	public void create(StoreDTO dto) throws Exception {
		try {
			if(dto.getName() == null || dto.getBusiNum() == null){
				throw new Exception("필수 정보 오류");
			}
			storeDAO.create(dto);
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getMyStores() throws Exception {
		try {
			return storeDAO.getMyStores();
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getStoreDetail(Integer storeId) throws Exception {
		try {
			return storeDAO.getStore(storeId);
		}catch(Exception e) {
			throw e;
		}
	}
	public void modify(StoreDTO dto) throws Exception {
		try {
			storeDAO.modify(dto);
		}catch(Exception e) {
			throw e;
		}
	}

	public void delete(StoreDTO dto) throws Exception {
		try {
			storeDAO.delete(dto);
		}catch(Exception e) {
			throw e;
		}
	}

	public void modifyForRoot(StoreDTO dto) throws Exception {
		try {
			storeDAO.modifyForRoot(dto);
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getMyStoresByRoot(Integer page) throws Exception {
		try {
			return storeDAO.getMyStoresByRoot(page);
		}catch(Exception e) {
			throw e;
		}
	}
	
	public void deleteByRoot(StoreDTO dto) throws Exception {
		try {
			storeDAO.deleteByRoot(dto);
		}catch(Exception e) {
			throw e;
		}
	}
}
