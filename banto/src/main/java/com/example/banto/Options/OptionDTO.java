package com.example.banto.Options;

import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private Integer addPrice;
    @NotNull
    private String optionInfo;
    @NotNull
    private Long itemPk;
    @NotNull
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
