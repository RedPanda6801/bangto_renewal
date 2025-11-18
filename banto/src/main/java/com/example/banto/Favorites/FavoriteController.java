package com.example.banto.Favorites;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FavoriteController {

    private final FavoriteService favoriteService;
    // 찜 추가
    @PostMapping("/favorite")
    public ResponseEntity<?> addFavorite(@Valid @RequestBody FavoriteDTO favoriteDTO){
        favoriteService.insert(favoriteDTO);
        return ResponseEntity.ok().body("물건을 찜했습니다.");
    }
}
