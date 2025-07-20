package com.example.banto.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.banto.DAOs.ItemDAO;
import com.example.banto.DTOs.ItemDTO;
import com.example.banto.DTOs.OptionDTO;
import com.example.banto.DTOs.ResponseDTO;

@Service
public class ItemService {
	@Autowired
	ItemDAO itemDAO;
	
	public ResponseDTO getAllItemList(Integer page) throws Exception {
		try {
			if(page == null || page < 1) {
				throw new Exception("페이지 입력 오류");
		}else {
				return itemDAO.getAllItemList(page);
			}
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getRecommendItemList() throws Exception {
		try {
			return itemDAO.getRecommendItemList();
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getFilterdItemList(ItemDTO dto) throws Exception {
		try {
			return itemDAO.getFilterdItemList(dto);
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getItemList(Integer storeId, Integer page) throws Exception {
		try {
			if(page == null || page < 1) {
				throw new Exception("페이지 입력 오류");
		}else {
				return itemDAO.getItemList(storeId, page);
			}
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getItemListByTitle(String title, Integer page) throws Exception {
		try {
			if(page == null || page < 1) {
				throw new Exception("페이지 입력 오류");
		}else {
				return itemDAO.getItemListByTitle(title, page);
			}
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getItemListByStoreName(String storeName, Integer page) throws Exception {
		try {
			if(page == null || page < 1) {
				throw new Exception("페이지 입력 오류");
		}else {
				return itemDAO.getItemListByStoreName(storeName, page);
			}
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getItemListByCategory(String category, Integer page) throws Exception {
		try {
			if(page == null || page < 1) {
				throw new Exception("페이지 입력 오류");
		}else {
				return itemDAO.getItemListByCategory(category, page);
			}
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getItemDetail(Integer itemId) throws Exception {
		try {
			return itemDAO.getItemDetail(itemId);
		}catch(Exception e) {
			throw e;
		}
	}
	
	public void addItem(ItemDTO itemDTO, List<MultipartFile> files) throws Exception {
		try {
			if(itemDTO.getStorePk() == null || itemDTO.getOptions() == null || itemDTO.getContent() == null ||
				itemDTO.getCategory() == null || itemDTO.getTitle() == null){
				throw new Exception("필수 정보 오류");
			}
			itemDAO.addItem(itemDTO, files);
		}catch(Exception e) {
			throw e;
		}
	}
	
	public void modifyItem(ItemDTO itemDTO, List<MultipartFile> files) throws Exception {
		try {
			if(itemDTO.getId() == null){
				throw new Exception("필수 정보 오류");
			}
			itemDAO.modifyItem(itemDTO, files);
		}catch(Exception e) {
			throw e;
		}
	}

	// dto.id
	public void deleteItem(ItemDTO itemDTO) throws Exception {
		try {
			if(itemDTO.getId() == null || itemDTO.getStorePk() == null){
				throw new Exception("필수 정보 오류");
			}
			itemDAO.deleteItem(itemDTO);
		}catch(Exception e) {
			throw e;
		}
	}

	public void addItemOption(OptionDTO optionDTO) throws Exception {
		try {
			System.out.println("hehe");
			System.out.println(optionDTO.getOptionInfo());
			if(optionDTO.getOptionInfo() == null || optionDTO.getItemPk() == null
					|| optionDTO.getAmount() == null || optionDTO.getAddPrice() == null){
				throw new Exception("필수 정보 오류");
			}
			itemDAO.addItemOption(optionDTO);
		}catch(Exception e) {
			throw e;
		}
	}
	
	public void modifyItemOption(OptionDTO optionDTO) throws Exception {
		try {
			if(optionDTO.getId() == null){
				throw new Exception("필수 정보 오류");
			}
			itemDAO.modifyItemOption(optionDTO);
		}catch(Exception e) {
			throw e;
		}
	}

	public void deleteOption(OptionDTO optionDTO) throws Exception {
		try {
			if(optionDTO.getId() == null){
				throw new Exception("필수 정보 오류");
			}
			itemDAO.deleteOption(optionDTO);
		}catch(Exception e) {
			throw e;
		}
	}
}
