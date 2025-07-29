package com.example.banto.Carts;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItem {
    Long optionId;

    Integer amount;

    public static CartItem toObject(Long optionId, Integer amount){
        return CartItem.builder()
            .optionId(optionId)
            .amount(amount)
            .build();
    }
}
