package com.example.banto.Favorites;

import com.example.banto.Exceptions.CustomExceptions.AuthenticationException;
import com.example.banto.Exceptions.CustomExceptions.ResourceNotFoundException;
import com.example.banto.Items.ItemRepository;
import com.example.banto.Items.Items;
import com.example.banto.Users.UserRepository;
import com.example.banto.Users.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final ItemRepository itemRepository;
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;

    public void insert(FavoriteDTO favoriteDTO){
        // 1. 물품 조회
        Items item = itemRepository.findById(favoriteDTO.getItemPk())
            .orElseThrow(() -> new ResourceNotFoundException("물건 정보가 없습니다."));
        // 2. 유저 조회
        Users user = userRepository.findById(
            Long.parseLong(
                SecurityContextHolder.getContext().getAuthentication().getName()
            )).orElseThrow(() -> new AuthenticationException("로그인을 해주세요."));
        // 3. DB에 추가
        Favorites favorites = new Favorites();
        favorites.setUser(user);
        favorites.setItem(item);
        favoriteRepository.save(favorites);
        // 4. Redis에 추가
    }
}
