package com.example.banto.Items;

import com.example.banto.Enums.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemSearchLog {
    private Long itemId;
    private String title;
    private String category;
    private Integer price;

    public static ItemSearchLog setLog(Items item){
        return ItemSearchLog.builder()
            .itemId(item.getId())
            .title(item.getTitle())
            .category(item.getCategory().name())
            .price(item.getPrice())
            .build();
    }
}
