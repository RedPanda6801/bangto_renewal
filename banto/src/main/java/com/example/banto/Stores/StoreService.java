package com.example.banto.Stores;

import com.example.banto.Authentications.AuthService;
import com.example.banto.Utils.PageDTO;
import com.example.banto.Exceptions.ForbiddenException;
import com.example.banto.Exceptions.ResourceNotFoundException;
import com.example.banto.Sellers.SellerRepository;
import com.example.banto.Sellers.Sellers;
import com.example.banto.Utils.DTOMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StoreService {

	private final StoreRepository storeRepository;
	private final SellerRepository sellerRepository;
	private final AuthService authService;

	private Sellers authAndGetSeller(Authentication authentication){
		// 1. 판매자 인증 확인
		authService.authToSeller(authentication);
		// 2. 판매자 조회
		return sellerRepository.findByUserId(
				Long.parseLong(authentication.getName()))
			.orElseThrow(() -> new ResourceNotFoundException("판매자 정보가 없습니다."));
	}
	@Transactional
	public void create(StoreDTO dto) {
		// 1. 권한 확인 및 판매자(로그인된) 정보 가져오기
		Sellers seller = authAndGetSeller(SecurityContextHolder.getContext().getAuthentication());
		// 2. dto -> entity 매핑
		Stores store = Stores.toEntity(dto, seller);
		// 3. DB에 반영
		storeRepository.save(store);
	}
	
	public PageDTO getStoreList(Integer page) {
		// 1. 권한 확인 및 판매자(로그인된) 정보 가져오기
		Sellers seller = authAndGetSeller(SecurityContextHolder.getContext().getAuthentication());
		// 2. Pageable 객체 생성
		Pageable pageable = PageRequest.of(20, page);
		// 3. 판매자의 매장 모두 조회
		Page<Stores> storesPage =
			storeRepository.findAllBySellerId(seller.getId(), pageable);
		// 4. 매장이 비어있으면 안되기 때문에 예외처리
		if(storesPage.isEmpty()){
			throw new ResourceNotFoundException("판매자의 매장이 없습니다. 관리자에 문의하세요.");
		}
		// 5. Page<Stores> -> ArrayList<StoreDTO>
		List<StoreDTO> storeList = DTOMapper.convertList(storesPage.stream(), StoreDTO::toDTO);
		// 6. PageDTO로 감싸서 리턴
		return new PageDTO(storeList, storesPage.getTotalPages());
	}
	
	public StoreDTO getStoreDetail(Long storeId) {
		// 1. 권한 확인 및 판매자(로그인된) 정보 가져오기
		Sellers seller = authAndGetSeller(SecurityContextHolder.getContext().getAuthentication());
		// 2. 매장 조회
		Stores store = storeRepository.findById(storeId)
			.orElseThrow(() -> new ResourceNotFoundException("매장 정보가 없습니다."));
		// 3. 매장과 판매자 일치 여부 확인
		if(!Objects.equals(store.getSeller().getId(), seller.getId())){
			throw new ForbiddenException("매장에 접근할 권한이 없습니다.");
		}
		// 4. 매장 DTO 변환 후 반환
		return StoreDTO.toDTO(store);
	}

	@Transactional
	public void update(StoreDTO dto) {
		// 1. 권한 확인 및 판매자(로그인된) 정보 가져오기
		Sellers seller = authAndGetSeller(SecurityContextHolder.getContext().getAuthentication());
		// 2. 수정할 매장 조회
		Stores store = storeRepository.findById(dto.getId())
			.orElseThrow(() -> new ResourceNotFoundException("매장 정보가 없습니다."));
		// 3. 매장과 판매자 일치 여부 확인
		if(!Objects.equals(store.getSeller().getId(), seller.getId())){
			throw new ForbiddenException("매장에 접근할 권한이 없습니다.");
		}
		// 4. 매장 이름 수정 (사업자 번호는 변경이 불가능)
		store.setStoreName((dto.getStoreName() != null && !dto.getStoreName().trim().isEmpty()) ?
			dto.getStoreName() : store.getStoreName());
	}

	public PageDTO getStoreListForAdmin(Integer page) {
		// 1. 관리자 권한 확인
		authService.authToAdmin(SecurityContextHolder.getContext().getAuthentication());
		// 2. 페이징 객체 생성
		Pageable pageable = PageRequest.of(20, page);
		// 3. 페이지 별로 매장 전체 조회
		Page<Stores> storesPage = storeRepository.findAll(pageable);
		// 4. 없으면 빈 객체 반환
		if(storesPage.isEmpty()){
			return new PageDTO(new ArrayList<>(), storesPage.getTotalPages());
		}
		// 5. entity -> DTO
		List<StoreDTO> storeList = DTOMapper.convertList(storesPage.stream(), StoreDTO::toDTO);
		// 6. PageDTO로 감싸서 반환
		return new PageDTO(storeList, storesPage.getTotalPages());
	}

	public void delete(StoreDTO dto) {
		/*
		// 1. 권한 확인 및 판매자(로그인된) 정보 가져오기
		Sellers seller = authAndGetSeller(SecurityContextHolder.getContext().getAuthentication());
		// 2. 삭제할 매장 조회
		Stores store = storeRepository.findById(dto.getId())
			.orElseThrow(() -> new ResourceNotFoundException("매장 정보가 없습니다."));
		// 3. 매장과 판매자 일치 여부 확인
		if(!Objects.equals(store.getSeller().getId(), seller.getId())){
			throw new ForbiddenException("매장에 접근할 권한이 없습니다.");
		}
		// 4. 물건 삭제 시
		// 4-1. 결제 물건 및 결제 내역, 장바구니의 연결 관계 끊기
		// 4-2. Options -> (QNA, Comment), Favorite 삭제 필요
		List<Items> items = store.getItems();
		 */
	}
}
