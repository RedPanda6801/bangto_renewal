package com.example.banto.Boards.Qnas;

import java.time.LocalDateTime;
import java.util.List;

import com.example.banto.Items.Items;
import com.example.banto.Options.Options;
import com.example.banto.Users.Users;

import com.example.banto.Utils.DTOMapper;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private String qContent;
    
    private String aContent;

    private LocalDateTime qWriteDate;
    
    private LocalDateTime aWriteDate;

    private String userName;
    @NotNull
    private Long optionPk;

    private Long userPk;
    
    private Long storePk;

    private String itemName;

    private String optionName;

    private List<String> imageList;
    
    public static QNADTO toDTO(QNAs entity) {
        return QNADTO.builder()
            .id(entity.getId())
            .qContent(entity.getQContent())
            .aContent(entity.getAContent())
            .qWriteDate(entity.getQWriteDate())
            .aWriteDate(entity.getAWriteDate())
            .userName(entity.getUser() == null ? "삭제된 유저" : entity.getUser().getName())
            .itemName(entity.getOption().getItem().getTitle())
            .optionName(entity.getOption().getOptionInfo())
            .imageList(entity.getQnaImages().stream().map(image ->{
                try{
                    return image.getImgUrl();
                }catch (Exception e){
                    return null;
                }
            }).toList())
            .build();
    }
}
