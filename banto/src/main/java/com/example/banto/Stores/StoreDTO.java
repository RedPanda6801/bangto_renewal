package com.example.banto.Stores;

import java.util.List;

import com.example.banto.Items.Items;
import com.example.banto.Sellers.Sellers;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreDTO {
   
    private Long id;

    private String name;

    private String sellerName;

    private String busiNum;
    
    private Sellers seller;

    @JsonIgnore
    private List<Items> items;

    public StoreDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    public StoreDTO(Long id, String name, String busiNum) {
        this.id = id;
        this.name = name;
        this.busiNum = busiNum;
    }

    public static StoreDTO toDTO(Stores entity) {
        return StoreDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .sellerName(entity.getSeller().getUser().getName())
                .busiNum(entity.getBusiNum())
                .seller(null)
                .items(entity.getItems())
                .build();
    }
}
