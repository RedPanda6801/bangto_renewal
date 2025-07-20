package com.example.banto.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionDTO {

    private Integer id;
    
    private Integer addPrice;
    
    private String optionInfo;
 
    private Integer itemPk;
    
    private Integer amount;
}
