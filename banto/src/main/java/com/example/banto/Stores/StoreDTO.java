package com.example.banto.Stores;

import java.util.List;
import java.util.stream.Collectors;

import com.example.banto.Items.ItemDTO;
import com.example.banto.Items.Items;
import com.example.banto.Sellers.Sellers;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotNull;
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

    private String storeName;

    private String sellerName;
    @NotNull
    private String busiNum;

    private List<ItemDTO> items;

    public static StoreDTO toDTO(Stores entity) {
        return StoreDTO.builder()
                .id(entity.getId())
                .storeName(entity.getStoreName())
                .sellerName(entity.getSeller().getUser().getName())
                .busiNum(entity.getBusiNum())
                .items(entity.getItems().stream().map(item -> {
                    try{
                        return ItemDTO.toDTO(item);
                    }catch(Exception e){
                        return null;
                    }
                }).collect(Collectors.toList()))
                .build();
    }
}
