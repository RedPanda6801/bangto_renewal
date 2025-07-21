package com.example.banto.SoldItems;

import com.example.banto.Enums.DeliverType;
import com.example.banto.Options.Options;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SoldItemDTO {
	private Long id;
	private String itemName;
    private Integer amount;
    private String optionInfo;
    private Integer soldPrice;
    private DeliverType deliverInfo;
    private Long optionPk;
    private Long userPk;
    private Long storePk;
    
    public static SoldItemDTO toDTO(SoldItems entity) {
        return SoldItemDTO.builder()
            .id(entity.getId())
            .amount(entity.getAmount())
            .deliverInfo(entity.getDeliverInfo())
            .itemName(entity.getItemName())
            .soldPrice( culcPricePerSoldItem(
                    entity.getOption(), entity.getAmount()
                )
            ).optionInfo(entity.getOption().getOptionInfo())
            .optionPk(entity.getOption().getId())
            .soldPrice(entity.getSoldPrice())
            .userPk(entity.getUser().getId())
            .build();
    }

    public static Integer culcPricePerSoldItem(Options option, int amount){
        return (option.getAddPrice() + option.getItem().getPrice()) * amount;
    }
}
