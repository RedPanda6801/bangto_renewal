package com.example.banto.DAOs;

import com.example.banto.Enums.CategoryType;
import com.example.banto.Items.Items;
import com.example.banto.Options.Options;
import com.example.banto.Repositorys.*;
import com.example.banto.Stores.Stores;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.File;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.banto.Configs.EnvConfig;
import com.example.banto.Items.ItemDTO;
import com.example.banto.Options.OptionDTO;
import com.example.banto.DTOs.PageDTO;
import com.example.banto.DTOs.ResponseDTO;

import jakarta.transaction.Transactional;


@Component
public class ItemDAO {
	@Autowired
	ItemRepository itemRepository;
	@Autowired
	AuthDAO authDAO;
	@Autowired
	StoreRepository storeRepository;
	@Autowired
	OptionRepository optionRepository;
	@Autowired
	EnvConfig envConfig;
	@Autowired
	GroupBuyPayRepository groupBuyPayRepository;
	@Autowired
	GroupBuyItemRepository groupBuyItemRepository;

	public ResponseDTO getAllItemList(Integer page) throws Exception{
		try {
			Pageable pageable = PageRequest.of(page-1, 20, Sort.by("id").ascending());
			Page<Items>items = itemRepository.findAll(pageable);
			List<ItemDTO>itemList = new ArrayList<ItemDTO>();
			for(Items item : items) {
				ItemDTO dto = ItemDTO.toDTO(item);
				itemList.add(dto);
			}
			return new ResponseDTO(itemList, new PageDTO(items.getSize(), items.getTotalElements(), items.getTotalPages()));
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getRecommendItemList() throws Exception{
		try {
			int size = 20;
			Pageable pageable = PageRequest.of(0, size);
			Page<Items>items = itemRepository.getItemsOrderByFavoritesSizeDesc(pageable);
			List<ItemDTO>itemList = new ArrayList<ItemDTO>();
			for(Items item : items) {
				ItemDTO dto = ItemDTO.toDTO(item);
				itemList.add(dto);
			}
			return new ResponseDTO(itemList, new PageDTO(size, size, 1));
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getFilterdItemList(ItemDTO dto) throws Exception{
		try {
			if(dto.getPage() == null || dto.getPage() < 1) {
				throw new Exception("page 입력 오류");
			}
			if(dto.getSize() == null || dto.getSize() < 1) {
				throw new Exception("size 입력 오류");
			}
			List<Sort.Order> sortOrder = new ArrayList<>();
			if(dto.getPriceSort() != null) {				
				if(dto.getPriceSort().equalsIgnoreCase("asc")) {
					sortOrder.add(Sort.Order.asc("price"));
				}
				else if(dto.getPriceSort().equalsIgnoreCase("desc")) {
					sortOrder.add(Sort.Order.desc("price"));
				}
				else {
					throw new Exception("priceSort 입력 오류");
				}
			}
			sortOrder.add(Sort.Order.asc("id"));
			CategoryType category = null;
			if(dto.getCategory() != null) {				
				category = CategoryType.valueOf(dto.getCategory());
			}
			Pageable pageable = PageRequest.of(dto.getPage() - 1, dto.getSize(), Sort.by(sortOrder));
			Page<Items> items = itemRepository.getFilterdItemList(dto.getTitle(), dto.getStoreName(), category, pageable);
			List<ItemDTO> itemList = new ArrayList<ItemDTO>();
			for(Items item : items) {
				ItemDTO itemDTO = ItemDTO.toDTO(item);
				itemList.add(itemDTO);
			}
			return new ResponseDTO(itemList, new PageDTO(items.getSize(), items.getTotalElements(), items.getTotalPages()));
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getItemList(Integer storeId, Integer page) throws Exception{
		try {
			// storeId로 가져오기
			Pageable pageable = PageRequest.of(page-1, 20, Sort.by("id").ascending());
			Page<Items>items = itemRepository.getItemsByStoreId(storeId, pageable);
			List<ItemDTO>itemList = new ArrayList<ItemDTO>();
			for(Items item : items) {
				ItemDTO dto = ItemDTO.toDTO(item);
				itemList.add(dto);
			}
			return new ResponseDTO(itemList, new PageDTO(items.getSize(), items.getTotalElements(), items.getTotalPages()));
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getItemListByTitle(String title, Integer page) throws Exception{
		try {
			// storeId로 가져오기
			Pageable pageable = PageRequest.of(page-1, 20, Sort.by("id").ascending());
			Page<Items> items = itemRepository.getItemsByTitle(title, pageable);
			if(items.isEmpty() || items == null) {
				throw new Exception("검색 결과가 없습니다.");
			}
			List<ItemDTO> itemList = new ArrayList<ItemDTO>();
			for(Items item : items) {
				ItemDTO dto = ItemDTO.toDTO(item);
				itemList.add(dto);
			}
			return new ResponseDTO(itemList, new PageDTO(items.getSize(), items.getTotalElements(), items.getTotalPages()));
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getItemListByStoreName(String storeName, Integer page) throws Exception{
		try {
			// storeId로 가져오기
			Pageable pageable = PageRequest.of(page-1, 20, Sort.by("id").ascending());
			Page<Items> items = itemRepository.getItemsByStoreName(storeName, pageable);
			if(items.isEmpty() || items == null) {
				throw new Exception("검색 결과가 없습니다.");
			}
			List<ItemDTO> itemList = new ArrayList<ItemDTO>();
			for(Items item : items) {
				ItemDTO dto = ItemDTO.toDTO(item);
				itemList.add(dto);
			}
			return new ResponseDTO(itemList, new PageDTO(items.getSize(), items.getTotalElements(), items.getTotalPages()));
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getItemListByCategory(String category, Integer page) throws Exception{
		try {
			// storeId로 가져오기
			Pageable pageable = PageRequest.of(page-1, 20, Sort.by("id").ascending());
			Page<Items> items = itemRepository.getItemsByCategory(CategoryType.valueOf(category), pageable);
			if(items.isEmpty() || items == null) {
				throw new Exception("검색 결과가 없습니다.");
			}
			List<ItemDTO> itemList = new ArrayList<ItemDTO>();
			for(Items item : items) {
				ItemDTO dto = ItemDTO.toDTO(item);
				itemList.add(dto);
			}
			return new ResponseDTO(itemList, new PageDTO(items.getSize(), items.getTotalElements(), items.getTotalPages()));
		}catch(Exception e) {
			throw e;
		}
	}
	
	public ResponseDTO getItemDetail(Integer itemId) throws Exception{
		try {
			Optional<Items> item = itemRepository.findById(itemId);
			if(item.isEmpty()) {
				throw new Exception("물건 조회 오류");
			}else {
				ItemDTO dto = ItemDTO.toDTO(item.get());
				return new ResponseDTO(dto, null);
			}
		}catch(Exception e) {
			throw e;
		}
	}
	
	@Transactional
	public void addItem(ItemDTO itemDTO, List<MultipartFile> files) throws Exception{
		try {
			// 인증 유효 확인
			int sellerId = authDAO.authSeller(SecurityContextHolder.getContext().getAuthentication());
			if(authDAO.authSeller(SecurityContextHolder.getContext().getAuthentication()) == -1){
				throw new Exception("권한 오류");
			}
			Optional<Stores> store = storeRepository.findById(itemDTO.getStorePk());
			if(store.isEmpty()) {
				throw new Exception("매장 조회 오류");
			}else if(!store.get().getSeller().getUser().getId().equals(sellerId)){
				throw new Exception("본인 매장이 아님");
			}else {
				String savePath = envConfig.get("FRONTEND_UPLOAD_ADDRESS");
				String img = "";
				if(files != null) {				
					for(MultipartFile file : files) {
						String originalfilename = file.getOriginalFilename();
						String before = originalfilename.substring(0, originalfilename.indexOf("."));
						String ext = originalfilename.substring(originalfilename.indexOf("."));
						String newfilename = before + "(" + UUID.randomUUID() + ")" + ext;
						file.transferTo(new java.io.File(savePath + newfilename));
						img += newfilename + "/";
					}
					img = img.substring(0, img.length() - 1);			
				}
				
				Items item = Items.toEntity(itemDTO);
				item.setImg(img);
				item.setStore(store.get());
				Items newItem = itemRepository.save(item);
				
				for(Options option : itemDTO.getOptions()) {
					option.setAmount(option.getAmount());
					option.setOptionInfo(option.getOptionInfo());
					option.setAddPrice(option.getAddPrice());
					option.setItem(newItem); // 연관 관계 설정
	                optionRepository.save(option); // 개별적으로 저장
				}
			}
		}catch(Exception e) {
			throw e;
		}
	}
	
	@Transactional
	public void modifyItem(ItemDTO dto, List<MultipartFile> files) throws Exception{
		try {
			Integer sellerId = authDAO.authSeller(SecurityContextHolder.getContext().getAuthentication());
			if(sellerId == -1 && !authDAO.authRoot(SecurityContextHolder.getContext().getAuthentication())){
				throw new Exception("판매자 권한 오류");
			}
			Optional<Items> itemOpt = itemRepository.findById(dto.getId());
			if(itemOpt.isEmpty()) {
				throw new Exception("물건 조회 오류");
			}else if(!itemOpt.get().getStore().getSeller().getUser().getId().equals(sellerId)){
				throw new Exception("판매자 정보 불일치");
			}
			else {
				String savePath = envConfig.get("FRONTEND_UPLOAD_ADDRESS");
				StringBuilder img = new StringBuilder();
				Items item = itemOpt.get();

				if(files != null) {
					File saveDir = new File(savePath);
					if (!saveDir.exists()) {
						throw new Exception("파일 저장 디렉토리 오류");
					}
					File[] existingFiles = saveDir.listFiles();  // 기존 파일 목록 가져오기
					for(MultipartFile file : files) {
						String originalfilename = file.getOriginalFilename();
						if(originalfilename == null) break;
						// 파일이 이미 존재하는지 확인
						boolean exists = false;
						if (existingFiles != null) {
							for (File existingFile : existingFiles) {
								if (existingFile.getName().equals(originalfilename)) {
									img.append(originalfilename).append("/");
									exists = true;
									break;
								}
							}
						}
						if(exists) continue;
						String before = originalfilename.substring(0, originalfilename.indexOf("."));
						String ext = originalfilename.substring(originalfilename.indexOf("."));
						String newfilename = before + "(" + UUID.randomUUID() + ")" + ext;
						file.transferTo(new File(savePath + newfilename));
						img.append(newfilename).append("/");
					}
					img = new StringBuilder(img.substring(0, img.length() - 1));
				}
				// 수정 로직
				item.setTitle((dto.getTitle() != null && !dto.getTitle().equals("")) ?
						dto.getTitle() : item.getTitle());
				item.setCategory((dto.getCategory() != null && !dto.getCategory().equals("")) ?
						CategoryType.valueOf(dto.getCategory()) : item.getCategory());
				System.out.println("hello");
				item.setImg((!img.isEmpty()) ? img.toString() : item.getImg());
				item.setContent((dto.getContent() != null && !dto.getContent().equals("")) ?
						dto.getContent() : item.getContent());
				itemRepository.save(item);
			}
		}catch(Exception e) {
			throw e;
		}
	}

	@Transactional
	public void deleteItem(ItemDTO dto) throws Exception{
		try {
			int sellerId = authDAO.authSeller(SecurityContextHolder.getContext().getAuthentication());
			if(sellerId == -1 && !authDAO.authRoot(SecurityContextHolder.getContext().getAuthentication())){
				throw new Exception("판매자 권한 오류");
			}
			Optional<Stores> store = storeRepository.findById(dto.getStorePk());
			if(store.isEmpty()) {
				throw new Exception("매장 조회 오류");
			}else if(!store.get().getSeller().getUser().getId().equals(sellerId)){
				throw new Exception("판매자 정보 불일치");
			}else {
				Optional<Items> itemOpt = itemRepository.findById(dto.getId());
				if(itemOpt.isEmpty()){
					throw new Exception("아이템 조회 오류");
				}
				List<GroupItemPays> payments = groupBuyPayRepository.findByItemId(dto.getId());
				for(GroupItemPays payment : payments){
					payment.setItem(null);
					groupBuyPayRepository.save(payment);
				}
				itemRepository.delete(itemOpt.get());
			}
		}catch(Exception e) {
			throw e;
		}
	}

	@Transactional
	public void addItemOption(OptionDTO dto) throws Exception{
		try {
			int sellerId = authDAO.authSeller(SecurityContextHolder.getContext().getAuthentication());
			if(sellerId == -1 && !authDAO.authRoot(SecurityContextHolder.getContext().getAuthentication())){
				throw new Exception("판매자 권한 오류");
			}
			Optional<Items> itemOpt = itemRepository.findById(dto.getItemPk());
			if(itemOpt.isEmpty()) {
				throw new Exception("물건 조회 오류");
			}else if(!itemOpt.get().getStore().getSeller().getUser().getId().equals(sellerId)){
				throw new Exception("판매자 정보 불일치");
			}else {
				Items item = itemOpt.get();
				Options option = new Options();
				option.setOptionInfo(dto.getOptionInfo());
				option.setAddPrice(dto.getAddPrice());
				option.setAmount(dto.getAmount());
				option.setItem(item);
				optionRepository.save(option);
			}
		}catch(Exception e) {
			throw e;
		}
	}
	
	@Transactional
	public void modifyItemOption(OptionDTO dto) throws Exception{
		try {
			int sellerId = authDAO.authSeller(SecurityContextHolder.getContext().getAuthentication());
			if(sellerId == -1 && !authDAO.authRoot(SecurityContextHolder.getContext().getAuthentication())){
				throw new Exception("판매자 권한 오류");
			}
			Optional<Options> optionOpt = optionRepository.findById(dto.getId());
			if(optionOpt.isEmpty()) {
				throw new Exception("매장 조회 오류");
			}else if(!optionOpt.get().getItem().getStore().getSeller().getUser().getId().equals(sellerId)){
				throw new Exception("판매자 정보 불일치");
			}else {
				Options option = optionOpt.get();
				// 수정 로직
				option.setAddPrice((dto.getAddPrice() != null && !dto.getAddPrice().equals("")) ?
						dto.getAddPrice() : option.getAddPrice());
				option.setOptionInfo((dto.getOptionInfo() != null && !dto.getOptionInfo().equals("")) ?
						dto.getOptionInfo() : option.getOptionInfo());
				option.setAmount((dto.getAmount() != null && !dto.getAmount().equals("")) ?
						dto.getAmount() : option.getAmount());
				optionRepository.save(option);
			}
		}catch(Exception e) {
			throw e;
		}
	}

	@Transactional
	public void deleteOption(OptionDTO dto) throws Exception {
		try {
			int sellerId = authDAO.authSeller(SecurityContextHolder.getContext().getAuthentication());
			if (sellerId == -1 && !authDAO.authRoot(SecurityContextHolder.getContext().getAuthentication())) {
				throw new Exception("판매자 권한 오류");
			}

			Optional<Options> optionOpt = optionRepository.findById(dto.getId());
			if (optionOpt.isEmpty()) {
				throw new Exception("매장 조회 오류");
			} else if (!optionOpt.get().getItem().getStore().getSeller().getUser().getId().equals(sellerId)) {
				throw new Exception("판매자 정보 불일치");
			} else if (optionOpt.get().getItem().getOptions().size() == 1) {
				throw new Exception("필수 옵션 제거 불가");
			} else {
				Options option = optionOpt.get();
				Items item = option.getItem();

				// 연관 엔티티 정리
				option.getCarts().clear();
				option.getEventItems().clear();

                item.getOptions().remove(option);

                // 삭제 실행
				optionRepository.delete(option);
				optionRepository.flush();
			}
		} catch (Exception e) {
			throw e;
		}
	}
}
