package com.example.banto.Options;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionDTO {

    private Long id;
    
    private Integer addPrice;
    
    private String optionInfo;
 
    private Long itemPk;
    
    private Integer amount;

    private List<String> optionImages;

    public static OptionDTO toDTO(Options entity){
        return OptionDTO.builder()
            .addPrice(entity.getAddPrice())
            .optionInfo(entity.getOptionInfo())
            .amount(entity.getAmount())
            .optionImages(entity.getOptionImages().stream().map(img -> {
                try{
                    return img.getImgUrl();
                }catch (Exception e){
                    return null;
                }
            }).collect(Collectors.toList()))
            .build();
    }
}
