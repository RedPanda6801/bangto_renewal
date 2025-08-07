package com.example.banto.Payments;

import com.example.banto.Authentications.AuthService;
import com.example.banto.Carts.CartDTO;
import com.example.banto.Carts.CartRepository;
import com.example.banto.Enums.DeliverType;
import com.example.banto.Exceptions.CustomExceptions.*;
import com.example.banto.Options.OptionRepository;
import com.example.banto.Options.Options;
import com.example.banto.Users.Users;
import com.example.banto.Utils.DTOMapper;
import com.example.banto.Utils.PageDTO;
import com.example.banto.Utils.PurchaseHandler;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PayService {
	private final PaymentRepository paymentRepository;
	private final AuthService authService;
	private final CartRepository cartRepository;
	private final OptionRepository optionRepository;
	private final SoldItemsRepository soldItemsRepository;

	@Transactional
	public void payCart(PaymentDTO dto) {
		// 1. 인증 유효 확인
		Users user = authService.authToUser(SecurityContextHolder.getContext().getAuthentication());
		// 2. 구매할 목록 가져오기
		List<CartDTO> cartList = dto.getCartList();
		// 3. 구매 정보가 없으면 실패
		if(cartList.isEmpty()){
			throw new ResourceNotFoundException("구매할 목록이 일치하지 않습니다.");
		}
		// 4. 결제 목록 매핑
		List<SoldItems> soldItemsList = new ArrayList<>();
		int totalPrice = cartList.stream().mapToInt(cart -> {
			// 4-1. 결제할 옵션 찾기
			Options option = optionRepository.findById(cart.getOptionPk())
				.orElseThrow(() -> new ResourceNotFoundException("구매할 물건이 존재하지 않습니다."));
			// 4-2. 결제 물건 Entity를 List에 추가
			soldItemsList.add(SoldItems.toEntity(user, option, cart.getAmount()));
			// 4-3. 총값을 합계로 내기 위한 return
			return PurchaseHandler.priceCulc(option, cart.getAmount());
		}).sum();
		// 5. 결제 처리
		// 5-1. 잔액 확인 처리
		if(user.getCash() < totalPrice){
			throw new FailedPaymentException("잔액이 부족합니다.");
		}
		// 5-2. 결제 처리
		try{
			user.setCash(user.getCash() - totalPrice);
		}catch (Exception e){
			throw new InternalServerException("결제에 문제가 발생했습니다.");
		}
		// 6. Payment DB 반영
		Payments payment = Payments.toEntity(totalPrice, user);
		paymentRepository.save(payment);
		// 7. SoldItem - Payment 관계 설정 및 DB 반영
		for(SoldItems soldItem : soldItemsList){
			soldItem.setPayment(payment);
			soldItemsRepository.save(soldItem);
		}
	}
	
	public PageDTO getPaymentList(Integer year, Integer page) {
		// 1. 유저 정보 확인
		Users user = authService.authToUser(SecurityContextHolder.getContext().getAuthentication());
		// 2. Pageable 객체 생성
		Pageable pageable = PageRequest.of(page, 20, Sort.by("payDate").descending());
		// 3. 년도 별로 DB에 검색
		Page<Payments> paymentPages = paymentRepository.findAllByUserIdAndYear(
				user.getId(),
				LocalDateTime.of(year, 1, 1,0,0,0),
				LocalDateTime.of(year, 12, 31,23,59,59),
				pageable);
		// 4. 비어있으면 빈객체 반환
		if(paymentPages.isEmpty()){
			return new PageDTO(new ArrayList<>(), paymentPages.getTotalPages());
		}
		// 5. entity -> dto
		List<PaymentDTO> paymentList = DTOMapper.convertList(paymentPages.stream(), PaymentDTO::toDTO);
		return new PageDTO(paymentList, paymentPages.getTotalPages());

	}

	public PageDTO getPaymentListForAdmin(Long userId, Integer year, Integer page) {
		// 1. 관리자 권한 확인
		authService.authToAdmin(SecurityContextHolder.getContext().getAuthentication());
		// 2. 페이징 객체 생성
		Pageable pageable = PageRequest.of(page-1, 20, Sort.by("id").ascending());
		// 3. 사용자 정보로 조회
		Page<Payments> paymentPages = paymentRepository.findAllByUserIdAndYear(
			userId,
			LocalDateTime.of(year, 1, 1,0,0,0),
			LocalDateTime.of(year, 12, 31,23,59,59),
			pageable);
		// 4. 비어있으면 빈객체 반환
		if(paymentPages.isEmpty()){
			return new PageDTO(new ArrayList<>(), paymentPages.getTotalPages());
		}
		// 5. entity -> dto
		List<PaymentDTO> paymentList = DTOMapper.convertList(paymentPages.stream(), PaymentDTO::toDTO);
		return new PageDTO(paymentList, paymentPages.getTotalPages());
	}

	@Transactional
	public void processPayment(List<SoldItemDTO> dto, String deliver) {
		// 1. 인증 유효 확인
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		authService.authToSeller(authentication);
		// 2. 판매된 물건 조회
		List<Long> soldItemIdList = dto.stream()
			.mapToLong(SoldItemDTO::getId)
			.boxed().toList();
		List<SoldItems> soldItemList = soldItemsRepository.findAllById(soldItemIdList);
		if(soldItemList.isEmpty() || soldItemList.size() != dto.size()){
			throw new ResourceNotFoundException("결제 내역 조회를 할 수 없습니다.");
		}
		// 3. 배송 처리 정보에 대한 유효성 확인
		if(!deliver.equals(DeliverType.Delivering.toString()) ||
			!deliver.equals(DeliverType.Delivered.toString())){
			throw new ValidationException("배송 처리 정보가 잘못되었습니다.");
		}
		// 4. 물건 일괄 처리
		for(SoldItems soldItem : soldItemList){
			// 4-1. 물품에 대한 권한 확인
			if(!Objects.equals(soldItem.getOption().getItem().getStore().getSeller().getUser().getId(),
				Long.parseLong(authentication.getName()))){
				throw new ForbiddenException("배송처리에 대한 권한이 없습니다.");
			}
			DeliverType currentDeliverState = soldItem.getDeliverInfo();
			// 4-2. 배송 이전 물건에 대한 처리
			if(currentDeliverState.equals(DeliverType.Preparing) &&
				deliver.equals(DeliverType.Delivering.toString())){
				soldItem.setDeliverInfo(DeliverType.Delivering);
				// 4-3. 배송 중인 상품에 대한 처리
			}else if(currentDeliverState.equals(DeliverType.Delivering) &&
				deliver.equals(DeliverType.Delivered.getValue())){
				// 4-3-2. 값 수정
				soldItem.setDeliverInfo(DeliverType.Delivering);
			}else{
				throw new DuplicateResourceException("배송 처리에 대한 정보가 잘못되었습니다.");
			}
		}
	}
	
	public PageDTO getSoldListForSeller(Long storeId, Integer page) {
		// 1. 인증 유효 확인
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		authService.authToSeller(authentication);
		// 2. 페이징 객체 생성
		Pageable pageable = PageRequest.of(page, 20, Sort.by("payDate").descending());
		// 3. 매장 정보로 조회
		Page<SoldItems> soldItemPages = soldItemsRepository.findAllByStoreId(storeId, pageable);
		// 4. 빈 값에 대한 예외처리
		if(soldItemPages.isEmpty()){
			return new PageDTO(new ArrayList<>(), soldItemPages.getTotalPages());
		}
		// 5. 매장 정보의 판매자 권한 확인
		for(SoldItems soldItem : soldItemPages){
			if(!Objects.equals(soldItem.getOption().getItem().getStore().getSeller().getUser().getId(),
				Long.parseLong(authentication.getName()))){
				throw new ForbiddenException("매장의 판매 내역 조회 권한이 없습니다.");
			}
		}
		// 6. DTO 변환
		List<SoldItemDTO> soldItemList = DTOMapper.convertList(soldItemPages.stream(), SoldItemDTO::toDTO);
		return new PageDTO(soldItemList, soldItemPages.getTotalPages());
	}

	public PageDTO getSoldListForAdmin(Long storeId, Integer page) {
		// 1. 인증 유효 확인
		authService.authToAdmin(SecurityContextHolder.getContext().getAuthentication());
		// 2. 페이징 객체 생성
		Pageable pageable = PageRequest.of(page, 20, Sort.by("payDate").descending());
		// 3. 매장 정보로 조회
		Page<SoldItems> soldItemPages = soldItemsRepository.findAllByStoreId(storeId, pageable);
		// 4. 빈 값에 대한 예외처리
		if(soldItemPages.isEmpty()){
			return new PageDTO(new ArrayList<>(), soldItemPages.getTotalPages());
		}
		// 5. DTO 변환
		List<SoldItemDTO> soldItemList = DTOMapper.convertList(soldItemPages.stream(), SoldItemDTO::toDTO);
		return new PageDTO(soldItemList, soldItemPages.getTotalPages());
	}
}
