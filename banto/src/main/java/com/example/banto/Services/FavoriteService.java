package com.example.banto.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.banto.DAOs.FavoriteDAO;
import com.example.banto.DTOs.FavoriteDTO;
import com.example.banto.DTOs.ItemDTO;
import com.example.banto.DTOs.ResponseDTO;

@Service
public class FavoriteService {
	@Autowired
	FavoriteDAO favoriteDAO;
	
	public void addFavorite(ItemDTO itemDTO) throws Exception {
		try {
			if(itemDTO.getId() == null) {
				throw new Exception("필수 정보 오류");
			}
			favoriteDAO.addFavorite(itemDTO);
		}catch(Exception e) {
			throw e;
		}
	}
	
	public void deleteFavorite(ItemDTO itemDTO) throws Exception {
		try {
			if(itemDTO.getId() == null) {
				throw new Exception("필수 정보 오류");
			}
			favoriteDAO.deleteFavorite(itemDTO);
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getAllFavorites(Integer page) throws Exception {
		try {
			return favoriteDAO.getAllFavorites(page);
		}catch(Exception e) {
			throw e;
		}
	}
}
