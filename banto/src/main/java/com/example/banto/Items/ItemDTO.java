package com.example.banto.Items;
import java.util.List;
import java.util.stream.Collectors;

import com.example.banto.Boards.Comments.CommentDTO;
import com.example.banto.Enums.CategoryType;
import com.example.banto.Options.OptionDTO;
import com.example.banto.Boards.Qnas.QNADTO;
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
public class ItemDTO {

    private Long id;

    @NotNull
    private String title;
    @NotNull
	private CategoryType category;
    @NotNull
    private Integer price;
    
    private String content;
    @NotNull
    private Long storePk;

    private String storeName;

    private String sellerName;

    private Integer star;

    private String mainImage;

    private List<String> itemImages;

    private List<OptionDTO> options;

    private List<List<QNADTO>> qnas;

    private List<List<CommentDTO>> comments;

    public static ItemDTO toListDTO(Items entity){
        return ItemDTO.builder()
            .id(entity.getId())
            .title(entity.getTitle())
            .sellerName(entity.getStore().getSeller().getUser().getName())
            .storePk(entity.getStore().getId())
            .storeName(entity.getStore().getStoreName())
            .category(entity.getCategory())
            .price(entity.getPrice())
            .mainImage(entity.getItemImages().isEmpty() ? null : entity.getItemImages().get(0).getImgUrl())
            .build();
    }
    public static ItemDTO toListDtoFromElastic(ItemDocument document){
        return ItemDTO.builder()
            .id(document.getItemId())
            .title(document.getTitle())
            .storePk(document.getStorePk())
            .storeName(document.getStoreName())
            .category(document.getCategory())
            .price(document.getPrice())
            .mainImage(document.getMainImage())
            .build();
    }

    public static ItemDTO toDTO(Items entity) {
        return ItemDTO.builder()
            .id(entity.getId())
            .title(entity.getTitle())
            .category(entity.getCategory())
            .price(entity.getPrice())
            .content(entity.getContent())
            .sellerName(entity.getStore().getSeller().getUser().getName())
            .storePk(entity.getStore().getId())
            .storeName(entity.getStore().getStoreName())
            //.star(entity.getFavorites().size())
            .itemImages(
                entity.getItemImages().stream().map(url ->{
                    try{
                        return url.getImgUrl();
                    }catch (Exception e){
                        return null;
                    }
                }).collect(Collectors.toList())
            ).options(DTOMapper.convertList(entity.getOptions().stream(), OptionDTO::toDTO))
            .qnas(entity.getOptions().stream().map(option -> {
                try{
                    return DTOMapper.convertList(option.getQnas().stream(), QNADTO::toDTO);
                }catch (Exception e){
                    return null;
                }
            }).collect(Collectors.toList()))
            .comments(entity.getOptions().stream().map(option -> {
                try{
                    return DTOMapper.convertList(option.getComments().stream(), CommentDTO::toDTO);
                }catch (Exception e){
                    return null;
                }
            }).collect(Collectors.toList()))
            .build();
    }
}
