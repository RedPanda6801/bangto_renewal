package com.example.banto.Carts;

import com.example.banto.Authentications.AuthService;
import com.example.banto.Configs.RedisConfig;
import com.example.banto.Exceptions.ResourceNotFoundException;
import com.example.banto.Options.OptionRepository;
import com.example.banto.Options.Options;
import jakarta.servlet.http.HttpServletRequest;
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
	private final RedisConfig redisConfig;

	public void upsert(CartDTO dto, HttpServletRequest request) {
		// 1. 유저 확인
		String sessionId = authService.authToRedis(SecurityContextHolder.getContext().getAuthentication(), request);
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
	
	public List<CartDTO> getCart(HttpServletRequest request) {
		// 1. 인증 유효 확인
		String sessionId = authService.authToRedis(SecurityContextHolder.getContext().getAuthentication(), request);
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

	public void delete(Long optionId, HttpServletRequest request) {
		// 1. 인증 유효 확인
		String sessionId = authService.authToRedis(SecurityContextHolder.getContext().getAuthentication(), request);
		// 2. Cart 찾기
		Carts cart = cartRepository.findById(sessionId)
			.orElseThrow(() -> new ResourceNotFoundException("삭제할 물건 목록이 없습니다."));
		// 3. hashMap에서 삭제
		cart.getCartMap().remove(optionId);
		// 4. Redis에 갱신
		cartRepository.save(cart);
	}
}
