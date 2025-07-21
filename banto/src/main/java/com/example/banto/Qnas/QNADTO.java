package com.example.banto.Qnas;

import java.time.LocalDateTime;

import com.example.banto.Items.Items;
import com.example.banto.Options.Options;
import com.example.banto.Users.Users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QNADTO {
    private Long id;
    
    private String qContent;
    
    private String aContent;

    private LocalDateTime qWriteDate;
    
    private LocalDateTime aWriteDate;

    private String userName;

    private Long optionPk;

    private Long userPk;
    
    private Long storePk;
    
    private Users user;

    private Options option;

    private Items item;
    
    public static QNADTO toDTO(QNAs entity) {
        return QNADTO.builder()
                .id(entity.getId())
                .qContent(entity.getQContent())
                .aContent(entity.getAContent())
                .qWriteDate(entity.getQWriteDate())
                .aWriteDate(entity.getAWriteDate())
                .userName(entity.getUser() == null ? "삭제된 유저" : entity.getUser().getName())
                .item(entity.getOption().getItem())
                .option(entity.getOption())
                .build();
    }
}
