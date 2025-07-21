package com.example.banto.Options;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
