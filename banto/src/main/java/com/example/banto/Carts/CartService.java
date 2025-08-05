package com.example.banto.Carts;

import com.example.banto.Authentications.AuthService;
import com.example.banto.Exceptions.ResourceNotFoundException;
import com.example.banto.Options.OptionRepository;
import com.example.banto.Options.Options;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CartService {

	private final CartRepository cartRepository;
	private final OptionRepository optionRepository;
	private final AuthService authService;

	public void upsert(CartDTO dto, HttpServletRequest request, HttpServletResponse response) {
		// 1. 유저 확인
		String sessionId = authService.authToRedis(SecurityContextHolder.getContext().getAuthentication(), request, response);
		// 2. 데이터 확인
		Carts cart = cartRepository.findById(sessionId).orElse(null);
       	// 3. Cart 값이 없으면 새 값 생성
		if(cart == null){
			cart = new Carts();
			cart.setSessionId(sessionId);
			cart.setCartMap(new HashMap<>());
		}
		// 4. 새로운 값으로 대체
		CartItem newItem = CartItem.toObject(dto.getOptionPk(), dto.getAmount());
		cart.getCartMap().put(dto.getOptionPk(), newItem);
		cartRepository.save(cart);
	}
	
	public List<CartDTO> getCart(HttpServletRequest request, HttpServletResponse response) {
		// 1. 인증 유효 확인
		String sessionId = authService.authToRedis(SecurityContextHolder.getContext().getAuthentication(), request, response);
		// 2. sessionId로 cartList 찾기
		Carts cart = cartRepository.findById(sessionId).orElse(null);
		// 3. cart가 없으면 return null
		if(cart == null){
			return new ArrayList<>();
		}
		// 4. optionId로 option 전부 찾기
		List<Options> optionList = optionRepository.findAllById(cart.getCartMap().keySet());
		// 5. DTO로 변환
		List<CartDTO> cartList = new ArrayList<>();
		for(Options option : optionList){
			cartList.add(
				CartDTO.toDTO(option, cart.getCartMap().get(option.getId()))
			);
		}
		// 6. 반환
		return cartList;
	}

	public void delete(Long optionId, HttpServletRequest request, HttpServletResponse response) {
		// 1. 인증 유효 확인
		String sessionId = authService.authToRedis(SecurityContextHolder.getContext().getAuthentication(), request, response);
		// 2. Cart 찾기
		Carts cart = cartRepository.findById(sessionId)
			.orElseThrow(() -> new ResourceNotFoundException("삭제할 물건 목록이 없습니다."));
		// 3. hashMap에서 삭제
		cart.getCartMap().remove(optionId);
		// 4. Redis에 갱신
		cartRepository.save(cart);
	}

	public void mergeCartOnLogin(String cartId, Long userId, HttpServletResponse response) {
		// 1. Redis에서 userId와 cartId로 카드 조회
		String userStrId = String.valueOf(userId);
		Carts guestCart = cartRepository.findById(cartId).orElse(null);
		Carts userCart = cartRepository.findById(userStrId).orElse(new Carts());
		// 2. 비회원 장바구니가 존재하면 병합
		if (guestCart != null) {
			Map<Long, CartItem> guestItems = guestCart.getCartMap();
			Map<Long, CartItem> userItems = userCart.getCartMap();
			// 3. 병합 로직
			for (Map.Entry<Long, CartItem> entry : guestItems.entrySet()) {
				userItems.merge(entry.getKey(), entry.getValue(), (oldVal, newVal) -> {
					oldVal.setAmount(oldVal.getAmount() + newVal.getAmount());
					return oldVal;
				});
			}
			// 4. Redis에 반영
			cartRepository.save(userCart);
			cartRepository.deleteById(cartId);

			// 5. Cookie에 반영
			Cookie expiredCookie = new Cookie(cartId, null);
			expiredCookie.setMaxAge(0);
			expiredCookie.setPath("/");
			response.addCookie(expiredCookie);
		}
	}
}
