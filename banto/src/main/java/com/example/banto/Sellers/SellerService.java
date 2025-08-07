package com.example.banto.Sellers;

import com.example.banto.Authentications.AuthService;
import com.example.banto.Exceptions.CustomExceptions.AuthenticationException;
import com.example.banto.JWTs.JwtUtil;
import com.example.banto.JWTs.RefreshToken;
import com.example.banto.JWTs.RefreshTokenRepository;
import com.example.banto.Users.Users;
import com.example.banto.Utils.PageDTO;
import com.example.banto.Exceptions.CustomExceptions.ResourceNotFoundException;
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

@Service
@RequiredArgsConstructor
public class SellerService {

	private final SellerRepository sellerRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final AuthService authService;
	private final JwtUtil jwtUtil;

	public String loginSeller(){
		// 1. User 인증 확인
		Users user = authService.authToUser(SecurityContextHolder.getContext().getAuthentication());
		// 2. 토큰 재생성
		RefreshToken refreshToken = refreshTokenRepository.findById(user.getId())
			.orElseThrow(() -> new AuthenticationException("만료된 토큰입니다. 재로그인하세요."));
		String sellerToken = jwtUtil.generateToken(user.getId(), "ROLE_SELLER");
		// 3. 리프레시 토큰에 반영
		refreshToken.setJwtToken(sellerToken);
		refreshTokenRepository.save(refreshToken);
		// 4. 새 토큰 반환
		return sellerToken;
	}

	public SellerDTO getSeller() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		// 1. 판매자 인증 확인
		authService.authToSeller(authentication);
		// 2. 판매자 정보 가져오기 (DTO 매핑)
        return sellerRepository.findByUserId(
			Long.parseLong(authentication.getName())
		).map(SellerDTO::toDTO).orElseThrow(() ->
			new ResourceNotFoundException("판매자의 회원 정보가 없습니다."));
	}

	public SellerDTO getSellerForAdmin(Long userId){
		// 1. 관리자 인증 확인
		authService.authToAdmin(SecurityContextHolder.getContext().getAuthentication());
		// 2. 판매자 정보 가져오기 (DTO 매핑)
		return sellerRepository.findByUserId(userId)
			.map(SellerDTO::toDTO).orElseThrow(() ->
			new ResourceNotFoundException("판매자의 회원 정보가 없습니다."));
	}

	public PageDTO getSellerList(int page){
		// 1. 관리자 인증 확인
		authService.authToAdmin(SecurityContextHolder.getContext().getAuthentication());
		// 2. 페이지 객체 생성
		Pageable pageable = PageRequest.of(page, 20);
		// 3. Seller 페이징 조회
		Page<Sellers> sellerPages = sellerRepository.findAll(pageable);
		// 4. 비어있으면 빈 객체 반환
		if(sellerPages.isEmpty()){
			return new PageDTO(new ArrayList<>(), sellerPages.getTotalPages());
		}
		// 5. DTO 매핑
		List<SellerDTO> sellerList = DTOMapper.convertList(sellerPages.stream(), SellerDTO::toDTO);
		// 6. PageDTO로 감싸서 반환
		return new PageDTO(sellerList, sellerPages.getTotalPages());
	}

	@Transactional
	public void bannedByAdmin(Long id){
		// 1. 관리자 인증 확인
		authService.authToAdmin(SecurityContextHolder.getContext().getAuthentication());
		// 2. 판매자 정보 확인
		Sellers seller = sellerRepository.findByUserId(id)
			.orElseThrow(()-> new ResourceNotFoundException("판매자 정보가 없습니다."));
		// 3. 판매자 블랙리스트 처리
		seller.setIsBanned(true);
	}

	@Transactional
	public void delete() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		// 1. 판매자 인증 유효 확인
		authService.authToSeller(authentication);
		// 2. 판매자 정보 확인
		Sellers seller = sellerRepository.findByUserId(
			Long.parseLong(authentication.getName())
		).orElseThrow(()-> new ResourceNotFoundException("판매자 정보가 없습니다."));
		/*
		// 3. 판매자가 연결된 신청서 조회

		// 4. 승인된 신청서 조회 후 삭제 처리 (블랙리스트가 아님)

		// 5. 판매자의 매장 -> 물건 -> 옵션 / 댓글 / 후기 조회

		// 6. 역순으로 삭제 처리


		// 신청된 신청서에 대한 Ban 처리
		List<SellerAuths> applys = applyRepository.findAllByUserId(seller.getUser().getId());
		if(!applys.isEmpty()){
			for(SellerAuths apply : applys){
				if(apply.getAuth().equals(ApplyType.Accepted)){
					apply.setAuth(ApplyType.Banned);
					applyRepository.save(apply);
				}
			}
		}
		// 연관관계 삭제 로직
		List<Stores> stores = storeRepository.findAllBySellerIdToEntity(seller.getId());
		if(!stores.isEmpty()){
			for(Stores store : stores){
				if(!store.getItems().isEmpty()){
					for(Items item : store.getItems()){
						List<GroupItemPays> payments = groupBuyPayRepository.findByItemId(item.getId());
						for(GroupItemPays payment : payments){
							payment.setItem(null);
							groupBuyPayRepository.save(payment);
						}
					}
				}
			}
		}
		seller.setUser(null);
		user.setSellers(null);
		userRepository.save(user);
		sellerRepository.delete(seller);

		 */
	}
}
