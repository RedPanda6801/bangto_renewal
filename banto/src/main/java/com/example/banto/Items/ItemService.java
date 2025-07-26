package com.example.banto.Items;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import com.example.banto.Authentications.AuthService;
import com.example.banto.Configs.EnvConfig;
import com.example.banto.DTOs.PageDTO;
import com.example.banto.Enums.CategoryType;
import com.example.banto.Exceptions.ForbiddenException;
import com.example.banto.Exceptions.ImageHandleException;
import com.example.banto.Exceptions.InternalServerException;
import com.example.banto.Exceptions.ResourceNotFoundException;
import com.example.banto.Options.Options;
import com.example.banto.Sellers.Sellers;
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
	private final ItemImagesRepository itemImagesRepository;
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

	public PageDTO getFilteredItemList(SearchDTO dto) {
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

	@Transactional
	public void addItem(ItemDTO itemDTO, List<MultipartFile> files) {
		// 1. 판매자 권한 확인
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		authService.authToSeller(authentication);
		// 2. 추가하려는 store 확인
		Stores store = storeRepository.findById(itemDTO.getStorePk())
			.orElseThrow(() -> new ResourceNotFoundException("매장 정보가 없습니다."));
		// 3. 판매자와 매장의 권한 확인
		if(!Objects.equals(
			Long.parseLong(authentication.getName()), store.getSeller().getUser().getId()
		)){
			throw new ForbiddenException("매장에 대한 권한이 없습니다.");
		}
		// 4. item 엔티티 먼저 저장
		// 4-1. entity -> dto
		Items item = Items.toEntity(itemDTO);
		// 4-2. 연관관계 설정
		item.setStore(store);
		// 4-3. DB 반영 (영속성 반영)
		itemRepository.save(item);
		// 5. 이미지 파일 저장
		String savePath = envConfig.get("FRONTEND_UPLOAD_ADDRESS");
		// 5-1. 파일이 비어있지 않으면 저장 준비
		if(!files.isEmpty()) {
			try{
				// 5-2. 파일 순회
				for(MultipartFile file : files) {
					// 5-3. 파일 이름 가져오기
					String originFileName = file.getOriginalFilename();
					// 파일 이름과 확장자를 분리하기 위한 index
					int dotInd = originFileName.indexOf(".");
					// 파일 이름
					String before = originFileName.substring(0, dotInd);
					// 확장자
					String ext = originFileName.substring(dotInd);
					// 5-4. 파일 이름에 UUID(식별자) 추가
					String newFileName = before + "(" + UUID.randomUUID() + ")" + ext;
					// 5-5. 정해둔 경로에 파일 저장
					file.transferTo(new java.io.File(savePath + newFileName));
					// 5-6. 이미지 DB에 반영
					ItemImages image = new ItemImages();
					image.setImgUrl(newFileName);
					image.setItem(item);
					itemImagesRepository.save(image);
				}
			}catch (Exception e){
				// 5-7. 파일 저장 실패 시 예외 처리
				throw new ImageHandleException("이미지 저장에 오류가 발생했습니다.");
			}
		}
	}

	@Transactional
	public void updateItem(ItemDTO dto, List<MultipartFile> files) {
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
		// 4. 수정 로직
		item.setTitle((dto.getTitle() != null && !dto.getTitle().trim().isEmpty()) ?
			dto.getTitle() : item.getTitle());
		item.setContent((dto.getContent() != null && !dto.getContent().trim().isEmpty()) ?
			dto.getContent() : item.getContent());
		item.setCategory(dto.getCategory() != null && CategoryType.contains(dto.getCategory()) ?
			dto.getCategory() : item.getCategory());
		// 5. 이미지 저장 위치 및 저장
		String savePath = envConfig.get("FRONTEND_UPLOAD_ADDRESS");
		// 5-1. 저장되어있는 ItemImage 리스트 가져오기
		List<ItemImages> currentImg = itemImagesRepository.findAllByItemId(item.getId());
		List<String> currentImageNames = currentImg.stream().map(img ->{
			try{
				return img.getImgUrl();
			}catch(Exception e){
				return null;
			}
		}).toList();
		// 5-2. 받은 이미지가 있는지 확인
		if(!files.isEmpty()) {
			List<String> fileNameList = new ArrayList<>();
			// 5-3. 받은 이미지에 대한 추가 처리
			for (MultipartFile file : files) {
				String originalFileName = file.getOriginalFilename();
				fileNameList.add(originalFileName);
				// 5-3-1. 기존 파일에 있는 파일이면 저장 및 삭제 X
				if (!currentImageNames.isEmpty() && currentImageNames.contains(originalFileName)) {
					continue;
				}
				// 5-3-2. 추가되는 파일에 대한 작업
				try {
					int dotInd = originalFileName.indexOf(".");
					String before = originalFileName.substring(0, dotInd);
					String ext = originalFileName.substring(dotInd);
					String newFileName = before + "(" + UUID.randomUUID() + ")" + ext;
					file.transferTo(new java.io.File(savePath + newFileName));
					// 5-3-3. 이미지 DB에 반영
					ItemImages image = new ItemImages();
					image.setImgUrl(newFileName);
					image.setItem(item);
					itemImagesRepository.save(image);
				} catch (Exception e) {
					// 5-3-4. 파일 저장 실패 시 예외 처리
					throw new ImageHandleException("이미지 저장에 오류가 발생했습니다.");
				}
			}
			// 6. 제거된 이미지에 대한 삭제 처리
			for(ItemImages beforeImage : currentImg){
				// 6-1. 중복된 파일 여부 확인
				if(!fileNameList.contains(beforeImage.getImgUrl())){
					beforeImage.setItem(null);
					itemImagesRepository.delete(beforeImage);
				}
			}
		}else{
			// 7. 비어있으므로 전부 삭제 처리
			for(ItemImages image : currentImg){
				image.setItem(null);
				itemImagesRepository.delete(image);
			}
		}
	}

	@Transactional
	public void deleteItem(ItemDTO dto){
		/*
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
		 */
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
}
