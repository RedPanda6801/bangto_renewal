package com.example.banto.SellerAuths;

import com.example.banto.Authentications.AuthService;
import com.example.banto.DTOs.PageDTO;
import com.example.banto.Enums.ApplyType;
import com.example.banto.Exceptions.*;
import com.example.banto.Exceptions.IllegalArgumentException;
import com.example.banto.Stores.StoreRepository;
import com.example.banto.Sellers.SellerRepository;
import com.example.banto.Sellers.Sellers;
import com.example.banto.Stores.Stores;
import com.example.banto.Users.Users;
import com.example.banto.Utils.DTOMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellerAuthService {

	private final SellerAuthRepository sellerAuthRepository;
	private final SellerRepository sellerRepository;
	private final StoreRepository storeRepository;
	private final AuthService authService;

	@Transactional
	public void apply(SellerAuthDTO sellerAuthDTO) {
		// 1. 로그인 정보 확인
		Users user = authService.authToUser(SecurityContextHolder.getContext().getAuthentication());
		// 2. 판매자 중복 확인
		sellerRepository.findByUserId(user.getId()).ifPresent(e ->{
			throw new DuplicateUserException("이미 존재하는 판매자입니다.");
		});
		// 3. 존재하는 매장에서 사업자 번호 중복 확인
		storeRepository.findByBusiNum(sellerAuthDTO.getBusiNum()).ifPresent(e ->{
			throw new DuplicateResourceException("중복된 사업자 번호입니다.");
		});
		// 4. 신청 진행중(반려 또는 승인됨 X)인 신청서 확인
		sellerAuthRepository.findProcessingByUserId(user.getId()).ifPresent( e -> {
			throw new DuplicateResourceException("이미 판매자 인증이 진행중입니다.");
		});
		// 5. 신청서 등록(DTO -> Entity)
		SellerAuths entity = SellerAuths.toEntity(sellerAuthDTO, user);
		// 6. DB에 반영
		sellerAuthRepository.save(entity);
	}

	public List<SellerAuthDTO> getSellerAuthList() {
		// 1. 인증 유효 확인
		Users user = authService.authToUser(SecurityContextHolder.getContext().getAuthentication());
		// 2. 내 신청서 조회
		List<SellerAuths> sellerAuths = sellerAuthRepository.findAllByUserId(user.getId());
		// 3. 신청서가 비어있으면 빈 객체 반환
		if(sellerAuths.isEmpty()) {
			return new ArrayList<>();
		}
		// 4. DTO에 매핑 후 ArrayList로 묶어 반환
		 return DTOMapper.convertList(sellerAuths.stream(), SellerAuthDTO::toDTO);
	}

	@Transactional
	public void process(ProcessDTO dto) {
		// 1. 관리자 권한 인증
		authService.authToAdmin(SecurityContextHolder.getContext().getAuthentication());
		// 2. 판매자 신청서 조회
		SellerAuths apply = sellerAuthRepository.findById(dto.getSellerAuthPk())
			.orElseThrow(() -> new ResourceNotFoundException("처리할 신청서를 찾을 수 없습니다."));
		// 3. 신청서 처리 여부 확인
		if(apply.getAuth() != ApplyType.Processing){
			throw new AlreadyProcessedException("이미 처리된 신청입니다.");
		}
		String process = dto.getProcess();
		// 4. 신청서 반려 시
		if(process.equals("Duplicated")) {
			// 4-1. 반려로 변경
			apply.setAuth(ApplyType.Duplicated);
		}
		// 5. 신청서 승인 시
		else if(!process.equals("Accepted")) {
			throw new IllegalArgumentException("유효하지 않은 처리 입력값입니다.");
		}
		else {
			// 5-1. 승인으로 변경
			apply.setAuth(ApplyType.Accepted);
			apply.setApplyDate(LocalDateTime.now());
			// 5-2. 판매자 매핑 후 생성
			Sellers seller = Sellers.toEntity(apply.getUser());
			sellerRepository.save(seller);
			// 5-3. 매장 매핑 후 생성
			Stores store = Stores.toInitEntity(apply, seller);
			storeRepository.save(store);
		}
	}
	
	public PageDTO getApplyListForAdmin(Integer page) {
		// 1. 관리자 권한 인증
		authService.authToAdmin(SecurityContextHolder.getContext().getAuthentication());
		// 2. 페이징 객체 생성
		Pageable pageable = PageRequest.of(page, 20);
		// 3. 패이징 처리한 인증서 리스트 반환
		Page<SellerAuths> sellerAuthPages = sellerAuthRepository.findAll(pageable);
		// 4. 빈 값이면 빈 리스트 반환
		if(sellerAuthPages.isEmpty()) {
			return new PageDTO(new ArrayList<>(), sellerAuthPages.getTotalPages());
		}
		// 5. 배열로 반환 시 단순하게 출력 (단순 -> 세부)
		List<SellerAuthDTO> applyList = DTOMapper.convertList(sellerAuthPages.stream(), SellerAuthDTO::toDTO);
		// 6. Page 객체로 감싸고 반환
		return new PageDTO(applyList, sellerAuthPages.getTotalPages());
	}
	
	public SellerAuthDTO getApplyForAdmin(Long sellerAuthId) {
		// 1. 관리자 권한 인증
		authService.authToAdmin(SecurityContextHolder.getContext().getAuthentication());
		// 2. id로 인증서 조회
		SellerAuths auth = sellerAuthRepository.findById(sellerAuthId)
			.orElseThrow(() -> new ResourceNotFoundException("조회할 신청서가 없습니다."));
		// 3. DTO로 매핑 후 반환
		return SellerAuthDTO.toDTO(auth);
	}
}
