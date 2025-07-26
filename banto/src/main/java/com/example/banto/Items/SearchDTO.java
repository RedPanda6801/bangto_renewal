package com.example.banto.Items;

import com.example.banto.Enums.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchDTO {

    private String title;

    private CategoryType category;

    private Integer maxPrice;

    private Integer minPrice;

    private Integer page;
}
