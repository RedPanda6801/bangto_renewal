package com.example.banto.Items;

import java.io.File;
import java.util.*;

import com.example.banto.Authentications.AuthService;
import com.example.banto.Configs.EnvConfig;
import com.example.banto.DTOs.PageDTO;
import com.example.banto.Enums.CategoryType;
import com.example.banto.Exceptions.ForbiddenException;
import com.example.banto.Exceptions.InternalServerException;
import com.example.banto.Exceptions.ResourceNotFoundException;
import com.example.banto.Options.Options;
import com.example.banto.Stores.StoreRepository;
import com.example.banto.Stores.Stores;
import com.example.banto.Utils.DTOMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.banto.DTOs.ResponseDTO;

@Service
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;
	private final StoreRepository storeRepository;
	private final AuthService authService;
	private final EnvConfig envConfig;

	public ItemDTO getItemDetail(Long itemId) {
		// 1. 물품 id로 조회
		Items item = itemRepository.findById(itemId)
			.orElseThrow(() -> new ResourceNotFoundException("물건 정보가 없습니다."));
		// 2. DTO로 변환 후 반환
		return ItemDTO.toDTO(item);
	}

	public PageDTO getItemList(Integer page) {
		// 1. 권한 없는 검색으로 페이징 객체 생성
		Pageable pageable = PageRequest.of(page, 20);
		// 2. 페이징 객체로 아이템 조회
		Page<Items> itemPages = itemRepository.findAll(pageable);
		// 3. 비어있으면 빈 객체 반환
		if(itemPages.isEmpty()){
			return new PageDTO(new ArrayList<>(), itemPages.getTotalPages());
		}
		// 4. Entity -> DTO
		List<ItemDTO> itemList = DTOMapper.convertList(itemPages.stream(), ItemDTO::toDTO);
		// 5. PageDTO로 감싸서 반환
		return new PageDTO(itemList, itemPages.getTotalPages());
	}
	
	public PageDTO getRecommendItemList() {
		// 1. 권한없이 검색하므로 페이징 객체 생성
		Pageable pageable = PageRequest.of(0, 20);
		// 2. Favorites 개수로 정렬해서 검색
		Page<Items> recommendItemPages = itemRepository.getItemsOrderByFavoritesSizeDesc(pageable);
		// 3. 비어있으면 빈 객체 반환
		if(recommendItemPages.isEmpty()){
			return new PageDTO(new ArrayList<>(), recommendItemPages.getTotalPages());
		}
		// 4. entity -> DTO
		List<ItemDTO> recommendItemList = DTOMapper.convertList(recommendItemPages.stream(), ItemDTO::toDTO);
		// 5. Page 객체로 리턴
		return new PageDTO(recommendItemList, recommendItemPages.getTotalPages());
	}

	public PageDTO getItemListByStore(Long storeId, Integer page) {
		// 1. 매장 조회
		Stores store = storeRepository.findById(storeId)
			.orElseThrow(() -> new ResourceNotFoundException("매장 정보가 없습니다."));
		// 2. 매장 내의 물건을 20개씩 끊어서 합치기
		List<Items> items = store.getItems();
		int maxPage = Math.max(page * 21, items.size());
		List<Items> filteredItems = store.getItems().subList(page*20, maxPage);
		// 3. Entity -> DTO
		List<ItemDTO> itemList = DTOMapper.convertList(filteredItems.stream(), ItemDTO::toDTO);
		// 4. PageDTO로 감싸서 반환
		return new PageDTO(itemList, maxPage / 20);
	}

	public PageDTO getItemListByStoreName(String storeName, Integer page) {
		// 1. 매장 조회
		Stores store = storeRepository.findByStoreName(storeName)
			.orElseThrow(() -> new ResourceNotFoundException("매장 정보가 없습니다."));
		// 2. 매장 내의 물건을 20개씩 끊어서 합치기
		List<Items> items = store.getItems();
		int maxPage = Math.max(page * 21, items.size());
		List<Items> filteredItems = store.getItems().subList(page*20, maxPage);
		// 3. Entity -> DTO
		List<ItemDTO> itemList = DTOMapper.convertList(filteredItems.stream(), ItemDTO::toDTO);
		// 4. PageDTO로 감싸서 반환
		return new PageDTO(itemList, maxPage / 20);
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
	/*
	@Transactional
	public void modifyItem(ItemDTO dto, List<MultipartFile> files) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		// 1. 팬매자 인증 확인
		authService.authToSeller(authentication);
		// 2. 수정할 물건 조회
		Items item = itemRepository.findById(dto.getId())
			.orElseThrow(() -> new ResourceNotFoundException("수정된 물건이 없습니다."));
		// 3. 수정 권한 확인
		if(!Objects.equals(Long.parseLong(
			authentication.getName()), item.getStore().getSeller().getUser().getId()
		)){
			throw new ForbiddenException("수정할 권한이 없습니다.");
		}
		// 4. 이미지 저장 위치 및 저장 확인
		String savePath = envConfig.get("FRONTEND_UPLOAD_ADDRESS");
		StringBuilder img = new StringBuilder();

		if(files != null) {
			File saveDir = new File(savePath);
			if (!saveDir.exists()) {
				throw new InternalServerException("파일 저장 디렉토리 오류");
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
	 */

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

	public PageDTO getFilteredItemList(ItemDTO dto) {
		/*
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
			category = dto.getCategory();
		}
		Pageable pageable = PageRequest.of(dto.getPage() - 1, dto.getSize(), Sort.by(sortOrder));
		Page<Items> items = itemRepository.getFilterdItemList(dto.getTitle(), dto.getStoreName(), category, pageable);
		List<ItemDTO> itemList = new ArrayList<ItemDTO>();
		for(Items item : items) {
			ItemDTO itemDTO = ItemDTO.toDTO(item);
			itemList.add(itemDTO);
		}
		return new ResponseDTO(itemList, new PageDTO(items.getSize(), items.getTotalElements(), items.getTotalPages()));

		 */
		return new PageDTO(null, null);
	}
		/*
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
	 */
}
