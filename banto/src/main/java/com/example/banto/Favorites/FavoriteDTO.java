package com.example.banto.Favorites;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavoriteDTO {
    private Long id;
    private Long userPk;
    @Valid
    private Long itemPk;
}
